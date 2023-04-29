package ru.job4j.cars.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.*;

import java.time.LocalDateTime;
import java.util.List;

class HbnPostRepositoryTest {
    private final static SessionFactory SF = new MetadataSources(
            new StandardServiceRegistryBuilder().configure().build()
    ).buildMetadata().buildSessionFactory();
    private final static CrudRepository CRUD_REPOSITORY = new CrudRepository(SF);
    private final static HbnPostRepository HBN_POST_REPOSITORY = new HbnPostRepository(CRUD_REPOSITORY);
    private final static HbnEngineRepository HBN_ENGINE_REPOSITORY = new HbnEngineRepository(CRUD_REPOSITORY);
    private final static HbnBodyRepository HBN_BODY_REPOSITORY = new HbnBodyRepository(CRUD_REPOSITORY);
    private final static HbnTransmissionRepository HBN_TRANSMISSION_REPOSITORY =
            new HbnTransmissionRepository(CRUD_REPOSITORY);
    private final HbnFileRepository hbnFileRepository = new HbnFileRepository(CRUD_REPOSITORY);
    private final HbnCarRepository hbnCarRepository = new HbnCarRepository(CRUD_REPOSITORY);


    @BeforeAll
    public static void initRepos() {
        var engine = new Engine(1, "Diesel");
        var body = new Body(1, "Sedan");
        var transmission = new Transmission(1, "Auto");
        var file = new File();
        file.setName("name");
        file.setPath("some/path");
        var session = SF.openSession();
        session.beginTransaction();
        session.save(engine);
        session.save(body);
        session.save(transmission);
        session.save(file);
        session.getTransaction().commit();
        var car = new Car();
        car.setName("car");
        car.setBody(HBN_BODY_REPOSITORY.findById(1).get());
        car.setEngine(HBN_ENGINE_REPOSITORY.findById(1).get());
        car.setTransmission(HBN_TRANSMISSION_REPOSITORY.findById(1).get());
        session.beginTransaction();
        session.save(car);
        session.getTransaction().commit();
        session.close();
    }

    @AfterEach
    public void wipeTable() {
        var session = SF.openSession();
        session.beginTransaction();
        session.createQuery("delete from Post").executeUpdate();
        session.getTransaction();
        session.close();
    }

    @Test
    public void whenAddNewPostThenRepoHasSamePost() {
        var post = new Post();
        post.setDescription("some desc");
        post.setCreated(LocalDateTime.now());
        post.setCar(hbnCarRepository.findById(1).get());
        post.setFile(hbnFileRepository.findById(1).get());
        HBN_POST_REPOSITORY.create(post);
        assertThat(HBN_POST_REPOSITORY.findById(post.getId()).get()).isEqualTo(post);
    }

    @Test
    public void whenReplace() {
        var post = new Post();
        post.setDescription("some desc");
        post.setCreated(LocalDateTime.now());
        post.setCar(hbnCarRepository.findById(1).get());
        post.setFile(hbnFileRepository.findById(1).get());
        HBN_POST_REPOSITORY.create(post);
        post.setDescription("some new desc");
        HBN_POST_REPOSITORY.update(post);
        var rsl = HBN_POST_REPOSITORY.findById(post.getId()).get().getDescription();
        assertThat(rsl).isEqualTo(post.getDescription());
    }

    @Test
    public void whenDelete() {
        var post = new Post();
        post.setDescription("some desc");
        post.setCreated(LocalDateTime.now());
        post.setCar(hbnCarRepository.findById(1).get());
        post.setFile(hbnFileRepository.findById(1).get());
        HBN_POST_REPOSITORY.create(post);
        assertThat(HBN_POST_REPOSITORY.delete(post.getId())).isTrue();
    }

    @Test
    public void whenFindAll() {
        var post1 = new Post();
        post1.setDescription("some desc");
        post1.setCreated(LocalDateTime.now());
        post1.setCar(hbnCarRepository.findById(1).get());
        post1.setFile(hbnFileRepository.findById(1).get());
        HBN_POST_REPOSITORY.create(post1);
        var post2 = new Post();
        post2.setDescription("some desc");
        post2.setCreated(LocalDateTime.now());
        post2.setCar(hbnCarRepository.findById(1).get());
        post2.setFile(hbnFileRepository.findById(1).get());
        HBN_POST_REPOSITORY.create(post2);
        var post3 = new Post();
        post3.setDescription("some desc");
        post3.setCreated(LocalDateTime.now());
        post3.setCar(hbnCarRepository.findById(1).get());
        post3.setFile(hbnFileRepository.findById(1).get());
        HBN_POST_REPOSITORY.create(post3);
        assertThat(HBN_POST_REPOSITORY.findAllOrderById()).isEqualTo(List.of(post1, post2, post3));
    }

    @Test
    public void whenFindAllForTheLastDay() {
        var post = new Post();
        post.setDescription("some desc");
        post.setCreated(LocalDateTime.now());
        post.setCar(hbnCarRepository.findById(1).get());
        post.setFile(hbnFileRepository.findById(1).get());
        HBN_POST_REPOSITORY.create(post);
        assertThat(HBN_POST_REPOSITORY.findAllForTheLastDay()).isEqualTo(List.of(post));
    }

    @Test
    public void whenFindWithPhoto() {
        var post = new Post();
        post.setDescription("some desc");
        post.setCreated(LocalDateTime.now());
        post.setCar(hbnCarRepository.findById(1).get());
        post.setFile(hbnFileRepository.findById(1).get());
        HBN_POST_REPOSITORY.create(post);
        assertThat(HBN_POST_REPOSITORY.findWithPhoto()).isEqualTo(List.of(post));
    }

    @Test
    public void whenFindByModel() {
        var post = new Post();
        post.setDescription("some desc");
        post.setCreated(LocalDateTime.now());
        post.setCar(hbnCarRepository.findById(1).get());
        post.setFile(hbnFileRepository.findById(1).get());
        HBN_POST_REPOSITORY.create(post);
        assertThat(HBN_POST_REPOSITORY.findByModel("car")).isEqualTo(List.of(post));
    }
}