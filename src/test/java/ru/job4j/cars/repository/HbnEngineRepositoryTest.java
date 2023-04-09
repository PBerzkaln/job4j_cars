package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Engine;

import java.util.List;

import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;

public class HbnEngineRepositoryTest {
    private final SessionFactory sf = new MetadataSources(
            new StandardServiceRegistryBuilder().configure().build()
    ).buildMetadata().buildSessionFactory();
    private final CrudRepository crudRepository = new CrudRepository(sf);
    private final HbnEngineRepository hbnEngineRepository = new HbnEngineRepository(crudRepository);

    @AfterEach
    public void wipeTable() {
        var session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from Engine").executeUpdate();
        session.getTransaction();
        session.close();
    }

    @Test
    public void whenAddNewEngineThenRepoHasSameEngine() {
        var engine = new Engine();
        hbnEngineRepository.create(engine);
        assertThat(hbnEngineRepository.findById(engine.getId()).get()).isEqualTo(engine);
    }

    @Test
    public void whenReplace() {
        var engine = new Engine();
        engine.setName("engine");
        hbnEngineRepository.create(engine);
        engine.setName("engine2");
        hbnEngineRepository.update(engine);
        assertThat(hbnEngineRepository.findById(engine.getId()).get().getName()).isEqualTo("engine2");

    }

    @Test
    public void whenDelete() {
        var engine = new Engine();
        hbnEngineRepository.create(engine);
        hbnEngineRepository.delete(engine.getId());
        assertThat(hbnEngineRepository.findById(engine.getId())).isEqualTo(empty());
    }

    @Test
    public void whenFindAll() {
        var engine1 = new Engine();
        var engine2 = new Engine();
        hbnEngineRepository.create(engine1);
        hbnEngineRepository.create(engine2);
        assertThat(hbnEngineRepository.findAllOrderById()).isEqualTo(List.of(engine1, engine2));
    }

    @Test
    public void whenFindByName() {
        var engine = new Engine();
        engine.setName("engine");
        hbnEngineRepository.create(engine);
        assertThat(hbnEngineRepository.findByName(engine.getName()).get()).isEqualTo(engine);
    }
}