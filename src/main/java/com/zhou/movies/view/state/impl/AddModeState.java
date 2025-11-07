package com.zhou.movies.view.state.impl;

import com.zhou.movies.dto.MovieDTO;
import com.zhou.movies.view.MovieInputPanel;
import com.zhou.movies.view.MovieView;
import com.zhou.movies.view.state.FormState;

import javax.swing.JOptionPane;

public class AddModeState implements FormState {

    @Override
    public FormState handleSubmit(MovieView context, MovieDTO dto) {
        boolean success = context.getController().addMovieRequest(dto);
        if (success) {
            context.getInputPanel().clearFields();
        } else {
            JOptionPane.showMessageDialog(context, "Insertion Failed...", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return this;
    }

    @Override
    public void enterState(MovieView context) {
        MovieInputPanel panel = context.getInputPanel();
        panel.clearFields();
        panel.getSubmitButton().setText("ADD");
        panel.getCancelButton().setVisible(false);

        context.setMovieToEdit(null);
    }
}