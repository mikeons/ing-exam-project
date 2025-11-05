package com.zhou.movies.service.impl;

import com.zhou.movies.pojo.Movie;
import com.zhou.movies.service.strategy.SortDirection;
import com.zhou.movies.service.strategy.SortStrategyType;
import com.zhou.movies.repository.MovieRepository;
import com.zhou.movies.service.MovieService;
import com.zhou.movies.service.Observer;
import com.zhou.movies.service.strategy.SortingStrategy;
import com.zhou.movies.service.Subject;
import com.zhou.movies.service.strategy.impl.SortByRatingStrategy;
import com.zhou.movies.service.strategy.impl.SortByTitleStrategy;
import com.zhou.movies.service.strategy.impl.SortByYearStrategy;

import java.util.ArrayList;
import java.util.List;

public class MovieServiceImpl implements MovieService, Subject {
    private final MovieRepository movieRepository;
    private final List<Observer> observers;

    private final List<Movie> moviesListCache;
    private SortingStrategy currentSortingStrategy;
    private SortDirection currentSortDirection;

    public MovieServiceImpl(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
        this.observers = new ArrayList<>();

        this.moviesListCache = movieRepository.findAll();

        this.currentSortingStrategy = new SortByTitleStrategy();
        this.currentSortDirection = SortDirection.ASCENDING;
    }

    @Override
    public List<Movie> getAllMovies(){
        List<Movie> listToDisplay = new ArrayList<>(this.moviesListCache);

        if (currentSortingStrategy != null) {
            currentSortingStrategy.sort(listToDisplay, currentSortDirection);
        }

        return listToDisplay;
    }

    @Override
    public void addMovie(Movie movie){
        movieRepository.save(movie);
        moviesListCache.add(movie);

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

    @Override
    public void setSortStrategy(SortStrategyType strategyType){
        if (strategyType == null)
            strategyType = SortStrategyType.TITLE;

        switch (strategyType){
            case YEAR:
                currentSortingStrategy = new SortByYearStrategy();
                break;
            case RATING:
                currentSortingStrategy = new SortByRatingStrategy();
                break;
            default:
                currentSortingStrategy = new SortByTitleStrategy();
                break;
        }

        notifyObservers();
    }

    @Override
    public void setSortDirection(SortDirection sortDirection){
        currentSortDirection = sortDirection;
        notifyObservers();
    }
}
