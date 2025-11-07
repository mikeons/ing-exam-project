package com.zhou.movies.view;

import com.zhou.movies.controller.MovieController;
import com.zhou.movies.dto.MovieDTO;
import com.zhou.movies.pojo.Category;
import com.zhou.movies.pojo.Movie;
import com.zhou.movies.pojo.Status;
import com.zhou.movies.service.Observer;
import com.zhou.movies.service.strategy.SortDirection;
import com.zhou.movies.service.strategy.SortStrategyType;
import com.zhou.movies.view.state.FormState;
import com.zhou.movies.view.state.impl.AddModeState;
import com.zhou.movies.view.state.impl.EditModeState;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.util.List;

/**
 * Acts as the View in the MVC pattern.
 * Handles UI display, user interactions, and communication with the controller.
 * Updates the movie table when data changes.
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
    private Movie movieToEdit = null;

    public MovieView() {
        setTitle("My movies collection");
        setSize(1300, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    public void setController(MovieController controller) {
        this.controller = controller;

        this.currentState = new AddModeState();
        this.currentState.enterState(this);

        this.update();
    }

    public MovieController getController(){
        return controller;
    }

    private void initComponents() {
        // --- Table ---
        String[] columnNames = {"Title", "Director", "Year", "Category", "Status", "Rating"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        movieTable = new JTable(tableModel);
        movieTable.getTableHeader().setReorderingAllowed(false);

        // --- Initialize Sub Panels ---
        inputPanel = new MovieInputPanel();
        toolbarPanel = new ToolbarPanel();
        actionPanel = new ActionPanel();

        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // --- Initialize Layout ---
        setLayout(new BorderLayout());
        add(toolbarPanel, BorderLayout.NORTH);
        add(new JScrollPane(movieTable), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        add(actionPanel, BorderLayout.EAST); // 添加到布局的东侧
        // --- Linking Listeners to buttons ---
        initListeners();
    }


    private void initListeners(){
        initFormListeners();
        initEditDeleteListeners();
        initSortAndFilterListeners();
        initSearchListeners();
    }

    private void initSearchListeners() {
        // Search Action
        Runnable searchAction = () -> {
            if (controller != null) {
                String query = toolbarPanel.getSearchQuery();
                controller.searchMovies(query);
            }
        };

        // Search with button
        toolbarPanel.getSearchButton().addActionListener(e -> searchAction.run());

        // Search with Enter
        toolbarPanel.getSearchField().addActionListener(e -> searchAction.run());
    }

    private void initFormListeners(){
        // --- Submit button listener ---
        inputPanel.getSubmitButton().addActionListener(e -> {
            if (controller == null) return;

            // Create DTO from form
            MovieDTO dto = new MovieDTO(
                    inputPanel.getTitleText(),
                    inputPanel.getDirectorText(),
                    inputPanel.getYearText(),
                    inputPanel.getSelectedCategory(),
                    inputPanel.getSelectedStatus(),
                    inputPanel.getSelectedRating()
            );

            FormState nextState = currentState.handleSubmit(this, dto);

            if (nextState != this.currentState){
                this.currentState = nextState;
                this.currentState.enterState(this);
            }
        });

        inputPanel.getCancelButton().addActionListener(e -> {
            this.currentState = new AddModeState();
            this.currentState.enterState(this);
        });
    }

    private void initEditDeleteListeners() {
        // --- Edit Button Listener ---
        actionPanel.getEditButton().addActionListener(e -> {
            int viewRow = movieTable.getSelectedRow();
            if (viewRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a movie to edit");
                return;
            }
            int modelRow = movieTable.convertRowIndexToModel(viewRow);
            this.movieToEdit = this.currentMoviesList.get(modelRow);

            this.currentState = new EditModeState(this.movieToEdit);
            this.currentState.enterState(this);
        });

        // --- Delete Button Listener ---
        actionPanel.getDeleteButton().addActionListener(e -> {
            int viewRow = movieTable.getSelectedRow();
            if (viewRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a movie to delete");
                return;
            }
            int choice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete the selected movie?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
            int modelRow = movieTable.convertRowIndexToModel(viewRow);
            Movie movieToDelete = this.currentMoviesList.get(modelRow);
            controller.deleteMovie(movieToDelete.getId());
        });
    }

    private void initSortAndFilterListeners() {
        // --- Sort ComboBox ---
        toolbarPanel.getSortComboBox().addActionListener(e -> {
            if (controller != null) {
                SortStrategyType selectedStrategy = (SortStrategyType) toolbarPanel.getSortComboBox().getSelectedItem();
                controller.changeSortStrategy(selectedStrategy);
            }
        });

        // --- Sort Direction Button ---
        toolbarPanel.getSortDirectionButton().addActionListener(e -> {
            if (controller != null) {
                JToggleButton button = toolbarPanel.getSortDirectionButton();
                if (button.isSelected()) {
                    button.setText("Descending ⬇️");
                    controller.changeSortDirection(SortDirection.DESCENDING);
                } else {
                    button.setText("Ascending ⬆️");
                    controller.changeSortDirection(SortDirection.ASCENDING);
                }
            }
        });

        // --- Category Filter ---
        toolbarPanel.getCategoryFilterComboBox().addActionListener(e -> {
            if (controller != null) {
                Category selected = (Category) toolbarPanel.getCategoryFilterComboBox().getSelectedItem();
                controller.setFilterCategory(selected);
            }
        });

        // --- Status Filter ---
        toolbarPanel.getStatusFilterComboBox().addActionListener(e -> {
            if (controller != null) {
                Status selected = (Status) toolbarPanel.getStatusFilterComboBox().getSelectedItem();
                controller.setFilterStatus(selected);
            }
        });

        // --- Rating Filter ---
        toolbarPanel.getRatingFilterComboBox().addActionListener(e -> {
            if (controller != null) {
                Integer selected = (Integer) toolbarPanel.getRatingFilterComboBox().getSelectedItem();
                controller.setFilterRating(selected);
            }
        });

        // --- Reset Button ---
        toolbarPanel.getResetButton().addActionListener(e -> {
            if (controller != null) {
                controller.resetFiltersAndSort();
                toolbarPanel.resetFilterControls();
                toolbarPanel.resetSortControls();

                this.currentState = new AddModeState();
                this.currentState.enterState(this);
            }
        });
    }

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

    public MovieInputPanel getInputPanel() {
        return this.inputPanel;
    }

    public void setMovieToEdit(Movie movie) {
        this.movieToEdit = movie;
    }

    @Override
    public void update() {
        this.currentMoviesList = controller.getAllMovies();
        refreshTable(currentMoviesList);
    }
}