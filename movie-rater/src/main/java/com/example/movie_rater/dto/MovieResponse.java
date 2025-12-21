package com.example.movie_rater.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieResponse {

    private String imdbId;

    private String title;

    private String imageUrl;

    private String review;

    private Integer rating;

}
