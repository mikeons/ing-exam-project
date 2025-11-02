package com.zhou.movies.controller;

import com.zhou.movies.pojo.Category;
import com.zhou.movies.pojo.Movie;
import com.zhou.movies.pojo.Status;
import com.zhou.movies.service.MovieService;
import com.zhou.movies.view.MovieView;

import java.util.List;

public class MovieController {

    private final MovieService movieService;
    private final MovieView movieView;

    public MovieController(MovieService movieService, MovieView movieView) {
        this.movieService = movieService;
        this.movieView = movieView;
    }

    public void addMovieRequest(String title, String director, String yearStr,
                                Category category, Status status, Integer rating) {

        // 1. Checking fields
        if (title.isEmpty() || director.isEmpty() || yearStr.isEmpty()) {
            System.out.println("Error： Title, Director or Year ==> Cannot be empty!");
            return;
        }

        // 2. Try to insert a Movie object
        try {
            int year = Integer.parseInt(yearStr);

            Movie movie = new Movie(title, director, year, category, status, rating);

            movieService.addMovie(movie);

            movieView.refreshTable(movieService.getAllMovies());

            movieView.clearInputFields();
        } catch (NumberFormatException e) {
            System.out.println("Conversion Failed: Year ==> Should be a number!");
        }
    }

    // --- Getting data from the service and fill the MovieView ---
    public void loadInitialData() {
        List<Movie> movies = movieService.getAllMovies();

        movieView.refreshTable(movies);
    }
}