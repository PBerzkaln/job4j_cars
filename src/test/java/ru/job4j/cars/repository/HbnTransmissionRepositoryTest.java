package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.model.Transmission;

import java.util.List;

import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class HbnTransmissionRepositoryTest {
    private final SessionFactory sf = new MetadataSources(
            new StandardServiceRegistryBuilder().configure().build()
    ).buildMetadata().buildSessionFactory();
    private final CrudRepository crudRepository = new CrudRepository(sf);
    private final HbnTransmissionRepository hbnTransmissionRepository =
            new HbnTransmissionRepository(crudRepository);

    @AfterEach
    public void wipeTable() {
        var session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from Transmission").executeUpdate();
        session.getTransaction();
        session.close();
    }

    @Test
    public void whenAddNewTransmissionThenRepoHasSameTransmission() {
        var transmission = new Transmission();
        transmission.setName("auto");
        hbnTransmissionRepository.create(transmission);
        assertThat(hbnTransmissionRepository.findById(transmission.getId()).get()).isEqualTo(transmission);
    }

    @Test
    public void whenReplace() {
        var transmission = new Transmission();
        transmission.setName("auto");
        hbnTransmissionRepository.create(transmission);
        transmission.setName("new_auto");
        hbnTransmissionRepository.update(transmission);
        assertThat(hbnTransmissionRepository.findById(transmission.getId()).get().getName())
                .isEqualTo("new_auto");

    }

    @Test
    public void whenDelete() {
        var transmission = new Transmission();
        transmission.setName("auto");
        hbnTransmissionRepository.create(transmission);
        hbnTransmissionRepository.delete(transmission.getId());
        assertThat(hbnTransmissionRepository.findById(transmission.getId())).isEqualTo(empty());
    }

    @Test
    public void whenFindAll() {
        var transmission1 = new Transmission();
        var transmission2 = new Transmission();
        transmission1.setName("auto1");
        transmission2.setName("auto2");
        hbnTransmissionRepository.create(transmission1);
        hbnTransmissionRepository.create(transmission2);
        assertThat(hbnTransmissionRepository.findAllOrderById()).isEqualTo(List.of(transmission1, transmission2));
    }

    @Test
    public void whenFindByName() {
        var transmission = new Transmission();
        transmission.setName("auto");
        hbnTransmissionRepository.create(transmission);
        assertThat(hbnTransmissionRepository.findByName(transmission.getName()).get()).isEqualTo(transmission);
    }
}