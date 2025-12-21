package com.example.movie_rater.controller;


import com.example.movie_rater.dto.UserResponse;
import com.example.movie_rater.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Controller", description = "Controller for user")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;

    @Operation(summary = "Get logged in user")
    @GetMapping("/me")
    public UserResponse getLoggedInUser(){
        return userService.getLoggedInUser();
    }






}
