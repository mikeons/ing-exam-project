package com.zhou.movies.view.visitor;

import com.zhou.movies.command.CommandVisitor;
import com.zhou.movies.command.impl.AddMovieCommand;
import com.zhou.movies.command.impl.DeleteMovieCommand;
import com.zhou.movies.command.impl.EditMovieCommand;
import com.zhou.movies.view.MovieView;

// Concrete visitor for Undo
public class UndoViewVisitor implements CommandVisitor {

    private final MovieView view;

    public UndoViewVisitor(MovieView view) {
        this.view = view;
    }

    @Override
    public void visit(AddMovieCommand cmd) {
        // Undo Add: no selection needed
    }

    @Override
    public void visit(EditMovieCommand cmd) {
        // Undo Edit: restore original movie and select it
        view.selectRowById(cmd.getOriginalMovie().getId());
    }

    @Override
    public void visit(DeleteMovieCommand cmd) {
        // Undo Delete: restore deleted movie and select it
        view.selectRowById(cmd.getMovieToDelete().getId());
    }
}

