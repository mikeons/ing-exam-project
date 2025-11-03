package com.zhou.movies.view;

import com.zhou.movies.controller.MovieController;
import com.zhou.movies.pojo.Category;
import com.zhou.movies.pojo.Movie;
import com.zhou.movies.pojo.Status;
import com.zhou.movies.service.Observer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MovieView extends JFrame implements Observer {

    private MovieController controller;

    private JTable movieTable;
    private DefaultTableModel tableModel;

    private JPanel inputPanel;

    private final JTextField titleField = new JTextField(20);
    private final JTextField directorField = new JTextField(20);
    private final JTextField yearField = new JTextField(5);

    private JComboBox<Category> categoryJComboBox;
    private JComboBox<Status> statusComboBox;
    private JComboBox<Integer> ratingComboBox;
    private final JButton addButton = new JButton("ADD");

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

        initPanel();
        initLayout();

        addButton.addActionListener(e -> {
            if (controller != null) {
                Category category = (Category) categoryJComboBox.getSelectedItem();
                Status status = (Status) statusComboBox.getSelectedItem();
                Integer rating = (Integer) ratingComboBox.getSelectedItem();

                controller.addMovieRequest(
                        titleField.getText(),
                        directorField.getText(),
                        yearField.getText(),
                        category,
                        status,
                        rating
                );
            }
        });
    }

    private void initPanel(){
        // --- Initialize the input panel ---
        inputPanel = new JPanel(new FlowLayout());

        // --- Initialize Selection Box ---
        Integer[] ratings = {1, 2, 3, 4, 5};
        ratingComboBox = new JComboBox<>(ratings);
        categoryJComboBox = new JComboBox<>(Category.values());
        statusComboBox = new JComboBox<>(Status.values());

        // --- Add initialized elements on Input Panel ---
        inputPanel.add(new JLabel("Title:"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Director:"));
        inputPanel.add(directorField);
        inputPanel.add(new JLabel("Year:"));
        inputPanel.add(yearField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryJComboBox);
        inputPanel.add(new JLabel("Status:"));
        inputPanel.add(statusComboBox);
        inputPanel.add(new JLabel("Rating:"));
        inputPanel.add(ratingComboBox);
        inputPanel.add(addButton);
    }

    private void initLayout() {
        setLayout(new BorderLayout());
        add(new JScrollPane(movieTable), BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
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

    // Invoked by the Controller after adding successfully a new item
    public void clearInputFields() {
        titleField.setText("");
        directorField.setText("");
        yearField.setText("");
        categoryJComboBox.setSelectedIndex(0);
        statusComboBox.setSelectedIndex(0);
        ratingComboBox.setSelectedIndex(0);
    }

    @Override
    public void update() {
        List<Movie> updatedMovies = controller.getAllMovies();
        refreshTable(updatedMovies);
        clearInputFields();
    }
}