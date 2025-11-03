package com.zhou.movies.controller;

import com.zhou.movies.pojo.Category;
import com.zhou.movies.pojo.Movie;
import com.zhou.movies.pojo.Status;
import com.zhou.movies.service.MovieService;

import java.util.List;

public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    public boolean addMovieRequest(String title, String director, String yearStr,
                                Category category, Status status, Integer rating) {

        // 1. Checking fields
        if (title.isEmpty() || director.isEmpty() || yearStr.isEmpty()) {
            System.out.println("Error： Title, Director or Year ==> Cannot be empty!");
            return false;
        }

        // 2. Try to insert a Movie object
        try {
            int year = Integer.parseInt(yearStr);

            Movie movie = new Movie(title, director, year, category, status, rating);

            movieService.addMovie(movie);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Conversion Failed: Year ==> Should be a number!");
        }

        return false;
    }

    public List<Movie> getAllMovies(){
        return movieService.getAllMovies();
    }
}