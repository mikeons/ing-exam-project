package com.zhou.movies;

import com.zhou.movies.controller.MovieController;
import com.zhou.movies.repository.MovieRepository;
import com.zhou.movies.repository.impl.MovieRepositoryJsonImpl;
import com.zhou.movies.service.impl.MovieServiceImpl;
import com.zhou.movies.view.MovieView;

import javax.swing.*;

public class Main {
    private static final String JSON_FILE_PATH = "movies.json"; // 统一定义路径
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            // 1. Create all concrete components
            MovieRepository movieRepository = new MovieRepositoryJsonImpl(JSON_FILE_PATH);
            MovieServiceImpl serviceImpl = new MovieServiceImpl(movieRepository);
            MovieView view = new MovieView();
            MovieController controller = new MovieController(serviceImpl);

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
