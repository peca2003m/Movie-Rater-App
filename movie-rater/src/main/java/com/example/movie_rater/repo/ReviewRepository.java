package com.example.movie_rater.repo;

import com.example.movie_rater.entity.MovieEntity;
import com.example.movie_rater.entity.ReviewEntity;
import com.example.movie_rater.entity.UserEntity;
import feign.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {


    @Query("SELECT r FROM ReviewEntity r WHERE r.movie = :movie AND r.user = :user")
    Optional<ReviewEntity> findByMovieAndUser(@Param("movie") MovieEntity movie, @Param("user") UserEntity user);


    @Query("SELECT r FROM ReviewEntity r WHERE r.user = :user")
    List<ReviewEntity> findByUser(@Param("user") UserEntity user);

    @Query(value = "select avg(rating) from review where movie_id = :movieId", nativeQuery = true)
    Double getAvgRating(@Param("movieId") Integer movieId);

    @Query("SELECT r FROM ReviewEntity r WHERE r.movie = :movie ORDER BY r.createdAt DESC")
    List<ReviewEntity> findByMovieOrderByCreatedAtDesc(@Param("movie") MovieEntity movie);

    @Transactional
    @Modifying
    @Query("DELETE FROM ReviewEntity r WHERE r.movie = :movie AND r.user = :user ")
    int deleteByimdbIdAndUser(@Param("movie") MovieEntity movie, @Param("user") UserEntity user);



}
