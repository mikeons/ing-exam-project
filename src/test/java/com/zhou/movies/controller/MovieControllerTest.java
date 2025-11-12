package com.zhou.movies.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zhou.movies.command.Command;
import com.zhou.movies.command.CommandManager;
import com.zhou.movies.command.impl.AddMovieCommand;
import com.zhou.movies.command.impl.DeleteMovieCommand;
import com.zhou.movies.dto.MovieDTO;
import com.zhou.movies.pojo.Movie;
import com.zhou.movies.service.MovieService;
import com.zhou.movies.service.strategy.SortStrategyType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for MovieController
 * Goal: verify Controller properly routes, wraps, shields, and propagates
 * Dependencies: mocked MovieService and CommandManager
 */
@ExtendWith(MockitoExtension.class)
class MovieControllerTest {

    @Mock
    private MovieService mockService;
    @Mock
    private CommandManager mockManager;

    private MovieController movieController;

    private MovieDTO goodDTO;
    private MovieDTO emptyTitleDTO;
    private MovieDTO badYearDTO;
    private Movie fakeMovie; // Memento for Add/Edit

    @BeforeEach
    void setUp() {
        movieController = new MovieController(mockService, mockManager);

        goodDTO = new MovieDTO("Test Title", "Director", "2025", null, null, 3);
        emptyTitleDTO = new MovieDTO("", "Director", "2025", null, null, 3);
        badYearDTO = new MovieDTO("Title", "Director", "Not-A-Year", null, null, 3);

        fakeMovie = new Movie.Builder("Test Title", "Director").build();
    }

    @Test
    @DisplayName("setSortStrategy should delegate to service")
    void setSortStrategy_WhenCalled_ShouldDelegateToService() {
        movieController.changeSortStrategy(SortStrategyType.RATING);

        verify(mockService, times(1)).setSortStrategy(SortStrategyType.RATING);
    }

    @Test
    @DisplayName("addMovieRequest should return null when validation fails")
    void addMovieRequest_WhenValidationFails_ShouldReturnNullAndNotCallManager() throws Exception {
        Movie resultEmpty = movieController.addMovieRequest(emptyTitleDTO);
        Movie resultBadYear = movieController.addMovieRequest(badYearDTO);

        assertNull(resultEmpty);
        assertNull(resultBadYear);
        verify(mockManager, never()).execute(any(Command.class));
    }

    @Test
    @DisplayName("addMovieRequest should return null when Manager fails")
    void addMovieRequest_WhenManagerFails_ShouldCatchExceptionAndReturnNull() throws Exception {
        doThrow(new Exception("Manager failed")).when(mockManager).execute(any(AddMovieCommand.class));

        Movie result = movieController.addMovieRequest(goodDTO);

        assertNull(result);
        verify(mockManager, times(1)).execute(any(AddMovieCommand.class));
    }

    @Test
    @DisplayName("addMovieRequest should execute and return Movie on success")
    void addMovieRequest_WhenSuccess_ShouldExecuteAndReturnMovie() throws Exception {
        when(mockService.addMovie(goodDTO)).thenReturn(fakeMovie);

        doAnswer((Answer<Void>) invocation -> {
            AddMovieCommand cmd = invocation.getArgument(0);
            cmd.execute(); // actually call execute on the command
            return null;
        }).when(mockManager).execute(any(AddMovieCommand.class));

        Movie result = movieController.addMovieRequest(goodDTO);

        assertEquals(fakeMovie, result);
        assertEquals(fakeMovie.getId(), result.getId());
    }

    @Test
    @DisplayName("deleteMovieRequest should propagate exception when Manager fails")
    void deleteMovieRequest_WhenManagerFails_ShouldPropagateException() throws Exception {
        doThrow(new Exception("Delete failed")).when(mockManager).execute(any(DeleteMovieCommand.class));

        Exception exception = assertThrows(Exception.class, () -> movieController.deleteMovieRequest(fakeMovie));
        assertEquals("Delete failed", exception.getMessage());
    }

    @Test
    @DisplayName("undoRequest should return command from Manager")
    void undoRequest_WhenCalled_ShouldReturnCommandFromManager() throws Exception {
        Command fakeCommand = mock(Command.class);
        when(mockManager.undo()).thenReturn(fakeCommand);

        Command result = movieController.undoRequest();

        assertEquals(fakeCommand, result);
        verify(mockManager, times(1)).undo();
    }

    @Test
    @DisplayName("undoRequest should propagate exception when Manager fails")
    void undoRequest_WhenManagerFails_ShouldPropagateException() throws Exception {
        when(mockManager.undo()).thenThrow(new Exception("Undo stack empty"));

        Exception exception = assertThrows(Exception.class, () -> movieController.undoRequest());
        assertEquals("Undo stack empty", exception.getMessage());
    }
}
