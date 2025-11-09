package com.zhou.movies.view.event;

import com.zhou.movies.command.Command;
import com.zhou.movies.command.CommandVisitor;
import com.zhou.movies.controller.MovieController;
import com.zhou.movies.dto.MovieDTO;
import com.zhou.movies.pojo.Category;
import com.zhou.movies.pojo.Movie;
import com.zhou.movies.pojo.Status;
import com.zhou.movies.service.strategy.SortDirection;
import com.zhou.movies.service.strategy.SortStrategyType;
import com.zhou.movies.view.MovieView;
import com.zhou.movies.view.state.FormState;
import com.zhou.movies.view.state.impl.AddModeState;
import com.zhou.movies.view.state.impl.EditModeState;
import com.zhou.movies.view.visitor.RedoViewVisitor;
import com.zhou.movies.view.visitor.UndoViewVisitor;

import javax.swing.*;
import java.awt.Point;

/**
 * Manages all UI listeners and decouples UI logic from MovieView.
 * Acts as a binder between View and Controller.
 */
public class ViewListenerManager {

    private final MovieView view;
    private final MovieController controller;
    private final CommandVisitor undoVisitor;
    private final CommandVisitor redoVisitor;

    public ViewListenerManager(MovieView view, MovieController controller) {
        this.view = view;
        this.controller = controller;
        this.undoVisitor = new UndoViewVisitor(view);
        this.redoVisitor = new RedoViewVisitor(view);
    }

    /** Public entry to activate all UI listeners. */
    public void bindListeners() {
        initFormListeners();
        initEditDeleteListeners();
        initSortAndFilterListeners();
        initSearchListeners();
        initUndoRedoListeners();
    }

    /** Form (State) logic for Submit/Cancel buttons. */
    private void initFormListeners() {
        view.getInputPanel().getSubmitButton().addActionListener(e -> {
            if (controller == null) return;

            MovieDTO dto = new MovieDTO(
                    view.getInputPanel().getTitleText(),
                    view.getInputPanel().getDirectorText(),
                    view.getInputPanel().getYearText(),
                    view.getInputPanel().getSelectedCategory(),
                    view.getInputPanel().getSelectedStatus(),
                    view.getInputPanel().getSelectedRating()
            );

            FormState nextState = view.getCurrentState().handleSubmit(view, dto);

            if (nextState != view.getCurrentState()) {
                view.setCurrentState(nextState);
                view.getCurrentState().enterState(view);
            }
        });

        view.getInputPanel().getCancelButton().addActionListener(e -> {
            FormState nextState = new AddModeState();
            view.setCurrentState(nextState);
            nextState.enterState(view);
        });
    }

