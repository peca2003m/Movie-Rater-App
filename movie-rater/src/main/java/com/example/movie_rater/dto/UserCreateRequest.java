package com.example.movie_rater.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {

    private String firstName;

    private String lastName;

    private String username;

    private String password;

}
