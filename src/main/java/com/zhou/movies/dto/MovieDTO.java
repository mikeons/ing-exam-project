package com.zhou.movies.dto;

import com.zhou.movies.pojo.Category;
import com.zhou.movies.pojo.Status;

/**
 * A Data Transfer Object (DTO) that carries raw, unvalidated movie data
 * from the View layer to the Controller layer for processing.
 */
public class MovieDTO {
    public final String title;
    public final String director;
    public final String yearStr;
    public final Category category;
    public final Status status;
    public final Integer rating;

    public MovieDTO(String title, String director, String yearStr, Category category, Status status, Integer rating) {
        this.title = title;
        this.director = director;
        this.yearStr = yearStr;
        this.category = category;
        this.status = status;
        this.rating = rating;
    }
}