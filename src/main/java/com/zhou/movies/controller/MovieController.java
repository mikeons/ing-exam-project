package com.zhou.movies.controller;

import com.zhou.movies.command.CommandManager;
import com.zhou.movies.command.impl.*;
import com.zhou.movies.command.Command;
import com.zhou.movies.dto.MovieDTO;
import com.zhou.movies.pojo.Category;
import com.zhou.movies.pojo.Movie;
import com.zhou.movies.pojo.Status;
import com.zhou.movies.service.MovieService;
import com.zhou.movies.service.strategy.SortDirection;
import com.zhou.movies.service.strategy.SortStrategyType;
import java.util.List;

/**
 * Mediator between the UI and the service layer.
 *
 * Responsibility:
 * Processes user actions related to movies (add, edit, delete, sort, filter, search),
 * handles validation, and coordinates undo/redo operations via the Command pattern.
 */
public class MovieController {

    private final MovieService movieService;
    private final CommandManager commandManager;

    public MovieController(MovieService movieService, CommandManager commandManager) {
        this.movieService = movieService;
        this.commandManager = commandManager;
    }

    public Movie addMovieRequest(MovieDTO dto) {
        try {
            validateFields(dto);

            AddMovieCommand addCmd = new AddMovieCommand(movieService, dto);
            commandManager.execute(addCmd);

            return addCmd.getCreatedMovie();
        } catch (Exception e) {
            System.out.println("Add Movie Failed: " + e.getMessage());
            return null;
        }
    }

    public Movie editMovieRequest(Movie movieToEdit, MovieDTO dto) {
        try {
            validateFields(dto);

            EditMovieCommand editCmd = new EditMovieCommand(movieService, movieToEdit, dto);
            commandManager.execute(editCmd);

            return editCmd.getUpdatedMovie();
        } catch (Exception e) {
            System.out.println("Update Failed: " + e.getMessage());
            return null;
        }
    }

    public void deleteMovieRequest(Movie movieToDelete) throws Exception {
        Command deleteCmd = new DeleteMovieCommand(movieService, movieToDelete);
        commandManager.execute(deleteCmd);
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

    public void searchMovies(String query) {
        movieService.setSearchQuery(query);
    }

    public Command undoRequest() throws Exception{
        return commandManager.undo();
    }

    public Command redoRequest() throws Exception{
        return commandManager.redo();
    }
}