    /** Edit/Delete logic (Command pattern). */
    private void initEditDeleteListeners() {
        view.getActionPanel().getEditButton().addActionListener(e -> {
            Movie movieToEdit = getMovieFromSelectedRow();
            if (movieToEdit != null) enterEditMode(movieToEdit);
        });

        view.getMovieTable().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Movie movieToEdit = getMovieFromClickedRow(e.getPoint());
                    if (movieToEdit != null) enterEditMode(movieToEdit);
                }
            }
        });

        view.getActionPanel().getDeleteButton().addActionListener(e -> {
            Movie movieToDelete = getMovieFromSelectedRow();
            if (movieToDelete == null) return;

            if (confirmDelete()) {
                try {
                    controller.deleteMovieRequest(movieToDelete);
                } catch (Exception ex) {
                    showError("Delete Error", "Delete operation failed: \n" + ex.getMessage());
                }
            }
        });
    }

    /** Undo/Redo logic using Visitor pattern. */
    private void initUndoRedoListeners() {
        view.getToolbarPanel().getUndoButton().addActionListener(e -> {
            if (controller != null) {
                try {
                    Command cmd = controller.undoRequest();
                    if (cmd != null) cmd.accept(undoVisitor);
                } catch (Exception ex) {
                    showError("Undo Error", "Undo operation failed: \n" + ex.getMessage());
                }
            }
        });

        view.getToolbarPanel().getRedoButton().addActionListener(e -> {
            if (controller != null) {
                try {
                    Command cmd = controller.redoRequest();
                    if (cmd != null) cmd.accept(redoVisitor);
                } catch (Exception ex) {
                    showError("Redo Error", "Redo operation failed: \n" + ex.getMessage());
                }
            }
        });
    }

    /** Sorting, filtering, and reset logic (Strategy pattern). */
    private void initSortAndFilterListeners() {
        view.getToolbarPanel().getSortComboBox().addActionListener(e -> {
            if (controller != null) controller.changeSortStrategy(
                    (SortStrategyType) view.getToolbarPanel().getSortComboBox().getSelectedItem()
            );
        });

        view.getToolbarPanel().getSortDirectionButton().addActionListener(e -> {
            if (controller != null) {
                JToggleButton button = view.getToolbarPanel().getSortDirectionButton();
                if (button.isSelected()) {
                    button.setText("Descending ⬇️");
                    controller.changeSortDirection(SortDirection.DESCENDING);
                } else {
                    button.setText("Ascending ⬆️");
                    controller.changeSortDirection(SortDirection.ASCENDING);
                }
            }
        });

        view.getToolbarPanel().getCategoryFilterComboBox().addActionListener(e -> {
            if (controller != null) controller.setFilterCategory(
                    (Category) view.getToolbarPanel().getCategoryFilterComboBox().getSelectedItem()
            );
        });
        view.getToolbarPanel().getStatusFilterComboBox().addActionListener(e -> {
            if (controller != null) controller.setFilterStatus(
                    (Status) view.getToolbarPanel().getStatusFilterComboBox().getSelectedItem()
            );
        });
        view.getToolbarPanel().getRatingFilterComboBox().addActionListener(e -> {
            if (controller != null) controller.setFilterRating(
                    (Integer) view.getToolbarPanel().getRatingFilterComboBox().getSelectedItem()
            );
        });

        view.getToolbarPanel().getResetButton().addActionListener(e -> {
            if (controller != null) {
                controller.resetFiltersAndSort();
                view.getToolbarPanel().resetFilterControls();
                view.getToolbarPanel().resetSortControls();

                FormState nextState = new AddModeState();
                view.setCurrentState(nextState);
                nextState.enterState(view);
            }
        });
    }

    /** Search logic. */
    private void initSearchListeners() {
        Runnable searchAction = () -> {
            if (controller != null) controller.searchMovies(view.getToolbarPanel().getSearchQuery());
        };
        view.getToolbarPanel().getSearchButton().addActionListener(e -> searchAction.run());
        view.getToolbarPanel().getSearchField().addActionListener(e -> searchAction.run());
    }

    // ------------------------------------------------------------------
    // Helper methods
    // ------------------------------------------------------------------

    private void enterEditMode(Movie movie) {
        FormState nextState = new EditModeState(movie);
        view.setCurrentState(nextState);
        nextState.enterState(view);
    }

    private Movie getMovieFromSelectedRow() {
        int viewRow = view.getMovieTable().getSelectedRow();
        if (viewRow == -1) { showError("No Selection", "Please select a movie"); return null; }
        int modelRow = view.getMovieTable().convertRowIndexToModel(viewRow);
        return view.getCurrentMoviesList().get(modelRow);
    }

    private Movie getMovieFromClickedRow(Point point) {
        int viewRow = view.getMovieTable().rowAtPoint(point);
        if (viewRow >= 0) return view.getCurrentMoviesList()
                .get(view.getMovieTable().convertRowIndexToModel(viewRow));
        return null;
    }

    private boolean confirmDelete() {
        int choice = JOptionPane.showConfirmDialog(view,
                "Are you sure you want to delete the selected movie?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        return choice == JOptionPane.YES_OPTION;
    }

    private void showError(String title, String message) {
        System.out.println(title + ": " + message);
        JOptionPane.showMessageDialog(view, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
