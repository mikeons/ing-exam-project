package com.zhou.movies.view;

import javax.swing.*;

import com.zhou.movies.pojo.Category;
import com.zhou.movies.pojo.Status;

import java.awt.*;

/**
 * A dedicated panel for "Add Movie" inputs.
 * Its Single Responsibility is to gather data for creating a new movie.
 */
public class MovieInputPanel extends JPanel {
    private final JTextField titleField = new JTextField(20);
    private final JTextField directorField = new JTextField(20);
    private final JTextField yearField = new JTextField(5);

    private final JComboBox<Category> categoryJComboBox;
    private final JComboBox<Status> statusComboBox;
    private final JComboBox<Integer> ratingComboBox;
    private final JButton addButton = new JButton("ADD");

    public MovieInputPanel() {
        setLayout(new FlowLayout());

        Integer[] ratings = {1, 2, 3, 4, 5};
        ratingComboBox = new JComboBox<>(ratings);
        categoryJComboBox = new JComboBox<>(Category.values());
        statusComboBox = new JComboBox<>(Status.values());

        add(new JLabel("Title:"));
        add(titleField);
        add(new JLabel("Director:"));
        add(directorField);
        add(new JLabel("Year:"));
        add(yearField);
        add(new JLabel("Category:"));
        add(categoryJComboBox);
        add(new JLabel("Status:"));
        add(statusComboBox);
        add(new JLabel("Rating:"));
        add(ratingComboBox);
        add(addButton);
    }

    public String getTitleText() { return titleField.getText(); }
    public String getDirectorText() { return directorField.getText(); }
    public String getYearText() { return yearField.getText(); }
    public Category getSelectedCategory() { return (Category) categoryJComboBox.getSelectedItem(); }
    public Status getSelectedStatus() { return (Status) statusComboBox.getSelectedItem(); }
    public Integer getSelectedRating() { return (Integer) ratingComboBox.getSelectedItem(); }

    public JButton getAddButton() { return addButton; }

    public void clearFields() {
        titleField.setText("");
        directorField.setText("");
        yearField.setText("");
        categoryJComboBox.setSelectedIndex(0);
        statusComboBox.setSelectedIndex(0);
        ratingComboBox.setSelectedIndex(0);
    }
}