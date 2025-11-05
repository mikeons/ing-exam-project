package com.zhou.movies.service;

import com.zhou.movies.pojo.Movie;
import com.zhou.movies.service.strategy.SortDirection;
import com.zhou.movies.service.strategy.SortStrategyType;

import java.util.List;

public interface MovieService {
    List<Movie> getAllMovies();
    void addMovie(Movie movie);
    void setSortStrategy(SortStrategyType strategyType);
    void setSortDirection(SortDirection sortDirection);
}