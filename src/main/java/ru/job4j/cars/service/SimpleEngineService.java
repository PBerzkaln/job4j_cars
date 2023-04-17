package ru.job4j.cars.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.EngineRepository;

import java.util.List;
import java.util.Optional;

@Service
@ThreadSafe
@AllArgsConstructor
public class SimpleEngineService implements EngineService {
    private final EngineRepository engineRepository;

    @Override
    public Optional<Engine> save(Engine engine) {
        return engineRepository.create(engine);
    }

    @Override
    public boolean update(Engine engine) {
        return engineRepository.update(engine);
    }

    @Override
    public boolean delete(int engineId) {
        return engineRepository.delete(engineId);
    }

    @Override
    public List<Engine> findAllOrderById() {
        return engineRepository.findAllOrderById();
    }

    @Override
    public Optional<Engine> findById(int engineId) {
        return engineRepository.findById(engineId);
    }

    @Override
    public Optional<Engine> findByName(String name) {
        return engineRepository.findByName(name);
    }
}