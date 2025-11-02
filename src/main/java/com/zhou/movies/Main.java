package com.zhou.movies;

import com.zhou.movies.controller.MovieController;
import com.zhou.movies.service.MovieService;
import com.zhou.movies.service.impl.MovieServiceImpl;
import com.zhou.movies.view.MovieView;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            MovieService movieService = new MovieServiceImpl();
            MovieView movieView = new MovieView();

            MovieController movieController = new MovieController(movieService, movieView);

            movieView.setController(movieController);

            movieController.loadInitialData();

            movieView.setVisible(true);
        });
    }
}
