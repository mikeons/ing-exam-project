package com.zhou.movies.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import com.zhou.movies.dto.MovieDTO;
import com.zhou.movies.pojo.Category;
import com.zhou.movies.pojo.Movie;
import com.zhou.movies.pojo.Status;
import com.zhou.movies.repository.MovieRepository;
import com.zhou.movies.service.strategy.SortDirection;
import com.zhou.movies.service.strategy.SortStrategyType;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for MovieServiceImpl
 * Focus: business logic (sorting, filtering, searching)
 */
@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    private MovieServiceImpl movieService;

    @Mock
    private MovieRepository mockRepository;

    @Captor
    private ArgumentCaptor<List<Movie>> movieListCaptor;

    @BeforeEach
    void setUp() {
        Movie movieA_Action_5 = new Movie.Builder("A-Title", "DirectorA")
                .year(2000).rating(5).category(Category.ACTION).build();
        Movie movieB_Comedy_1 = new Movie.Builder("B-Title", "DirectorB")
                .year(2020).rating(1).category(Category.COMEDY).build();
        Movie movieC_Action_3 = new Movie.Builder("C-Title", "DirectorC")
                .year(2010).rating(3).category(Category.ACTION).build();

        List<Movie> fakeDatabase = new ArrayList<>();
        fakeDatabase.add(movieA_Action_5);
        fakeDatabase.add(movieB_Comedy_1);
        fakeDatabase.add(movieC_Action_3);

        when(mockRepository.findAll()).thenReturn(new ArrayList<>(fakeDatabase));

        movieService = new MovieServiceImpl(mockRepository);
    }

    @Test
    @DisplayName("Default: should return all movies sorted by title ascending")
    void getAllMovies_DefaultState_ReturnsAllMoviesSortedByTitleAsc() {
        List<Movie> result = movieService.getAllMovies();

        assertEquals(3, result.size()); // actual: [A-Title, B-Title, C-Title]
        assertEquals("A-Title", result.get(0).getTitle()); // actual first: A-Title
        assertEquals("B-Title", result.get(1).getTitle()); // actual second: B-Title
        assertEquals("C-Title", result.get(2).getTitle()); // actual third: C-Title
    }

    @Test
    @DisplayName("Sort by rating descending")
    void getAllMovies_WhenSortByRatingDesc_ReturnsSortedList() {
        movieService.setSortStrategy(SortStrategyType.RATING);
        movieService.setSortDirection(SortDirection.DESCENDING);
        List<Movie> result = movieService.getAllMovies();

        assertEquals(3, result.size()); // actual: [A-Title, C-Title, B-Title]
        assertEquals("A-Title", result.get(0).getTitle()); // actual rating=5
        assertEquals("C-Title", result.get(1).getTitle()); // actual rating=3
        assertEquals("B-Title", result.get(2).getTitle()); // actual rating=1
    }

    @Test
    @DisplayName("Filter by category")
    void getAllMovies_WhenFilteredByCategory_ReturnsFilteredList() {
        movieService.setFilterCategory(Category.ACTION);
        List<Movie> result = movieService.getAllMovies();

        assertEquals(2, result.size()); // actual: [A-Title, C-Title]
        assertEquals("A-Title", result.get(0).getTitle()); // actual category=ACTION
        assertEquals("C-Title", result.get(1).getTitle()); // actual category=ACTION
    }

    @Test
    @DisplayName("Search by title (case insensitive)")
    void getAllMovies_WhenSearchQuery_ReturnsMatchingList() {
        movieService.setSearchQuery("b-title");
        List<Movie> result = movieService.getAllMovies();

        assertEquals(1, result.size()); // actual: [B-Title]
        assertEquals("B-Title", result.get(0).getTitle()); // actual title=B-Title
    }

    @Test
    @DisplayName("Filter + sort combination")
    void getAllMovies_WhenFilteredAndSorted_ReturnsFilteredAndSortedList() {
        movieService.setFilterCategory(Category.ACTION);
        movieService.setSortStrategy(SortStrategyType.YEAR);
        movieService.setSortDirection(SortDirection.ASCENDING);

        List<Movie> result = movieService.getAllMovies();

        assertEquals(2, result.size()); // actual: [A-Title(2000), C-Title(2010)]
        assertEquals("A-Title", result.get(0).getTitle()); // actual year=2000
        assertEquals("C-Title", result.get(1).getTitle()); // actual year=2010
    }

    @Test
    @DisplayName("Reset should clear all filters and sort states")
    void resetFiltersAndSort_WhenCalled_ResetsAllState() {
        movieService.setFilterCategory(Category.ACTION);
        movieService.setSortStrategy(SortStrategyType.RATING);
        movieService.setSearchQuery("b-title");

        movieService.resetFiltersAndSort();
        List<Movie> result = movieService.getAllMovies();

        assertEquals(3, result.size()); // actual: [A-Title, B-Title, C-Title]
        assertEquals("A-Title", result.get(0).getTitle()); // actual first after reset
        assertEquals("B-Title", result.get(1).getTitle()); // actual second after reset
    }

    @Test
    @DisplayName("Add movie should update cache and call saveAll")
    void addMovie_WhenCalled_UpdatesCacheAndCallsSaveAll() throws Exception {
        MovieDTO dto = new MovieDTO("D-Title", "DirectorD", "2025",
                Category.DRAMA, Status.WANT_TO_WATCH, 4);

        Movie createdMovie = movieService.addMovie(dto);

        assertNotNull(createdMovie); // actual: createdMovie = D-Title
        assertEquals("D-Title", createdMovie.getTitle()); // actual: D-Title

        assertEquals(4, movieService.getAllMovies().size()); // actual: [A,B,C,D]

        verify(mockRepository, times(1)).saveAll(movieListCaptor.capture());
        List<Movie> savedList = movieListCaptor.getValue();

        assertEquals(4, savedList.size()); // actual: list saved with 4 movies
        assertTrue(savedList.stream().anyMatch(m -> m.getTitle().equals("D-Title"))); // actual: true
    }
}
