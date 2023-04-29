package ru.job4j.cars.repository;

import static org.assertj.core.api.Assertions.assertThat;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Transmission;

import java.util.List;

public class HbnTransmissionRepositoryTest {
    private final SessionFactory sf = new MetadataSources(
            new StandardServiceRegistryBuilder().configure().build()
    ).buildMetadata().buildSessionFactory();
    private final CrudRepository crudRepository = new CrudRepository(sf);
    private final HbnTransmissionRepository hbnTransmissionRepository =
            new HbnTransmissionRepository(crudRepository);

    @BeforeEach
    public void initTable() {
        var session = sf.openSession();
        session.beginTransaction();
        var transmission1 = new Transmission(0, "Автоматическая");
        var transmission2 = new Transmission(0, "Ручная");
        session.save(transmission1);
        session.save(transmission2);
        session.getTransaction();
        session.close();
    }

    @AfterEach
    public void wipeTable() {
        var session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from Transmission").executeUpdate();
        session.getTransaction();
        session.close();
    }

    @Test
    public void whenFindAll() {
        var transmissions = List.of(new Transmission(1, "Автоматическая"),
                new Transmission(2, "Ручная"));
        assertThat(hbnTransmissionRepository.findAllOrderById()).isEqualTo(transmissions);
    }

    @Test
    public void whenFindByName() {
        var transmission = new Transmission(1, "Автоматическая");
        assertThat(hbnTransmissionRepository.findById(1).get()).isEqualTo(transmission);
    }
}