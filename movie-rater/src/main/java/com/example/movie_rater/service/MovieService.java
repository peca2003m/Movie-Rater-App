package com.example.movie_rater.service;


import com.example.movie_rater.client.ImdbClient;
import com.example.movie_rater.client.dto.ImdbTitle;
import com.example.movie_rater.client.dto.ImdbTitleResponse;
import com.example.movie_rater.dto.*;
import com.example.movie_rater.entity.MovieEntity;
import com.example.movie_rater.entity.ReviewEntity;
import com.example.movie_rater.entity.UserEntity;
import com.example.movie_rater.exception.ApiException;
import com.example.movie_rater.repo.MovieRepository;
import com.example.movie_rater.repo.ReviewRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final ImdbClient imdbClient;
    private final UserService userService;
    private final ReviewRepository reviewRepository;
    private final MovieRepository movieRepository;
    private final StompService stompService;


    public List<SearchMovieResponse> searchMovies(String query) {

        List<ImdbTitle> titles = imdbClient.searchMovies(query,5).getTitles();
        List<SearchMovieResponse> result = new LinkedList<>();
        for (ImdbTitle title : titles){

            SearchMovieResponse s = new SearchMovieResponse();
            s.setImageUrl(title.getPrimaryImage().getUrl());
            s.setYear(title.getStartYear());
            s.setTitle(title.getOriginalTitle());
            s.setImdbId(title.getId());

            result.add(s);

        }
        return result;
    }


    public void addMovieToCollection(String imdbId) {

        MovieEntity movieEntity = getOrCreateMovieByImdbId(imdbId);

        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setUser(userService.getLoggedInUserEntity());
        reviewEntity.setMovie(movieEntity);
        reviewEntity.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(reviewEntity);

    }

    private MovieEntity getOrCreateMovieByImdbId(String imdbId) {

        Optional<MovieEntity> movieEntity = movieRepository.findByImdbId(imdbId);

        if(movieEntity.isPresent()){
            return movieEntity.get();
        }else{
            return addMovieToDb(imdbId);
        }

    }

    private MovieEntity addMovieToDb(String imdbId) {

        ImdbTitleResponse imdbTitleResponse = imdbClient.getMovieById(imdbId);
        MovieEntity movieEntity = new MovieEntity();
        movieEntity.setImdbId(imdbId);
        movieEntity.setTitle(imdbTitleResponse.getPrimaryTitle());
        movieEntity.setImageUrl(imdbTitleResponse.getPrimaryImage().getUrl());
        movieEntity.setDescription(imdbTitleResponse.getPlot());
        movieEntity.setYear(imdbTitleResponse.getStartYear());
        return movieRepository.save(movieEntity);

    }

    @Transactional
    public MovieResponse rateMovie(String imdbId, @Valid RateMovieRequest request) {

        MovieEntity movieEntity = getOrCreateMovieByImdbId(imdbId);
        UserEntity userEntity = userService.getLoggedInUserEntity();

        ReviewEntity reviewEntity = reviewRepository.findByMovieAndUser(movieEntity, userEntity).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Movie with id " + imdbId + " not found for this user"));

        reviewEntity.setRating(request.getRating());
        reviewEntity.setReview(request.getReview());
        reviewEntity.setCreatedAt(LocalDateTime.now());

        ReviewEntity savedEntity = reviewRepository.save(reviewEntity);

        stompService.sendNotification(imdbId, new UserReview(userEntity.getUsername(), savedEntity.getReview(), savedEntity.getCreatedAt()));

        MovieResponse movieResponse = new MovieResponse();
        movieResponse.setImdbId(imdbId);
        movieResponse.setImageUrl(movieEntity.getImageUrl());
        movieResponse.setTitle(movieEntity.getTitle());
        movieResponse.setReview(savedEntity.getReview());
        movieResponse.setRating(savedEntity.getRating());

        return movieResponse;


    }

    public List<MovieResponse> getAllMoviesForUser() {

        List<ReviewEntity> entityList = reviewRepository.findByUser(userService.getLoggedInUserEntity());

        List<MovieResponse> responses = new LinkedList<>();

        for (ReviewEntity entity : entityList){

            MovieResponse movie = new MovieResponse();
            movie.setImdbId(entity.getMovie().getImdbId());
            movie.setImageUrl(entity.getMovie().getImageUrl());
            movie.setTitle(entity.getMovie().getTitle());
            movie.setRating(entity.getRating());
            movie.setReview(entity.getReview());

            responses.add(movie);


        }


        return responses;

    }


    public List<MovieResponse> deleteReviewForUser(String imdbId){

        MovieEntity movieEntity = getOrCreateMovieByImdbId(imdbId);

        reviewRepository.deleteByimdbIdAndUser(movieEntity, userService.getLoggedInUserEntity());


        return getAllMoviesForUser();
    }


    public MovieDetailsResponse getMovieDetails(String imdbId) {

        MovieEntity movieEntity = getOrCreateMovieByImdbId(imdbId);

        Double avgRaiting = reviewRepository.getAvgRating(movieEntity.getId());

        List<ReviewEntity> reviewEntites = reviewRepository.findByMovieOrderByCreatedAtDesc(movieEntity);
        List<UserReview> userReviews = new LinkedList<>();

        for (ReviewEntity reviewEntity : reviewEntites){

            if(reviewEntity.getReview() != null){

                userReviews.add(
                        new UserReview(reviewEntity.getUser().getUsername(),
                                reviewEntity.getReview(),
                                reviewEntity.getCreatedAt()));

            }

        }

        MovieDetailsResponse movieDetailsResponse = new MovieDetailsResponse();
        movieDetailsResponse.setImdbId(movieEntity.getImdbId());
        movieDetailsResponse.setTitle(movieEntity.getTitle());
        movieDetailsResponse.setDescription(movieEntity.getDescription());
        movieDetailsResponse.setAverageRating(avgRaiting);
        movieDetailsResponse.setReviews(userReviews);
        movieDetailsResponse.setImageUrl(movieEntity.getImageUrl());
        return movieDetailsResponse;
    }
}
