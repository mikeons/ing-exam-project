package com.zhou.movies.command;

public interface Command {
    void execute() throws Exception;
    void undo() throws Exception;

    void accept(CommandVisitor visitor);
}
