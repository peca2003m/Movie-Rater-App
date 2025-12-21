package com.example.movie_rater.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchMovieResponse {

    private String imdbId;

    private String title;

    private Integer year;

    private String imageUrl;



}
