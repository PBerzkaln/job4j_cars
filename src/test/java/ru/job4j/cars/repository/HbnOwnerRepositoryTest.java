package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Owner;

import java.util.List;

import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;

public class HbnOwnerRepositoryTest {
    private final SessionFactory sf = new MetadataSources(
            new StandardServiceRegistryBuilder().configure().build()
    ).buildMetadata().buildSessionFactory();
    private final CrudRepository crudRepository = new CrudRepository(sf);
    private final HbnOwnerRepository hbnOwnerRepository = new HbnOwnerRepository(crudRepository);

    @AfterEach
    public void wipeTable() {
        var session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from Owner").executeUpdate();
        session.getTransaction();
        session.close();
    }

    @Test
    public void whenAddNewOwnerThenRepoHasSameOwner() {
        var owner = new Owner();
        hbnOwnerRepository.create(owner);
        assertThat(hbnOwnerRepository.findById(owner.getId()).get()).isEqualTo(owner);
    }

    @Test
    public void whenReplace() {
        var owner = new Owner();
        owner.setName("owner1");
        hbnOwnerRepository.create(owner);
        owner.setName("owner2");
        hbnOwnerRepository.update(owner);
        assertThat(hbnOwnerRepository.findById(owner.getId()).get().getName()).isEqualTo("owner2");

    }

    @Test
    public void whenDelete() {
        var owner = new Owner();
        hbnOwnerRepository.create(owner);
        hbnOwnerRepository.delete(owner.getId());
        assertThat(hbnOwnerRepository.findById(owner.getId())).isEqualTo(empty());
    }

    @Test
    public void whenFindAll() {
        var owner1 = new Owner();
        var owner2 = new Owner();
        hbnOwnerRepository.create(owner1);
        hbnOwnerRepository.create(owner2);
        assertThat(hbnOwnerRepository.findAllOrderById()).isEqualTo(List.of(owner1, owner2));
    }

    @Test
    public void whenFindByName() {
        var owner = new Owner();
        owner.setName("owner");
        hbnOwnerRepository.create(owner);
        assertThat(hbnOwnerRepository.findByName(owner.getName()).get()).isEqualTo(owner);
    }
}