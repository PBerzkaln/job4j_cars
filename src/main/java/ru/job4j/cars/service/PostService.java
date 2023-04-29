package ru.job4j.cars.service;

import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.dto.PostCreateDto;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
    List<PostDto> findAll();
    List<PostDto> getAllWithPhoto();
    List<PostDto> getAllForTheLastDay();
    Post create(PostCreateDto postDto, FileDto image);
    Optional<PostDto> findById(int postId);
    boolean setIsSold(int postId);
    boolean delete(int postId);
}