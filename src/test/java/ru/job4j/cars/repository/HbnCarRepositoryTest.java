package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.Body;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.model.Transmission;

import java.util.List;

import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;

public class HbnCarRepositoryTest {
    private final SessionFactory sf = new MetadataSources(
            new StandardServiceRegistryBuilder().configure().build()
    ).buildMetadata().buildSessionFactory();
    private final CrudRepository crudRepository = new CrudRepository(sf);
    private final HbnCarRepository hbnCarRepository = new HbnCarRepository(crudRepository);
    private final HbnEngineRepository hbnEngineRepository = new HbnEngineRepository(crudRepository);
    private final HbnBodyRepository hbnBodyRepository = new HbnBodyRepository(crudRepository);
    private final HbnTransmissionRepository hbnTransmissionRepository =
            new HbnTransmissionRepository(crudRepository);

    @AfterEach
    public void wipeTable() {
        var session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from Car").executeUpdate();
        session.getTransaction();
        session.close();
    }

    @Test
    public void whenAddNewCarThenRepoHasSameCar() {
        var engine = new Engine();
        engine.setName("engine");
        hbnEngineRepository.create(engine);
        var transmission = new Transmission();
        transmission.setName("auto");
        hbnTransmissionRepository.create(transmission);
        var body = new Body();
        body.setName("sedan");
        hbnBodyRepository.create(body);
        var car = new Car();
        car.setName("car");
        car.setEngine(engine);
        car.setBody(body);
        car.setTransmission(transmission);
        hbnCarRepository.create(car);
        var rsl = hbnCarRepository.findById(car.getId()).get();
        assertThat(rsl).isEqualTo(car);
    }

    @Test
    public void whenReplace() {
        var engine = new Engine();
        engine.setName("engine");
        hbnEngineRepository.create(engine);
        var transmission = new Transmission();
        transmission.setName("auto");
        hbnTransmissionRepository.create(transmission);
        var body = new Body();
        body.setName("sedan");
        hbnBodyRepository.create(body);
        var car = new Car();
        car.setName("car");
        car.setEngine(engine);
        car.setBody(body);
        car.setTransmission(transmission);
        hbnCarRepository.create(car);
        car.setName("car1");
        hbnCarRepository.update(car);
        var rsl = hbnCarRepository.findById(car.getId()).get().getName();
        assertThat(rsl).isEqualTo(car.getName());
    }

    @Test
    public void whenDelete() {
        var engine = new Engine();
        engine.setName("engine");
        hbnEngineRepository.create(engine);
        var transmission = new Transmission();
        transmission.setName("auto");
        hbnTransmissionRepository.create(transmission);
        var body = new Body();
        body.setName("sedan");
        hbnBodyRepository.create(body);
        var car = new Car();
        car.setName("car");
        car.setEngine(engine);
        car.setBody(body);
        car.setTransmission(transmission);
        hbnCarRepository.create(car);
        hbnCarRepository.delete(car.getId());
        var rsl = hbnCarRepository.findById(car.getId());
        assertThat(rsl).isEqualTo(empty());
    }

    @Test
    public void whenFindAll() {
        var engine = new Engine();
        engine.setName("engine");
        hbnEngineRepository.create(engine);
        var transmission = new Transmission();
        transmission.setName("auto");
        hbnTransmissionRepository.create(transmission);
        var body = new Body();
        body.setName("sedan");
        hbnBodyRepository.create(body);
        var car1 = new Car();
        var car2 = new Car();
        var car3 = new Car();
        car1.setName("car1");
        car1.setEngine(engine);
        car1.setBody(body);
        car1.setTransmission(transmission);
        car2.setName("car2");
        car2.setEngine(engine);
        car2.setBody(body);
        car2.setTransmission(transmission);
        car3.setName("car3");
        car3.setEngine(engine);
        car3.setBody(body);
        car3.setTransmission(transmission);
        hbnCarRepository.create(car1);
        hbnCarRepository.create(car2);
        hbnCarRepository.create(car3);
        assertThat(hbnCarRepository.findAllOrderById()).isEqualTo(List.of(car1, car2, car3));
    }

    @Test
    public void whenFindByName() {
        var engine = new Engine();
        engine.setName("engine");
        hbnEngineRepository.create(engine);
        var transmission = new Transmission();
        transmission.setName("auto");
        hbnTransmissionRepository.create(transmission);
        var body = new Body();
        body.setName("sedan");
        hbnBodyRepository.create(body);
        var car = new Car();
        car.setName("car");
        car.setEngine(engine);
        car.setBody(body);
        car.setTransmission(transmission);
        hbnCarRepository.create(car);
        var rsl = hbnCarRepository.findByName(car.getName()).get();
        assertThat(rsl).isEqualTo(car);
    }
}