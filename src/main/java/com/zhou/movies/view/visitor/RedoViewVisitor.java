package com.zhou.movies.view.visitor;

import com.zhou.movies.command.CommandVisitor;
import com.zhou.movies.command.impl.AddMovieCommand;
import com.zhou.movies.command.impl.DeleteMovieCommand;
import com.zhou.movies.command.impl.EditMovieCommand;
import com.zhou.movies.view.MovieView;

// Concrete visitor for Redo
public class RedoViewVisitor implements CommandVisitor {

    private final MovieView view;

    public RedoViewVisitor(MovieView view) {
        this.view = view;
    }

    @Override
    public void visit(AddMovieCommand cmd) {
        // Redo Add: restore movie and select it
        view.selectRowById(cmd.getCreatedMovie().getId());
    }

    @Override
    public void visit(EditMovieCommand cmd) {
        // Redo Edit: apply updated movie and select it
        view.selectRowById(cmd.getUpdatedMovie().getId());
    }

    @Override
    public void visit(DeleteMovieCommand cmd) {
        // Redo Delete: no selection needed
    }
}
