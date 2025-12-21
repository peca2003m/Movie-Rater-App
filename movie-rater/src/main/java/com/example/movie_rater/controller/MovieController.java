package com.example.movie_rater.controller;


import com.example.movie_rater.dto.MovieDetailsResponse;
import com.example.movie_rater.dto.MovieResponse;
import com.example.movie_rater.dto.RateMovieRequest;
import com.example.movie_rater.dto.SearchMovieResponse;
import com.example.movie_rater.service.MovieService;
import feign.Param;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Movie Controller", description = "Controller for managing movies of logged in user")
@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
@Validated
public class MovieController {


    private final MovieService movieService;

    @Operation(summary = "Search for a movie on imdb", description = "Search on IMDB for titles using search query")
    @GetMapping("/search")
    public List<SearchMovieResponse> searchMovies(@RequestParam String query){

        return  movieService.searchMovies(query);

    }

    @Operation(summary = "Add movie to user collection", description = "Put ImdbId to add movie to collection")
    @PostMapping("/{imdbId}")
    public void addMovieToCollection(@PathVariable String imdbId){

        movieService.addMovieToCollection(imdbId);

    }

    @Operation(summary = "Rate movie", description = "Put ImdbId to rate movie")
    @PutMapping("/{imdbId}")
    public MovieResponse rateMovie(@Parameter(description = "ID from imdb") @PathVariable String imdbId, @RequestBody @Valid RateMovieRequest request){


        return  movieService.rateMovie(imdbId, request);
    }

    @Operation(summary = "See all user movies")
    @GetMapping
    public List<MovieResponse> getAllMovies(){
        return movieService.getAllMoviesForUser();
    }

    @Operation(summary = "Movie Details", description = "Put ImdbId to see movie details")
    @GetMapping("/{imdbId}")
    public MovieDetailsResponse getMovieDetails(@Parameter(description = "ID from imdb") @PathVariable String imdbId){
        return movieService.getMovieDetails(imdbId);
    }

    @DeleteMapping("/{imdbId}")
    public List<MovieResponse> deleteReviewForUser(@PathVariable String imdbId){
        return movieService.deleteReviewForUser(imdbId);
    }


}
