package com.zhou.movies.controller;

import com.zhou.movies.dto.MovieDTO;
import com.zhou.movies.pojo.Movie;
import com.zhou.movies.service.MovieService;

import java.util.List;

public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    public boolean addMovieRequest(MovieDTO dto) {
        // 1. Checking fields
        if (dto.title.isEmpty() || dto.director.isEmpty() || dto.yearStr.isEmpty()) {
            System.out.println("Error： Title, Director or Year ==> Cannot be empty!");
            return false;
        }

        // 2. Try to insert a Movie object
        try {
            int year = Integer.parseInt(dto.yearStr);

            Movie movie = new Movie.Builder(dto.title, dto.director)
                    .year(year)
                    .category(dto.category)
                    .status(dto.status)
                    .rating(dto.rating)
                    .build();

            movieService.addMovie(movie);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Conversion Failed: Year ==> Should be a number!");
            return false;
        }
    }

    public List<Movie> getAllMovies(){
        return movieService.getAllMovies();
    }
}