package com.zhou.movies.repository.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zhou.movies.pojo.Movie;
import com.zhou.movies.repository.MovieRepository;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON file-based repository for movies.
 * Handles all file I/O and JSON (de)serialization using Gson.
 */
public class MovieRepositoryJsonImpl implements MovieRepository {

    private final Path filePath;
    private final Gson gson;
    private static final Type MOVIE_LIST_TYPE = new TypeToken<List<Movie>>(){}.getType();

    public MovieRepositoryJsonImpl(String filePath) {
        this.filePath = Paths.get(filePath);
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        initFile();
    }

    /**
     * Ensure the JSON file exists with an empty array if newly created.
     */
    private void initFile() {
        try {
            // Ensure parent folder exists
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }
            // Create file with empty JSON array if it doesn't exist
            if (Files.notExists(filePath)) {
                Files.writeString(filePath, "[]", StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize JSON file: " + filePath, e);
        }
    }

    @Override
    public List<Movie> findAll() {
        try {
            String json = Files.readString(filePath, StandardCharsets.UTF_8);
            List<Movie> movies = gson.fromJson(json, MOVIE_LIST_TYPE);
            return movies != null ? movies : new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read movie data from JSON file: " + filePath, e);
        }
    }

    @Override
    public void saveAll(List<Movie> moviesListCache) {
        try {
            String json = gson.toJson(moviesListCache);
            Files.writeString(filePath, json, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save movie data to JSON file: " + filePath, e);
        }
    }
}
