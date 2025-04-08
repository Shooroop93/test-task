package com.example.test_task.model;

import com.example.test_task.model.listener.UsersPhotoListener;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users_photo")
@Getter
@Setter
@EntityListeners(UsersPhotoListener.class)
@NoArgsConstructor
public class UsersPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url_photo", nullable = false)
    private String urlPhoto;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private Users owner;

    public UsersPhoto(String urlPhoto, Users owner) {
        this.urlPhoto = urlPhoto;
        this.owner = owner;
    }
}