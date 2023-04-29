package ru.job4j.cars.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.repository.HbnCarRepository;

import java.util.List;
import java.util.Optional;

@Service
@ThreadSafe
@AllArgsConstructor
public class SimpleCarService implements CarService {
    private final HbnCarRepository hbnCarRepository;

    @Override
    public Car save(Car car) {
        return hbnCarRepository.create(car);
    }

    @Override
    public boolean update(Car car) {
        return hbnCarRepository.update(car);
    }

    @Override
    public boolean delete(int carId) {
        return hbnCarRepository.delete(carId);
    }

    @Override
    public List<Car> findAllOrderById() {
        return hbnCarRepository.findAllOrderById();
    }

    @Override
    public Optional<Car> findById(int carId) {
        return hbnCarRepository.findById(carId);
    }

    @Override
    public Optional<Car> findByName(String name) {
        return hbnCarRepository.findByName(name);
    }
}