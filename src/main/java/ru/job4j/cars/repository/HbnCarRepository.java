package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Car;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HbnCarRepository implements CarRepository {
    private final CrudRepository crudRepository;

    public Car create(Car car) {
        crudRepository.run((session -> session.save(car)));
        return car;
    }

    public boolean update(Car car) {
        crudRepository.run(session -> session.merge(car));
        return true;
    }

    public boolean delete(int carId) {
        return crudRepository.booleanQuery("DELETE Car WHERE id = :fId",
                Map.of("fId", carId));
    }

    public List<Car> findAllOrderById() {
        return crudRepository.query(
                "FROM Car AS c JOIN FETCH c.engine "
                        + "JOIN FETCH c.body "
                        + "JOIN FETCH c.transmission ORDER BY c.id ASC", Car.class);
    }

    public Optional<Car> findById(int carId) {
        return crudRepository.optional(
                "FROM Car AS c JOIN FETCH c.engine "
                        + "JOIN FETCH c.body "
                        + "JOIN FETCH c.transmission WHERE c.id = :fId", Car.class,
                Map.of("fId", carId)
        );
    }

    public Optional<Car> findByName(String name) {
        return crudRepository.optional(
                "FROM Car AS c JOIN FETCH c.engine "
                        + "JOIN FETCH c.body "
                        + "JOIN FETCH c.transmission WHERE c.name = :fname", Car.class,
                Map.of("fname", name)
        );
    }
}