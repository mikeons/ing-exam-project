package com.zhou.movies.repository;

import com.zhou.movies.pojo.Movie;

import java.util.List;

public interface MovieRepository {
    List<Movie> findAll();
    void save(Movie movie);
}
