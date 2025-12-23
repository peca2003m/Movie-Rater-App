package com.example.movie_rater.controller;

import com.example.movie_rater.dto.RegisterResponse;
import com.example.movie_rater.dto.UserCreateRequest;
import com.example.movie_rater.dto.UserPreRegisterRequest;
import com.example.movie_rater.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Register Controller", description = "Controller for registering a new user")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;

    @PostMapping("/pre-register")
    public ResponseEntity preRegisterUser(@RequestBody @Validated UserPreRegisterRequest request){

        userService.preRegister(request);
        return ResponseEntity.ok().body("Email sent successfully");
    }

    @Operation(summary = "Register", description = "Put register credentials")
    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody UserCreateRequest request){

        try {
            userService.createNewUser(request);
            return ResponseEntity.ok(new RegisterResponse("Registered successfully. Welcome, " + request.getFirstName() + " " +request.getLastName()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new RegisterResponse(e.getMessage()));
        }
    }



}
