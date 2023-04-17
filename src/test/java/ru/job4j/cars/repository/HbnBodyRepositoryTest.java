package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Body;

import java.util.List;

import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;

public class HbnBodyRepositoryTest {
    private final SessionFactory sf = new MetadataSources(
            new StandardServiceRegistryBuilder().configure().build()
    ).buildMetadata().buildSessionFactory();
    private final CrudRepository crudRepository = new CrudRepository(sf);
    private final HbnBodyRepository hbnBodyRepository = new HbnBodyRepository(crudRepository);

    @AfterEach
    public void wipeTable() {
        var session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from Body").executeUpdate();
        session.getTransaction();
        session.close();
    }

    @Test
    public void whenAddNewBodyThenRepoHasSameBody() {
        var body = new Body();
        body.setName("sedan");
        hbnBodyRepository.create(body);
        assertThat(hbnBodyRepository.findById(body.getId()).get()).isEqualTo(body);
    }

    @Test
    public void whenReplace() {
        var body = new Body();
        body.setName("sedan");
        hbnBodyRepository.create(body);
        body.setName("minivan");
        hbnBodyRepository.update(body);
        assertThat(hbnBodyRepository.findById(body.getId()).get().getName()).isEqualTo("minivan");

    }

    @Test
    public void whenDelete() {
        var body = new Body();
        body.setName("sedan");
        hbnBodyRepository.create(body);
        hbnBodyRepository.delete(body.getId());
        assertThat(hbnBodyRepository.findById(body.getId())).isEqualTo(empty());
    }

    @Test
    public void whenFindAll() {
        var body1 = new Body();
        var body2 = new Body();
        body1.setName("sedan");
        body2.setName("minivan");
        hbnBodyRepository.create(body1);
        hbnBodyRepository.create(body2);
        assertThat(hbnBodyRepository.findAllOrderById()).isEqualTo(List.of(body1, body2));
    }

    @Test
    public void whenFindByName() {
        var body = new Body();
        body.setName("sedan");
        hbnBodyRepository.create(body);
        assertThat(hbnBodyRepository.findByName(body.getName()).get()).isEqualTo(body);
    }
}