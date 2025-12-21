package com.example.movie_rater.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class RateMovieRequest {

    @NotBlank
    @Schema(description = "Review text for a movie. Must not be blank", example = "Movie is great!")
    private String review;

    @Min(1)
    @Max(10)
    @NonNull
    @Schema(description = "Rating for a movie, must be 1-10", example = "10")
    private Integer rating;


}
