package com.example.movie_rater.service;


import com.example.movie_rater.dto.UserCreateRequest;
import com.example.movie_rater.dto.UserPreRegisterRequest;
import com.example.movie_rater.dto.UserResponse;
import com.example.movie_rater.entity.RegistrationEntity;
import com.example.movie_rater.entity.UserEntity;
import com.example.movie_rater.exception.ApiException;
import com.example.movie_rater.repo.RegistrationRepository;
import com.example.movie_rater.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RegistrationRepository registrationRepository;
    private final EmailService emailService;



    public UserEntity createNewUser(UserCreateRequest request){

        Optional<RegistrationEntity> registrationOpt = registrationRepository.findByToken(request.getToken());

        if (!registrationOpt.isPresent()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid or expired token!");
        }

        RegistrationEntity registration = registrationOpt.get();
        String email = registration.getEmail();

        Optional<UserEntity> existingUser = userRepository.findByUsername(email);
        if (existingUser.isPresent()) {
            registrationRepository.delete(registration);
            throw new ApiException(HttpStatus.BAD_REQUEST, "User already registered!");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        UserEntity user = new UserEntity(
                null,
                request.getFirstName(),
                request.getLastName(),
                email,
                encodedPassword
        );

        UserEntity savedUser = userRepository.save(user);

        registrationRepository.delete(registration);

        return savedUser;

    }


    public UserResponse getLoggedInUser() {

        UserEntity userEntity = getLoggedInUserEntity();
        return new UserResponse(userEntity.getFirst_name(), userEntity.getLast_name(), userEntity.getUsername());
    }

    public UserEntity getLoggedInUserEntity() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()){
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        String username = authentication.getName();

        return userRepository.findByUsername(username).orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Unauthorized"));


    }

    public void preRegister(UserPreRegisterRequest request) {

        String email = request.getEmail();

        Optional<UserEntity> existingUser = userRepository.findByUsername(email);
        if (existingUser.isPresent()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "User with this email is already registered!");
        }

        Optional<RegistrationEntity> existingRegistration = registrationRepository.findById(email);

        UUID token;
        if (existingRegistration.isPresent()) {
            token = existingRegistration.get().getToken();
        } else {
            token = UUID.randomUUID();
            RegistrationEntity entity = new RegistrationEntity(email, token);
            registrationRepository.save(entity);
        }

        emailService.sendRegistrationEmail(email, token);



    }
}
