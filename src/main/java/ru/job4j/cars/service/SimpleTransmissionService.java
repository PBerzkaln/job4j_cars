package ru.job4j.cars.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Transmission;
import ru.job4j.cars.repository.HbnTransmissionRepository;

import java.util.List;
import java.util.Optional;

@Service
@ThreadSafe
@AllArgsConstructor
public class SimpleTransmissionService implements TransmissionService {
    private final HbnTransmissionRepository hbnTransmissionRepository;

    @Override
    public List<Transmission> findAllOrderById() {
        return hbnTransmissionRepository.findAllOrderById();
    }

    @Override
    public Optional<Transmission> findById(int transmissionId) {
        return hbnTransmissionRepository.findById(transmissionId);
    }
}