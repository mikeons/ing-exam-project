package com.zhou.movies.service;

import com.zhou.movies.dto.MovieDTO;
import com.zhou.movies.pojo.Category;
import com.zhou.movies.pojo.Movie;
import com.zhou.movies.pojo.Status;
import com.zhou.movies.service.strategy.SortDirection;
import com.zhou.movies.service.strategy.SortStrategyType;

import java.util.List;

public interface MovieService {
    List<Movie> getAllMovies();

    Movie addMovie(MovieDTO dto) throws Exception;
    Movie editMovie(String id, MovieDTO dto) throws Exception;
    void deleteMovie(String id);
    void updateMovie(Movie movie);

    void setSortStrategy(SortStrategyType strategyType);
    void setSortDirection(SortDirection sortDirection);

    void setFilterCategory(Category category);
    void setFilterStatus(Status status);
    void setFilterRating(Integer rating);

    void resetFiltersAndSort();

    void setSearchQuery(String query);

    void addMovieObject(Movie movieToDelete);
}