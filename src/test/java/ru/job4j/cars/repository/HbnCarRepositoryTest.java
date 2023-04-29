package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Body;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.model.Transmission;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HbnCarRepositoryTest {
    private final static SessionFactory SF = new MetadataSources(
            new StandardServiceRegistryBuilder().configure().build()
    ).buildMetadata().buildSessionFactory();
    private final CrudRepository crudRepository = new CrudRepository(SF);
    private final HbnCarRepository hbnCarRepository = new HbnCarRepository(crudRepository);
    private final HbnEngineRepository hbnEngineRepository = new HbnEngineRepository(crudRepository);
    private final HbnBodyRepository hbnBodyRepository = new HbnBodyRepository(crudRepository);
    private final HbnTransmissionRepository hbnTransmissionRepository =
            new HbnTransmissionRepository(crudRepository);

    @BeforeAll
    public static void initRepos() {
        var engine = new Engine(1, "Diesel");
        var body = new Body(1, "Sedan");
        var transmission = new Transmission(1, "Auto");
        var session = SF.openSession();
        session.beginTransaction();
        session.save(engine);
        session.save(body);
        session.save(transmission);
        session.getTransaction();
        session.close();
    }

    @AfterEach
    public void wipeTable() {
        var session = SF.openSession();
        session.beginTransaction();
        session.createQuery("delete from Car").executeUpdate();
        session.getTransaction();
        session.close();
    }

    @Test
    public void whenAddNewCarThenRepoHasSameCar() {
        var car = new Car();
        car.setName("car");
        car.setBody(hbnBodyRepository.findById(1).get());
        car.setEngine(hbnEngineRepository.findById(1).get());
        car.setTransmission(hbnTransmissionRepository.findById(1).get());
        hbnCarRepository.create(car);
        var rsl = hbnCarRepository.findById(car.getId()).get();
        assertThat(rsl).isEqualTo(car);
    }

    @Test
    public void whenReplace() {
        var car = new Car();
        car.setName("car");
        car.setBody(hbnBodyRepository.findById(1).get());
        car.setEngine(hbnEngineRepository.findById(1).get());
        car.setTransmission(hbnTransmissionRepository.findById(1).get());
        hbnCarRepository.create(car);
        car.setName("car1");
        hbnCarRepository.update(car);
        var rsl = hbnCarRepository.findById(car.getId()).get().getName();
        assertThat(rsl).isEqualTo(car.getName());
    }

    @Test
    public void whenDelete() {
        var car = new Car();
        car.setName("car");
        car.setBody(hbnBodyRepository.findById(1).get());
        car.setEngine(hbnEngineRepository.findById(1).get());
        car.setTransmission(hbnTransmissionRepository.findById(1).get());
        hbnCarRepository.create(car);
        assertThat(hbnCarRepository.delete(car.getId())).isTrue();
    }

    @Test
    public void whenFindAll() {
        var car1 = new Car();
        car1.setName("car");
        car1.setBody(hbnBodyRepository.findById(1).get());
        car1.setEngine(hbnEngineRepository.findById(1).get());
        car1.setTransmission(hbnTransmissionRepository.findById(1).get());
        hbnCarRepository.create(car1);
        var car2 = new Car();
        car2.setName("car");
        car2.setBody(hbnBodyRepository.findById(1).get());
        car2.setEngine(hbnEngineRepository.findById(1).get());
        car2.setTransmission(hbnTransmissionRepository.findById(1).get());
        hbnCarRepository.create(car2);
        var car3 = new Car();
        car3.setName("car");
        car3.setBody(hbnBodyRepository.findById(1).get());
        car3.setEngine(hbnEngineRepository.findById(1).get());
        car3.setTransmission(hbnTransmissionRepository.findById(1).get());
        hbnCarRepository.create(car3);
        assertThat(hbnCarRepository.findAllOrderById()).isEqualTo(List.of(car1, car2, car3));
    }

    @Test
    public void whenFindByName() {
        var car = new Car();
        car.setName("car");
        car.setBody(hbnBodyRepository.findById(1).get());
        car.setEngine(hbnEngineRepository.findById(1).get());
        car.setTransmission(hbnTransmissionRepository.findById(1).get());
        hbnCarRepository.create(car);
        var rsl = hbnCarRepository.findByName(car.getName()).get();
        assertThat(rsl).isEqualTo(car);
    }
}