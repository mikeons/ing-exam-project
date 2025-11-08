package com.zhou.movies.command.impl;

import com.zhou.movies.command.Command;
import com.zhou.movies.dto.MovieDTO;
import com.zhou.movies.pojo.Movie;
import com.zhou.movies.service.MovieService;

public class EditMovieCommand implements Command {

    private final MovieService movieService;
    private final MovieDTO movieDTO;

    private final Movie originalMovie;
    private Movie updatedMovie;

    public EditMovieCommand(MovieService movieService, Movie originalMovie, MovieDTO movieDTO) {
        this.movieService = movieService;
        this.originalMovie = originalMovie;
        this.movieDTO = movieDTO;
    }

    @Override
    public void execute() throws Exception {
        this.updatedMovie = movieService.editMovie(originalMovie.getId(), movieDTO);
    }

    @Override
    public void undo() throws Exception {
        movieService.updateMovie(this.originalMovie);
    }

    public Movie getUpdatedMovie() {
        return updatedMovie;
    }
}