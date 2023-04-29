package ru.job4j.cars.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.job4j.cars.model.User;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PostCreateDto {
    @EqualsAndHashCode.Include
    private int id;
    private int price;
    private int fileId;
    private int bodyId;
    private int transmissionId;
    private int engineId;
    private boolean sold;
    private String carName;
    private String description;
    private LocalDateTime created;
    private User user;
}