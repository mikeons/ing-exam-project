package com.zhou.movies.command.impl;

import com.zhou.movies.command.Command;
import com.zhou.movies.command.CommandVisitor;
import com.zhou.movies.dto.MovieDTO;
import com.zhou.movies.pojo.Movie;
import com.zhou.movies.service.MovieService;

public class AddMovieCommand implements Command {

    private final MovieService movieService;
    private final MovieDTO movieDTO;

    private Movie createdMovie;

    public AddMovieCommand(MovieService movieService, MovieDTO movieDTO) {
        this.movieService = movieService;
        this.movieDTO = movieDTO;
    }

    @Override
    public void execute() throws Exception {
        this.createdMovie = movieService.addMovie(movieDTO);
    }

    @Override
    public void undo() throws Exception{
        if (this.createdMovie != null) {
            movieService.deleteMovie(this.createdMovie.getId());
        }else {
            throw new Exception("Undo failed: Movie was not created.");
        }
    }

    @Override
    public void accept(CommandVisitor visitor) {
        visitor.visit(this);
    }

    public Movie getCreatedMovie() {
        return createdMovie;
    }
}