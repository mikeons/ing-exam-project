package com.zhou.movies.command.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import com.zhou.movies.dto.MovieDTO;
import com.zhou.movies.pojo.Movie;
import com.zhou.movies.service.MovieService;

/**
 * Unit tests for AddMovieCommand
 * Goal: verify execute/undo logic with mocked MovieService
 */
@ExtendWith(MockitoExtension.class)
class AddMovieCommandTest {

    @Mock
    private MovieService mockService;

    private MovieDTO testDTO;
    private Movie fakeMovieMemento;
    private String randomId;
    private AddMovieCommand addMovieCommand;

    @BeforeEach
    void setUp() {
        testDTO = new MovieDTO("Test Title", "Test Director", "2025",
                null, null, 3);

        // Fake movie returned by mocked service
        fakeMovieMemento = new Movie.Builder("Test Title", "Test Director")
                .year(2025)
                .build();

        randomId = fakeMovieMemento.getId();
        addMovieCommand = new AddMovieCommand(mockService, testDTO);
    }

    @Test
    @DisplayName("Execute should call service.addMovie and store memento")
    void execute_WhenCalled_ShouldCallServiceAndStoreMemento() throws Exception {
        when(mockService.addMovie(testDTO)).thenReturn(fakeMovieMemento);

        addMovieCommand.execute();

        verify(mockService, times(1)).addMovie(testDTO); // ensure service called once
        assertEquals(fakeMovieMemento, addMovieCommand.getCreatedMovie()); // memento stored
        assertEquals(randomId, addMovieCommand.getCreatedMovie().getId()); // correct ID
    }

    @Test
    @DisplayName("Undo after execute should call service.deleteMovie with memento ID")
    void undo_AfterExecute_ShouldCallServiceDeleteWithMementoId() throws Exception {
        when(mockService.addMovie(testDTO)).thenReturn(fakeMovieMemento);
        addMovieCommand.execute();

        addMovieCommand.undo();

        verify(mockService, times(1)).deleteMovie(randomId); // correct deletion
    }

    @Test
    @DisplayName("Undo before execute should throw exception")
    void undo_BeforeExecute_ShouldThrowException() {
        Exception exception = assertThrows(Exception.class, () -> {
            addMovieCommand.undo();
        });

        assertEquals("Undo failed: Movie was not created.", exception.getMessage());
    }

    @Test
    @DisplayName("Execute should propagate service exception correctly")
    void execute_WhenServiceFails_ShouldPropagateException() throws Exception {
        when(mockService.addMovie(testDTO)).thenThrow(new Exception("Database connection failed"));

        Exception exception = assertThrows(Exception.class, () -> {
            addMovieCommand.execute();
        });

        assertNull(addMovieCommand.getCreatedMovie()); // no memento stored
        assertEquals("Database connection failed", exception.getMessage()); // exception propagated
    }
}
