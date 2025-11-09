package com.zhou.movies.view.state.impl;

import com.zhou.movies.dto.MovieDTO;
import com.zhou.movies.pojo.Movie;
import com.zhou.movies.view.components.MovieInputPanel;
import com.zhou.movies.view.MovieView;
import com.zhou.movies.view.state.FormState;

import javax.swing.JOptionPane;

public class AddModeState implements FormState {

    @Override
    public FormState handleSubmit(MovieView context, MovieDTO dto) {
        Movie createdMovie = context.getController().addMovieRequest(dto);

        if (createdMovie != null) {
            context.getInputPanel().clearFields();
            context.selectRowById(createdMovie.getId());
        } else {
            JOptionPane.showMessageDialog(context,
                    "Insertion Failed: Please check input data.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return this;
    }

    @Override
    public void enterState(MovieView context) {
        MovieInputPanel panel = context.getInputPanel();
        panel.clearFields();
        panel.getSubmitButton().setText("ADD");
        panel.getCancelButton().setVisible(false);
    }
}