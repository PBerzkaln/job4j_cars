package ru.job4j.cars.repository;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cars.model.User;

import java.util.List;

import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;

public class HbnUserRepositoryTest {
    private final SessionFactory sf = new MetadataSources(
            new StandardServiceRegistryBuilder().configure().build()
    ).buildMetadata().buildSessionFactory();
    private final CrudRepository crudRepository = new CrudRepository(sf);
    private final HbnUserRepository hbnUserRepository = new HbnUserRepository(crudRepository);

    @AfterEach
    public void wipeTable() {
        var session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete from User").executeUpdate();
        session.getTransaction();
        session.close();
    }

    @Test
    public void whenAddNewUserThenRepoHasSameUser() {
        var user = new User();
        hbnUserRepository.create(user);
        assertThat(hbnUserRepository.findById(user.getId()).get()).isEqualTo(user);
    }

    @Test
    public void whenReplace() {
        var user = new User();
        user.setLogin("user");
        hbnUserRepository.create(user);
        user.setLogin("user2");
        hbnUserRepository.update(user);
        assertThat(hbnUserRepository.findById(user.getId()).get().getLogin()).isEqualTo("user2");

    }

    @Test
    public void whenDelete() {
        var user = new User();
        hbnUserRepository.create(user);
        hbnUserRepository.delete(user.getId());
        assertThat(hbnUserRepository.findById(user.getId())).isEqualTo(empty());
    }

    @Test
    public void whenFindAll() {
        var user1 = new User();
        var user2 = new User();
        hbnUserRepository.create(user1);
        hbnUserRepository.create(user2);
        assertThat(hbnUserRepository.findAllOrderById()).isEqualTo(List.of(user1, user2));
    }

    @Test
    public void whenFindByLike() {
        var user = new User();
        user.setLogin("Some login");
        hbnUserRepository.create(user);
        assertThat(hbnUserRepository.findByLikeLogin("me lo")).isEqualTo(List.of(user));
    }

    @Test
    public void whenFindByLogin() {
        var user = new User();
        user.setLogin("user");
        hbnUserRepository.create(user);
        assertThat(hbnUserRepository.findByLogin(user.getLogin()).get()).isEqualTo(user);
    }
}