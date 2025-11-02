package com.zhou.movies.service.impl;

import com.zhou.movies.pojo.Category;
import com.zhou.movies.pojo.Movie;
import com.zhou.movies.pojo.Status;
import com.zhou.movies.service.MovieService;

import java.util.ArrayList;
import java.util.List;

public class MovieServiceImpl implements MovieService {
    private final List<Movie> movies;

    public MovieServiceImpl(){
        movies = new ArrayList<>();

        movies.add(new Movie("Bad man", "William X.", 2020, Category.ACTION, Status.WATCHED, 1));
        movies.add(new Movie("Nezha2", "Boy cow", 2022,  Category.COMEDY, Status.WANT_TO_WATCH, 5));
    }

    public List<Movie> getAllMovies(){
        return movies;
    }

    public void addMovie(Movie movie){
        movies.add(movie);
    }
}
