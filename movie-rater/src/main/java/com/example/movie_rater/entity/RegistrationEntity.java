package com.example.movie_rater.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "registration", timeToLive = 3600)
public class RegistrationEntity {

    @Id
    private String email; // Redis koristi email kao primarni ključ

    @Indexed
    private UUID token;

    /*
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "token")
    private UUID token;
    */

}
