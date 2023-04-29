package ru.job4j.cars.service;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cars.dto.FileDto;
import ru.job4j.cars.dto.PostCreateDto;
import ru.job4j.cars.dto.PostDto;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.model.Owner;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@ThreadSafe
@AllArgsConstructor
public class SimplePostService implements PostService {
    private final PostRepository postRepository;
    private final FileService fileService;
    private final CarRepository carRepository;
    private final OwnerRepository ownerRepository;
    private final EngineRepository engineRepository;
    protected final TransmissionRepository transmissionRepository;
    private final BodyRepository bodyRepository;

    private List<PostDto> postDtoBuilder(List<Post> posts) {
        return posts.stream().map(post ->
                new PostDto(post.getId(), post.getPrice(), post.getFile().getId(), post.isSold(),
                        bodyRepository.findById(post.getCar().getBody().getId()).get().getName(),
                        transmissionRepository.findById(post.getCar().getTransmission().getId()).get().getName(),
                        engineRepository.findById(post.getCar().getEngine().getId()).get().getName(),
                        post.getCar().getName(), post.getDescription(), post.getUser(),
                        post.getCreated())).toList();
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
    public Post create(PostCreateDto postDto, FileDto image) {
        var owner = new Owner();
        owner.setName(postDto.getUser().getLogin());
        owner.setUser(postDto.getUser());
        ownerRepository.create(owner);
        var car = new Car();
        car.setName(postDto.getCarName());
        car.setEngine(engineRepository.findById(postDto.getEngineId()).get());
        car.setTransmission(transmissionRepository.findById(postDto.getTransmissionId()).get());
        car.setBody(bodyRepository.findById(postDto.getBodyId()).get());
        car.setOwners(Set.of(owner));
        carRepository.create(car);
        var post = new Post();
        post.setPrice(postDto.getPrice());
        post.setSold(false);
        post.setDescription(postDto.getDescription());
        post.setUser(postDto.getUser());
        post.setCar(car);
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