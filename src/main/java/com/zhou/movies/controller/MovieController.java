package com.zhou.movies.controller;

import com.zhou.movies.dto.MovieDTO;
import com.zhou.movies.pojo.Category;
import com.zhou.movies.pojo.Movie;
import com.zhou.movies.pojo.Status;
import com.zhou.movies.service.MovieService;
import com.zhou.movies.service.strategy.SortDirection;
import com.zhou.movies.service.strategy.SortStrategyType;

import java.util.List;

public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    public boolean addMovieRequest(MovieDTO dto) {
        try {
            validateFields(dto);
            movieService.addMovie(dto);
            return true;
        } catch (Exception e) {
            System.out.println("Add Movie Failed: " + e.getMessage());
            return false;
        }
    }

    public boolean editMovieRequest(String id, MovieDTO dto) {
        try {
            validateFields(dto);
            movieService.editMovie(id, dto);
            return true;
        } catch (Exception e) {
            System.out.println("Update Failed: " + e.getMessage());
            return false;
        }
    }

    private void validateFields(MovieDTO dto) throws Exception {
        if (dto.title.isEmpty() || dto.director.isEmpty() || dto.yearStr.isEmpty())
            throw new Exception("Title, Director or Year cannot be empty!");

        try {
            Integer.parseInt(dto.yearStr);
        } catch (NumberFormatException e) {
            throw new Exception("Year must be a valid number!");
        }
    }

    public List<Movie> getAllMovies(){
        return movieService.getAllMovies();
    }

    public void deleteMovie(String id) {
        movieService.deleteMovie(id);
    }

    public void changeSortStrategy(SortStrategyType strategyType){
        movieService.setSortStrategy(strategyType);
    }

    public void changeSortDirection(SortDirection direction){
        movieService.setSortDirection(direction);
    }

    public void setFilterCategory(Category category) {
        movieService.setFilterCategory(category);
    }

    public void setFilterStatus(Status status) {
        movieService.setFilterStatus(status);
    }

    public void setFilterRating(Integer rating) {
        movieService.setFilterRating(rating);
    }

    public void resetFiltersAndSort() {
        movieService.resetFiltersAndSort();
    }

}