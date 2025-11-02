package com.zhou.movies.pojo;

import java.util.Objects;

public class Movie {
    private String title;
    private String director;
    private int year;
    private int rating;
    private Category category;
    private Status status;

    public Movie(String title, String director, int year, Category category, Status status, int rating) {
        this.title = title;
        this.director = director;
        this.year = year;
        this.rating = rating;
        this.category = category;
        this.status = status;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return year == movie.year && rating == movie.rating && Objects.equals(title, movie.title) && Objects.equals(director, movie.director) && category == movie.category && status == movie.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, director, year, rating, category, status);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", director='" + director + '\'' +
                ", year=" + year +
                ", rating=" + rating +
                ", category=" + category +
                ", status=" + status +
                '}';
    }
}

