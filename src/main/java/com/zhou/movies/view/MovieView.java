package com.zhou.movies.view;

import com.zhou.movies.controller.MovieController;
import com.zhou.movies.pojo.Movie;
import com.zhou.movies.service.Observer;
import com.zhou.movies.view.components.ActionPanel;
import com.zhou.movies.view.components.MovieInputPanel;
import com.zhou.movies.view.components.ToolbarPanel;
import com.zhou.movies.view.state.FormState;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * MovieView acts as a "dumb container" following SRP:
 * 1. Holds and arranges all UI components.
 * 2. Observes MovieService to refresh the table.
 * 3. Provides public getters/setters for external access (Manager, State, Visitor).
 */
public class MovieView extends JFrame implements Observer {

    private MovieController controller;
    private JTable movieTable;
    private DefaultTableModel tableModel;
    private MovieInputPanel inputPanel;
    private ToolbarPanel toolbarPanel;
    private ActionPanel actionPanel;
    private List<Movie> currentMoviesList;
    private FormState currentState;

    public MovieView() {
        setTitle("My movies collection");
        setSize(1300, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    /** Initialize and layout UI components. */
    private void initComponents() {
        // Table
        String[] columnNames = {"Title", "Director", "Year", "Category", "Status", "Rating"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        movieTable = new JTable(tableModel);
        movieTable.getTableHeader().setReorderingAllowed(false);

        // Sub-panels
        inputPanel = new MovieInputPanel();
        toolbarPanel = new ToolbarPanel();
        actionPanel = new ActionPanel();
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Layout
        setLayout(new BorderLayout());
        add(toolbarPanel, BorderLayout.NORTH);
        add(new JScrollPane(movieTable), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        add(actionPanel, BorderLayout.EAST);
    }

    /** Observer update: refresh table with current movie list. */
    @Override
    public void update() {
        if (controller != null) {
            this.currentMoviesList = controller.getAllMovies();
            refreshTable(currentMoviesList);
        }
    }

    /** Refresh table rows with given movie data. */
    public void refreshTable(List<Movie> movies) {
        tableModel.setRowCount(0);
        for (Movie movie : movies) {
            Object[] row = {
                    movie.getTitle(),
                    movie.getDirector(),
                    movie.getYear(),
                    movie.getCategory(),
                    movie.getStatus(),
                    movie.getRating()
            };
            tableModel.addRow(row);
        }
    }

    /** Select a table row by movie ID (used by State/Visitor/UndoRedo). */
    public void selectRowById(String movieId) {
        int modelRowIndex = -1;
        for (int i = 0; i < currentMoviesList.size(); i++) {
            if (currentMoviesList.get(i).getId().equals(movieId)) {
                modelRowIndex = i;
                break;
            }
        }
        if (modelRowIndex != -1) {
            int viewRowIndex = movieTable.convertRowIndexToView(modelRowIndex);
            if (viewRowIndex != -1) {
                movieTable.setRowSelectionInterval(viewRowIndex, viewRowIndex);
                movieTable.scrollRectToVisible(movieTable.getCellRect(viewRowIndex, 0, true));
            }
        }
    }

    // Getters / Setters for external access
    public void setController(MovieController controller) {
        this.controller = controller;
        this.update(); // load initial data
    }

    public MovieController getController() { return controller; }
    public MovieInputPanel getInputPanel() { return inputPanel; }
    public ToolbarPanel getToolbarPanel() { return toolbarPanel; }
    public ActionPanel getActionPanel() { return actionPanel; }
    public JTable getMovieTable() { return movieTable; }
    public List<Movie> getCurrentMoviesList() { return currentMoviesList; }
    public FormState getCurrentState() { return currentState; }
    public void setCurrentState(FormState state) { this.currentState = state; }
}
