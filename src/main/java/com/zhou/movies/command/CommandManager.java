package com.zhou.movies.command;

import java.util.Stack;

/**
 * Manages execution and history of commands.
 *
 * Responsibility:
 * Executes Command objects and maintains undo/redo stacks,
 * serving as the central component of the Command pattern.
 */
public class CommandManager {

    private final Stack<Command> undoStack;
    private final Stack<Command> redoStack;

    public CommandManager(){
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public void execute(Command command) throws Exception{
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    public Command undo() throws Exception{
        if (!undoStack.isEmpty()){
            Command commandToUndo = undoStack.pop();
            commandToUndo.undo();
            redoStack.push(commandToUndo);
            return commandToUndo;
        }

        return null;
    }

    public Command redo() throws Exception{
        if (!redoStack.isEmpty()){
            Command commandToRedo = redoStack.pop();
            commandToRedo.execute();
            undoStack.push(commandToRedo);
            return commandToRedo;
        }

        return null;
    }
}
