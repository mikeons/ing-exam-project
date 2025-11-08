package com.zhou.movies;

import com.zhou.movies.command.CommandManager;
import com.zhou.movies.controller.MovieController;
import com.zhou.movies.repository.MovieRepository;
import com.zhou.movies.repository.impl.MovieRepositoryJsonImpl;
import com.zhou.movies.service.impl.MovieServiceImpl;
import com.zhou.movies.view.MovieView;

import javax.swing.*;

/**
 * Acts as a **Facade** for the application's startup process.
 * It simplifies the `Main` class by encapsulating all component creation,
 * dependency injection, and UI launch logic in one place.
 */
public class Application {
    private final String JSON_FILE_PATH = "movies.json";

    public void start(){
        SwingUtilities.invokeLater(() -> {
            // 1. Create all concrete components
            MovieRepository movieRepository = new MovieRepositoryJsonImpl(JSON_FILE_PATH);
            MovieServiceImpl serviceImpl = new MovieServiceImpl(movieRepository);
            MovieView view = new MovieView();

            CommandManager commandManager = new CommandManager();
            MovieController controller = new MovieController(serviceImpl, commandManager);

            // 2. Wire dependencies
            view.setController(controller);

            // 3. Set up observer pattern
            serviceImpl.addObserver(view);

            // 4. Load initial data
            view.refreshTable(controller.getAllMovies());

            // 5. Launch UI
            view.setVisible(true);
        });
    }
}
