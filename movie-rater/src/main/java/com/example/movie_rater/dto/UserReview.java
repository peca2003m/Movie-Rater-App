package com.example.movie_rater.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReview {

    private String username;

    private String review;

    private LocalDateTime createdAt;

}
