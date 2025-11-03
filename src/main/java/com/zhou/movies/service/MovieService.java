package com.zhou.movies.service;

import com.zhou.movies.pojo.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> getAllMovies();
    void addMovie(Movie movie);
}