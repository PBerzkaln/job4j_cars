package ru.job4j.cars.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.CarRepository;
import ru.job4j.cars.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
@ThreadSafe
@AllArgsConstructor
public class SimplePostService implements PostService {
    private final PostRepository postRepository;
    private final FileService fileService;
    private final CarRepository carRepository;

    private List<PostDto> postDtoBuilder(List<Post> posts) {
        return posts.stream().map(post ->
                new PostDto(post.getId(), post.getPrice(), post.getFile().getId(), post.isSold(),
                        post.getCar().getName(), post.getUser().getName(), post.getDescription(), post.getCreated(),
                        carRepository.findById(post.getCar().getId()).get().getBody().getName(),
                        carRepository.findById(post.getCar().getId()).get().getTransmission().getName(),
                        carRepository.findById(post.getCar().getId()).get().getEngine().getName())).toList();
    }

    @Override
    public List<PostDto> findAll() {
        return postDtoBuilder(postRepository.findAllOrderById());
    }

    @Override
    public List<PostDto> getAllWithPhoto() {
        return postDtoBuilder(postRepository.findWithPhoto());
    }

    @Override
    public List<PostDto> getAllForTheLastDay() {
        return postDtoBuilder(postRepository.findAllForTheLastDay());
    }

    @Override
    public Optional<Post> create(Post post, FileDto image) {
        saveNewFile(post, image);
        return postRepository.create(post);
    }

    @Override
    public Optional<PostDto> findById(int postId) {
        return postDtoBuilder(List.of(postRepository.findById(postId).get())).stream().findFirst();
    }

    @Override
    public boolean setIsSold(int postId) {
        return postRepository.setIsSold(postId);
    }

    @Override
    public boolean delete(int postId) {
        var post = postRepository.findById(postId).get();
        var isDeleted = postRepository.delete(postId);
        fileService.deleteById(post.getFile().getId());
        return isDeleted;
    }

    private void saveNewFile(Post post, FileDto image) {
        var file = fileService.save(image);
        post.setFile(file.get());
    }
}