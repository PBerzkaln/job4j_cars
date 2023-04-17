package ru.job4j.cars.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PostDto {
    @EqualsAndHashCode.Include
    private int id;
    private int price;
    private int fileId;
    private boolean sold;
    private String carName;
    private String postOwnerName;
    private String description;
    private LocalDateTime created;
    private String bodyName;
    private String transmissionName;
    private String engineName;
}