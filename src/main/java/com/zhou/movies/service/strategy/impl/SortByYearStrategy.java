package com.zhou.movies.service.strategy.impl;

import com.zhou.movies.pojo.Movie;
import com.zhou.movies.service.strategy.SortDirection;
import com.zhou.movies.service.strategy.SortingStrategy;

import java.util.Comparator;
import java.util.List;

public class SortByYearStrategy implements SortingStrategy {
    @Override
    public void sort(List<Movie> movies, SortDirection direction) {
        Comparator<Movie> comparator = Comparator.comparingInt(Movie::getYear);

        if (direction == SortDirection.DESCENDING)
            comparator = comparator.reversed();

        movies.sort(comparator);
    }
}
