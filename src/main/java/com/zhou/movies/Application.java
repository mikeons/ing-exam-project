package com.zhou.movies;

import com.zhou.movies.command.CommandManager;
import com.zhou.movies.controller.MovieController;
import com.zhou.movies.repository.MovieRepository;
import com.zhou.movies.repository.impl.MovieRepositoryJsonImpl;
import com.zhou.movies.service.impl.MovieServiceImpl;
import com.zhou.movies.view.MovieView;
import com.zhou.movies.view.event.ViewListenerManager;

import javax.swing.*;

/**
 * Acts as a **Facade** for the application's startup process.
 * It simplifies the `Main` class by encapsulating all component creation,
 * dependency injection, and UI launch logic in one place.
 */
public class Application {
    private final String JSON_FILE_PATH = "movies.json";

    public void start() {
        SwingUtilities.invokeLater(() -> {
            // Create components
            MovieRepository movieRepository = new MovieRepositoryJsonImpl(JSON_FILE_PATH);
            MovieServiceImpl serviceImpl = new MovieServiceImpl(movieRepository);
            MovieView view = new MovieView();
            CommandManager commandManager = new CommandManager();
            MovieController controller = new MovieController(serviceImpl, commandManager);

            // Wire dependencies
            view.setController(controller);

            // Register view as observer
            serviceImpl.addObserver(view);

            // Bind all UI listeners
            ViewListenerManager listenerManager = new ViewListenerManager(view, controller);
            listenerManager.bindListeners();

            // Load initial data into table
            view.refreshTable(controller.getAllMovies());

            // Launch UI
            view.setVisible(true);
        });
    }
}
