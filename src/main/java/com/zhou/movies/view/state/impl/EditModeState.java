package com.zhou.movies.view.state.impl;

import com.zhou.movies.dto.MovieDTO;
import com.zhou.movies.pojo.Movie;
import com.zhou.movies.view.MovieInputPanel;
import com.zhou.movies.view.MovieView;
import com.zhou.movies.view.state.FormState;

import javax.swing.JOptionPane;

public class EditModeState implements FormState {

    private final Movie movieToEdit;

    public EditModeState(Movie movieToEdit) {
        this.movieToEdit = movieToEdit;
    }

    @Override
    public FormState handleSubmit(MovieView context, MovieDTO dto) {
        Movie updatedMovie = context.getController().editMovieRequest(this.movieToEdit, dto);

        if (updatedMovie != null) {
            context.selectRowById(updatedMovie.getId());
            return new AddModeState();
        } else {
            JOptionPane.showMessageDialog(context, "Update Failed...", "Error", JOptionPane.ERROR_MESSAGE);
            return this;
        }
    }

    @Override
    public void enterState(MovieView context) {
        MovieInputPanel panel = context.getInputPanel();
        panel.populateForm(this.movieToEdit);
        panel.getSubmitButton().setText("UPDATE");
        panel.getCancelButton().setVisible(true);
        panel.focusTitleField();
    }
}