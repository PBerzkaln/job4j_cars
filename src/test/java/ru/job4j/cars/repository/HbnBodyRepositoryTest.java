package ru.job4j.cars.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Body;

import java.util.List;

public class HbnBodyRepositoryTest {
    private final SessionFactory sf = new MetadataSources(
            new StandardServiceRegistryBuilder().configure().build()
    ).buildMetadata().buildSessionFactory();
    private final CrudRepository crudRepository = new CrudRepository(sf);
    private final HbnBodyRepository hbnBodyRepository = new HbnBodyRepository(crudRepository);

    @BeforeEach
    public void initTable() {
        var session = sf.openSession();
        session.beginTransaction();
        var body1 = new Body(0, "Седан");
        var body2 = new Body(0, "Универсал");
        session.save(body1);
        session.save(body2);
        session.getTransaction();
        session.close();
    }

    @AfterEach
    public void wipeTable() {
        var session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from Body").executeUpdate();
        session.getTransaction();
        session.close();
    }

    @Test
    public void whenFindAll() {
        var bodies = List.of(new Body(1, "Седан"), new Body(2, "Универсал"));
        assertThat(hbnBodyRepository.findAllOrderById()).isEqualTo(bodies);
    }

    @Test
    public void whenFindById() {
        var body = new Body(1, "Седан");
        assertThat(hbnBodyRepository.findById(1).get()).isEqualTo(body);
    }
}