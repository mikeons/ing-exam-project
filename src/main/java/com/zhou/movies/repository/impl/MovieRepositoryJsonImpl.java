package com.zhou.movies.repository.impl;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zhou.movies.pojo.Movie;
import com.zhou.movies.repository.MovieRepository;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A Repository implementation that persists movie data to a JSON file.
 * This class handles all the logic for file I/O and JSON serialization/deserialization.
 */
public class MovieRepositoryJsonImpl implements MovieRepository {

    private final String filePath;
    private final Gson gson;

    public MovieRepositoryJsonImpl(String filePath) {
        this.filePath = filePath;
        // Use GsonBuilder to create a "pretty printing" Gson instance.
        // This makes the resulting movies.json file human-readable.
        this.gson = new GsonBuilder().setPrettyPrinting().create();

        // Ensure the JSON file exists on startup.
        initFile();
    }

    /**
     * Initializes the JSON file if it does not already exist.
     * It writes an empty JSON array "[]" to the new file
     * to prevent parse errors when "findAll" is called on an empty file.
     */
    private void initFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("[]");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Movie> findAll() {
        try (FileReader reader = new FileReader(filePath)) {

            // This is the crucial part for deserializing generic types like List<T>.
            // We must provide Gson with the *exact* type (List<Movie>)
            // using a TypeToken, otherwise Gson only knows how to make a List of unknown objects.
            Type listType = new TypeToken<List<Movie>>(){}.getType();

            List<Movie> movies = gson.fromJson(reader, listType);

            // If the file is empty or corrupted, gson.fromJson might return null.
            // We must handle this case to avoid NullPointerExceptions.
            if (movies == null) {
                return new ArrayList<>();
            }

            return movies;

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void saveAll(List<Movie> moviesListCache) {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(moviesListCache, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}