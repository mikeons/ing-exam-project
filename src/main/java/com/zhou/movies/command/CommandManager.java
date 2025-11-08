package com.zhou.movies.command;

import java.util.Stack;

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

    public void undo() throws Exception{
        if (!undoStack.isEmpty()){
            Command commandToUndo = undoStack.pop();
            commandToUndo.undo();
            redoStack.push(commandToUndo);
        }
    }

    public void redo() throws Exception{
        if (!redoStack.isEmpty()){
            Command commandToRedo = redoStack.pop();
            commandToRedo.execute();
            undoStack.push(commandToRedo);
        }
    }
}
