package com.example.movie_rater.repo;

import com.example.movie_rater.entity.MovieEntity;
import com.example.movie_rater.entity.UserEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Integer> {


    @Query("SELECT m FROM MovieEntity m WHERE m.imdbId = :imdbId")
    Optional<MovieEntity> findByImdbId(@Param("imdbId") String imdbId);


}
