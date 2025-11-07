package com.zhou.movies.service.impl;

import com.zhou.movies.dto.MovieDTO;
import com.zhou.movies.pojo.Category;
import com.zhou.movies.pojo.Movie;
import com.zhou.movies.pojo.Status;
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
import java.util.stream.Collectors;

public class MovieServiceImpl implements MovieService, Subject {
    private final MovieRepository movieRepository;
    private final List<Observer> observers;

    private final List<Movie> moviesListCache;
    private SortingStrategy currentSortingStrategy;
    private SortDirection currentSortDirection;

    private Category currentFilterCategory = null;
    private Status currentFilterStatus = null;
    private Integer currentFilterRating = null;

    private String currentSearchQuery = null;

    public MovieServiceImpl(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
        this.observers = new ArrayList<>();

        this.moviesListCache = movieRepository.findAll();

        this.currentSortingStrategy = new SortByTitleStrategy();
        this.currentSortDirection = SortDirection.ASCENDING;
    }

    @Override
    public List<Movie> getAllMovies(){
        // Apply search params if exist
        List<Movie> searchResults;
        if (currentSearchQuery == null) {
            searchResults = new ArrayList<>(moviesListCache);
        } else {
            String queryLower = currentSearchQuery.toLowerCase();
            searchResults = moviesListCache.stream()
                    .filter(movie ->
                            movie.getTitle().toLowerCase().contains(queryLower) ||
                                    movie.getDirector().toLowerCase().contains(queryLower)
                    )
                    .collect(Collectors.toList());
        }

        // Filtering result of searched movies with selected categories
        List<Movie> filteredMovies = searchResults.stream()
                .filter(movie -> (currentFilterCategory == null || movie.getCategory().equals(currentFilterCategory)))
                .filter(movie -> (currentFilterStatus == null || movie.getStatus().equals(currentFilterStatus)))
                .filter(movie -> (currentFilterRating == null || movie.getRating() == currentFilterRating))
                .collect(Collectors.toList());

        // Sorting filtered Movies with the selected strategy
        if (currentSortingStrategy != null) {
            currentSortingStrategy.sort(filteredMovies, currentSortDirection);
        }

        return filteredMovies;
    }

    @Override
    public void deleteMovie(String id) {
        // Remove the movie from cache using its unique ID
        moviesListCache.removeIf(movie -> movie.getId().equals(id));

        // Update the JSON file (by overwriting with the new cache)
        movieRepository.saveAll(moviesListCache);

        // Notify all observers (View) to refresh
        notifyObservers();
    }

    @Override
    public void addMovie(MovieDTO dto) throws Exception {
        int year = Integer.parseInt(dto.yearStr);

        // Build a new domain object (Movie)
        Movie movie = new Movie.Builder(dto.title, dto.director)
                .year(year)
                .category(dto.category)
                .status(dto.status)
                .rating(dto.rating)
                .build();

        // Save to cache and persist changes
        moviesListCache.add(movie);
        movieRepository.saveAll(moviesListCache);

        // Notify observers to refresh the view
        notifyObservers();
    }

    @Override
    public void editMovie(String id, MovieDTO dto) throws Exception {
        // Find the original movie by ID or throw if not found
        Movie originalMovie = moviesListCache.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new Exception("Movie not found with id: " + id));

        int year = Integer.parseInt(dto.yearStr);

        // Create an updated Movie object
        Movie updatedMovie = originalMovie.toBuilder()
                .title(dto.title)
                .director(dto.director)
                .year(year)
                .category(dto.category)
                .status(dto.status)
                .rating(dto.rating)
                .build();

        // Replace in cache, persist, and notify observers
        int index = moviesListCache.indexOf(originalMovie);
        moviesListCache.set(index, updatedMovie);
        movieRepository.saveAll(moviesListCache);
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

    @Override
    public void setFilterCategory(Category category) {
        this.currentFilterCategory = category;
        notifyObservers();
    }

    @Override
    public void setFilterStatus(Status status) {
        this.currentFilterStatus = status;
        notifyObservers();
    }

    @Override
    public void setFilterRating(Integer rating) {
        this.currentFilterRating = rating;
        notifyObservers();
    }

    @Override
    public void resetFiltersAndSort() {
        this.currentFilterCategory = null;
        this.currentFilterStatus = null;
        this.currentFilterRating = null;

        this.currentSearchQuery = null;

        this.currentSortingStrategy = new SortByTitleStrategy();
        this.currentSortDirection = SortDirection.ASCENDING;

        notifyObservers();
    }

    @Override
    public void setSearchQuery(String query) {
        this.currentSearchQuery = (query == null || query.trim().isEmpty()) ? null : query.trim();
        notifyObservers();
    }
}
