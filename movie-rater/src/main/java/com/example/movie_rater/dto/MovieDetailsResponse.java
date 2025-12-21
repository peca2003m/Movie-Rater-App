package com.example.movie_rater.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDetailsResponse {

    private String imdbId;

    private String title;

    private String imageUrl;

    private Double averageRating;

    private List<UserReview> reviews;

    private String description;


}
