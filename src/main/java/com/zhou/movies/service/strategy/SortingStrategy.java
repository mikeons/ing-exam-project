package com.zhou.movies.service.strategy;

import com.zhou.movies.pojo.Movie;

import java.util.List;

public interface SortingStrategy {
    void sort(List<Movie> movies, SortDirection direction);
}
