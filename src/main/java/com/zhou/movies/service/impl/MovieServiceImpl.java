package com.zhou.movies.service.impl;

import com.zhou.movies.pojo.Category;
import com.zhou.movies.pojo.Movie;
import com.zhou.movies.pojo.Status;
import com.zhou.movies.service.MovieService;
import com.zhou.movies.service.Observer;
import com.zhou.movies.service.Subject;

import java.util.ArrayList;
import java.util.List;

public class MovieServiceImpl implements MovieService, Subject {
    private final List<Movie> movies;
    private final List<Observer> observers;

    public MovieServiceImpl(){
        movies = new ArrayList<>();
        observers = new ArrayList<>();

        movies.add(new Movie("Bad man", "William X.", 2020, Category.ACTION, Status.WATCHED, 1));
        movies.add(new Movie("Nezha2", "Boy cow", 2022,  Category.COMEDY, Status.WANT_TO_WATCH, 5));
    }

    public List<Movie> getAllMovies(){
        return movies;
    }

    public void addMovie(Movie movie){
        movies.add(movie);
        notifyObservers();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers)
            observer.update();
    }
}
