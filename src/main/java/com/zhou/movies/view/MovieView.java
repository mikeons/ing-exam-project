package com.zhou.movies.view;

import com.zhou.movies.controller.MovieController;
import com.zhou.movies.dto.MovieDTO;
import com.zhou.movies.pojo.Category;
import com.zhou.movies.pojo.Movie;
import com.zhou.movies.pojo.Status;
import com.zhou.movies.service.Observer;
import com.zhou.movies.service.strategy.SortDirection;
import com.zhou.movies.service.strategy.SortStrategyType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MovieView extends JFrame implements Observer {
    private MovieController controller;

    private JTable movieTable;
    private DefaultTableModel tableModel;

    private MovieInputPanel inputPanel;
    private ToolbarPanel toolbarPanel;

    public MovieView() {
        setTitle("My movies collection");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    public void setController(MovieController controller) {
        this.controller = controller;
    }

    private void initComponents() {
        // --- Table ---
        String[] columnNames = {"Title", "Director", "Year", "Category", "Status", "Rating"};
        tableModel = new DefaultTableModel(columnNames, 0);
        movieTable = new JTable(tableModel);

        // --- Initialize Sub Panels ---
        inputPanel = new MovieInputPanel();
        toolbarPanel = new ToolbarPanel();

        // --- Initialize Layout ---
        setLayout(new BorderLayout());
        add(toolbarPanel, BorderLayout.NORTH);
        add(new JScrollPane(movieTable), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        // --- Linking Listeners to buttons ---
        initListeners();
    }

    private void initListeners(){
        inputPanel.getAddButton().addActionListener(e -> {
            if (controller != null) {

                //1. View creates a DTO
                MovieDTO movieDTO = new MovieDTO(
                        inputPanel.getTitleText(),
                        inputPanel.getDirectorText(),
                        inputPanel.getYearText(),
                        inputPanel.getSelectedCategory(),
                        inputPanel.getSelectedStatus(),
                        inputPanel.getSelectedRating()
                );

                //2. Passing only ONE parameter to controller!!!
                boolean success = controller.addMovieRequest(movieDTO);

                if (success) {
                    inputPanel.clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Insertion Failed: Pls check the input data!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        toolbarPanel.getSortComboBox().addActionListener(e -> {
            if (controller != null) {
                SortStrategyType selectedStrategy = (SortStrategyType) toolbarPanel.getSortComboBox().getSelectedItem();

                controller.changeSortStrategy(selectedStrategy);
            }
        });

        toolbarPanel.getSortDirectionButton().addActionListener(e -> {
            if (controller != null) {
                JToggleButton button = toolbarPanel.getSortDirectionButton();

                // Is Ascending by default
                if (button.isSelected()) {
                    button.setText("Descending ⬇️");
                    controller.changeSortDirection(SortDirection.DESCENDING);
                } else {
                    button.setText("Ascending ⬆️");
                    controller.changeSortDirection(SortDirection.ASCENDING);
                }
            }
        });

        // 1. Category filter
        toolbarPanel.getCategoryFilterComboBox().addActionListener(e -> {
            if (controller != null) {
                // We use null as "All", so we can convert directly
                Category selected = (Category) toolbarPanel.getCategoryFilterComboBox().getSelectedItem();
                controller.setFilterCategory(selected);
            }
        });

        // 2. Status filter
        toolbarPanel.getStatusFilterComboBox().addActionListener(e -> {
            if (controller != null) {
                Status selected = (Status) toolbarPanel.getStatusFilterComboBox().getSelectedItem();
                controller.setFilterStatus(selected);
            }
        });

        // 3. Rating filter
        toolbarPanel.getRatingFilterComboBox().addActionListener(e -> {
            if (controller != null) {
                Integer selected = (Integer) toolbarPanel.getRatingFilterComboBox().getSelectedItem();
                controller.setFilterRating(selected);
            }
        });

        // 4. Reset all fields of sorting and filters
        toolbarPanel.getResetButton().addActionListener(e -> {
            if (controller != null) {
                controller.resetFiltersAndSort();

                toolbarPanel.resetFilterControls();
                toolbarPanel.resetSortControls();
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

    @Override
    public void update() {
        List<Movie> updatedMovies = controller.getAllMovies();
        refreshTable(updatedMovies);
    }
}