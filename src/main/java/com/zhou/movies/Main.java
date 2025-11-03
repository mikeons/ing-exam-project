package com.zhou.movies;

import com.zhou.movies.controller.MovieController;
import com.zhou.movies.service.impl.MovieServiceImpl;
import com.zhou.movies.view.MovieView;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            // 1. Create all concrete components
            MovieServiceImpl serviceImpl = new MovieServiceImpl();
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
