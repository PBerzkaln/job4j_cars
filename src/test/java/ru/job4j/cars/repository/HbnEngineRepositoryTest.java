package ru.job4j.cars.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Engine;

import java.util.List;

public class HbnEngineRepositoryTest {
    private final SessionFactory sf = new MetadataSources(
            new StandardServiceRegistryBuilder().configure().build()
    ).buildMetadata().buildSessionFactory();
    private final CrudRepository crudRepository = new CrudRepository(sf);
    private final HbnEngineRepository hbnEngineRepository = new HbnEngineRepository(crudRepository);

    @BeforeEach
    public void initTable() {
        var session = sf.openSession();
        session.beginTransaction();
        var engine1 = new Engine(0, "Бензиновый");
        var engine2 = new Engine(0, "Дизельный");
        session.save(engine1);
        session.save(engine2);
        session.getTransaction();
        session.close();
    }

    @AfterEach
    public void wipeTable() {
        var session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from Engine").executeUpdate();
        session.getTransaction();
        session.close();
    }

    @Test
    public void whenFindAll() {
      var engines = List.of(new Engine(1, "Бензиновый"), new Engine(2, "Дизельный"));
        assertThat(hbnEngineRepository.findAllOrderById()).isEqualTo(engines);
    }

    @Test
    public void whenFindById() {
        var engine = new Engine(1, "Бензиновый");
        assertThat(hbnEngineRepository.findById(1).get()).isEqualTo(engine);
    }
}