package ru.job4j.cars.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Body;
import ru.job4j.cars.repository.HbnBodyRepository;

import java.util.List;
import java.util.Optional;

@Service
@ThreadSafe
@AllArgsConstructor
public class SimpleBodyService implements BodyService {
    private final HbnBodyRepository hbnBodyRepository;

    @Override
    public Optional<Body> save(Body body) {
        return hbnBodyRepository.create(body);
    }

    @Override
    public boolean update(Body body) {
        return hbnBodyRepository.update(body);
    }

    @Override
    public boolean delete(int bodyId) {
        return hbnBodyRepository.delete(bodyId);
    }

    @Override
    public List<Body> findAllOrderById() {
        return hbnBodyRepository.findAllOrderById();
    }

    @Override
    public Optional<Body> findById(int bodyId) {
        return hbnBodyRepository.findById(bodyId);
    }

    @Override
    public Optional<Body> findByName(String name) {
        return hbnBodyRepository.findByName(name);
    }
}