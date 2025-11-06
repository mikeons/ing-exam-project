package com.zhou.movies.repository;

import com.zhou.movies.pojo.Movie;

import java.util.List;

public interface MovieRepository {
    List<Movie> findAll();
    void saveAll(List<Movie> moviesListCache);
}
