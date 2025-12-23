package com.example.movie_rater.repo;


import com.example.movie_rater.entity.RegistrationEntity;
import com.example.movie_rater.entity.UserEntity;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegistrationRepository extends CrudRepository<RegistrationEntity, String> {

    Optional<RegistrationEntity> findByToken(UUID token);


}
