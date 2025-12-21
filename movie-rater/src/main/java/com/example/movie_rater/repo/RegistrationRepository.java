package com.example.movie_rater.repo;


import com.example.movie_rater.entity.RegistrationEntity;
import com.example.movie_rater.entity.UserEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<RegistrationEntity, Integer> {




}
