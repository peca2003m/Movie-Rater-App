package com.example.movie_rater.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {

    private UUID token;

    private String firstName;

    private String lastName;

    //private String username;

    private String password;

}
