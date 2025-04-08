package com.example.test_task.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UsersPhotoListenerTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideDatasourceProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
    }

    @Test
    @DisplayName("createdAt и updatedAt устанавливаются при создании UsersPhoto")
    void shouldSetTimestampsOnPhotoPersist() {
        Users user = buildUser("photo1@example.com");
        entityManager.persist(user);

        UsersPhoto photo = new UsersPhoto();
        photo.setUrlPhoto("https://example.com/photo.jpg");
        photo.setOwner(user);
        entityManager.persist(photo);
        entityManager.flush();

        assertThat(photo.getCreatedAt()).isNotNull();
        assertThat(photo.getUpdatedAt()).isNotNull();
        assertThat(photo.getCreatedAt()).isEqualTo(photo.getUpdatedAt());
    }

    @Test
    @DisplayName("updatedAt изменяется при обновлении фото")
    void shouldUpdateUpdatedAtOnMerge() throws InterruptedException {
        Users user = buildUser("merge@example.com");
        entityManager.persist(user);

        UsersPhoto photo = new UsersPhoto();
        photo.setUrlPhoto("https://example.com/photo.jpg");
        photo.setOwner(user);
        entityManager.persist(photo);
        entityManager.flush();

        LocalDateTime beforeUpdate = photo.getUpdatedAt();
        Thread.sleep(1000);

        photo.setUrlPhoto("https://example.com/photo-updated.jpg");
        UsersPhoto merged = entityManager.merge(photo);
        entityManager.flush();
        entityManager.clear();

        assertThat(merged.getUpdatedAt()).isAfter(beforeUpdate);
    }

    @Test
    @DisplayName("createdAt и updatedAt устанавливаются при создании пользователя")
    void shouldSetTimestampsOnUserPersist() {
        Users user = buildUser("timestamps@example.com");
        entityManager.persist(user);
        entityManager.flush();

        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
        assertThat(user.getCreatedAt()).isEqualTo(user.getUpdatedAt());
    }

    @Test
    @DisplayName("updatedAt изменяется при изменении пользователя")
    void shouldUpdateUserUpdatedAtOnMerge() throws InterruptedException {
        Users user = buildUser("updated@example.com");
        entityManager.persist(user);
        entityManager.flush();

        LocalDateTime beforeUpdate = user.getUpdatedAt();
        Thread.sleep(1000);

        user.setFirstName("UpdatedName");
        Users merged = entityManager.merge(user);
        entityManager.flush();
        entityManager.clear();

        assertThat(merged.getUpdatedAt()).isAfter(beforeUpdate);
    }

    @Test
    @DisplayName("фото связывается с пользователем")
    void shouldLinkPhotoToUser() {
        Users user = buildUser("link@example.com");
        entityManager.persist(user);

        UsersPhoto photo = new UsersPhoto();
        photo.setUrlPhoto("https://example.com/photo.jpg");
        photo.setOwner(user);
        entityManager.persist(photo);
        entityManager.flush();

        assertThat(photo.getOwner()).isEqualTo(user);
        assertThat(photo.getOwner().getEmail()).isEqualTo("link@example.com");
    }

    @Test
    @DisplayName("ошибка при сохранении фото без владельца")
    void shouldFailToPersistPhotoWithoutOwner() {
        UsersPhoto photo = new UsersPhoto();
        photo.setUrlPhoto("https://example.com/photo.jpg");

        Throwable thrown = catchThrowable(() -> {
            entityManager.persist(photo);
            entityManager.flush();
        });

        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).contains("null value in column \"user_id\"");
    }

    @Test
    @DisplayName("ошибка при сохранении фото без url")
    void shouldFailToPersistPhotoWithoutUrl() {
        Users user = buildUser("photourl@example.com");
        entityManager.persist(user);

        UsersPhoto photo = new UsersPhoto();
        photo.setOwner(user);

        Throwable thrown = catchThrowable(() -> {
            entityManager.persist(photo);
            entityManager.flush();
        });

        assertThat(thrown).isNotNull();
        assertThat(thrown.getMessage()).contains("null value in column \"url_photo\"");
    }


    private Users buildUser(String email) {
        Users user = new Users();
        user.setFirstName("Name");
        user.setLastName("Last");
        user.setEmail(email);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }
}