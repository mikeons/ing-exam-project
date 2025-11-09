package com.zhou.movies.command;

import com.zhou.movies.command.impl.AddMovieCommand;
import com.zhou.movies.command.impl.EditMovieCommand;
import com.zhou.movies.command.impl.DeleteMovieCommand;

public interface CommandVisitor {
    void visit(AddMovieCommand cmd);
    void visit(EditMovieCommand cmd);
    void visit(DeleteMovieCommand cmd);
}
