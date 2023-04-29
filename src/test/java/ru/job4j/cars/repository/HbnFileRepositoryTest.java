package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.File;

import java.util.List;

import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;

public class HbnFileRepositoryTest {
    private final SessionFactory sf = new MetadataSources(
            new StandardServiceRegistryBuilder().configure().build()
    ).buildMetadata().buildSessionFactory();
    private final CrudRepository crudRepository = new CrudRepository(sf);
    private final HbnFileRepository hbnFileRepository = new HbnFileRepository(crudRepository);

    @AfterEach
    public void wipeTable() {
        var session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from File").executeUpdate();
        session.getTransaction();
        session.close();
    }

    @Test
    public void whenAddNewFileThenRepoHasSameFile() {
        var file = new File();
        file.setName("name");
        file.setPath("some/path");
        hbnFileRepository.create(file);
        var rsl = hbnFileRepository.findById(file.getId()).get();
        assertThat(rsl).isEqualTo(file);
    }

    @Test
    public void whenReplace() {
        var file = new File();
        file.setName("name");
        file.setPath("some/path");
        hbnFileRepository.create(file);
        file.setName("new_name");
        file.setPath("new_some/path");
        hbnFileRepository.update(file);
        var rsl1 = hbnFileRepository.findById(file.getId()).get().getName();
        var rsl2 = hbnFileRepository.findById(file.getId()).get().getPath();
        assertThat(rsl1).isEqualTo(file.getName());
        assertThat(rsl2).isEqualTo(file.getPath());
    }

    @Test
    public void whenDelete() {
        var file = new File();
        file.setName("name");
        file.setPath("some/path");
        hbnFileRepository.create(file);
        assertThat(hbnFileRepository.delete(file.getId())).isTrue();
    }

    @Test
    public void whenFindAll() {
        var file1 = new File();
        file1.setName("name1");
        file1.setPath("some/path1");
        var file2 = new File();
        file2.setName("name1");
        file2.setPath("some/path2");
        var file3 = new File();
        file3.setName("name1");
        file3.setPath("some/path3");
        hbnFileRepository.create(file1);
        hbnFileRepository.create(file2);
        hbnFileRepository.create(file3);
        assertThat(hbnFileRepository.findAllOrderById()).isEqualTo(List.of(file1, file2, file3));
    }

    @Test
    public void whenFindByName() {
        var file = new File();
        file.setName("name");
        file.setPath("some/path");
        hbnFileRepository.create(file);
        var rsl = hbnFileRepository.findByName(file.getName()).get();
        assertThat(rsl).isEqualTo(file);
    }
}