package com.zhou.movies.pojo;

import java.util.Objects;
import java.util.UUID;

public class Movie {
    private final String id;
    private final String title;
    private final String director;
    private final int year;
    private final int rating;
    private final Category category;
    private final Status status;

    public Movie(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.director = builder.director;
        this.year = builder.year;
        this.category = builder.category;
        this.status = builder.status;
        this.rating = builder.rating;
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public static class Builder{
        private final String id;
        private String title;
        private String director;
        private int year;
        private int rating;
        private Category category;
        private Status status;

        public Builder(String title, String director){
            this.id = UUID.randomUUID().toString();
            this.title = title;
            this.director = director;
        }

        private Builder(Movie movie) {
            this.id = movie.id;
            this.title = movie.title;
            this.director = movie.director;
            this.year = movie.year;
            this.category = movie.category;
            this.status = movie.status;
            this.rating = movie.rating;
        }

        public Builder title(String title){
            this.title = title;
            return this;
        }

        public Builder director(String director){
            this.director = director;
            return this;
        }

        public Builder year(int year) { this.year = year; return this; }
        public Builder category(Category c) { this.category = c; return this; }
        public Builder status(Status s) { this.status = s; return this; }
        public Builder rating(int r) { this.rating = r; return this; }

        public Movie build() {
            return new Movie(this);
        }
    }

    public String getId(){
        return id;
    }

    public int getRating() {
        return rating;
    }

    public String getTitle() {
        return title;
    }

    public String getDirector() {
        return director;
    }

    public int getYear() {
        return year;
    }

    public Category getCategory() {
        return category;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(id, movie.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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

