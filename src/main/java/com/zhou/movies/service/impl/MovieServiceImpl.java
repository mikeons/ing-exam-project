package com.zhou.movies.service.impl;

import com.zhou.movies.pojo.Movie;
import com.zhou.movies.repository.MovieRepository;
import com.zhou.movies.service.MovieService;
import com.zhou.movies.service.Observer;
import com.zhou.movies.service.Subject;

import java.util.ArrayList;
import java.util.List;

public class MovieServiceImpl implements MovieService, Subject {
    private final MovieRepository movieRepository;
    private final List<Observer> observers;

    public MovieServiceImpl(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
        observers = new ArrayList<>();
    }

    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }

    public void addMovie(Movie movie){
        movieRepository.save(movie);
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
