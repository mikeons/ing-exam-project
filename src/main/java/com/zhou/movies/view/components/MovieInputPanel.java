package com.zhou.movies.view.components;

import javax.swing.*;
import com.zhou.movies.pojo.Category;
import com.zhou.movies.pojo.Status;
import com.zhou.movies.pojo.Movie;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * A form panel dedicated to movie input.
 *
 * Responsibility:
 * - Collect user input for creating or editing a Movie.
 */
public class MovieInputPanel extends JPanel {
    private final JTextField titleField = new JTextField(20);
    private final JTextField directorField = new JTextField(20);
    private final JTextField yearField = new JTextField(5);

    private final JComboBox<Category> categoryJComboBox;
    private final JComboBox<Status> statusComboBox;
    private final JComboBox<Integer> ratingComboBox;

    private final JButton submitButton = new JButton("ADD");
    private final JButton cancelButton = new JButton("Cancel");

    public MovieInputPanel() {
        setLayout(new FlowLayout());

        Integer[] ratings = {1, 2, 3, 4, 5};
        ratingComboBox = new JComboBox<>(ratings);
        categoryJComboBox = new JComboBox<>(Category.values());
        statusComboBox = new JComboBox<>(Status.values());

        // Build form
        add(new JLabel("Title:")); add(titleField);
        add(new JLabel("Director:")); add(directorField);
        add(new JLabel("Year:")); add(yearField);
        add(new JLabel("Category:")); add(categoryJComboBox);
        add(new JLabel("Status:")); add(statusComboBox);
        add(new JLabel("Rating:")); add(ratingComboBox);

        add(submitButton);
        add(cancelButton);
        cancelButton.setVisible(false);

        // ENTER key triggers submission
        ActionListener submitOnEnter = e -> submitButton.doClick();
        titleField.addActionListener(submitOnEnter);
        directorField.addActionListener(submitOnEnter);
        yearField.addActionListener(submitOnEnter);
    }

    // --- Getters ---
    public String getTitleText() { return titleField.getText(); }
    public String getDirectorText() { return directorField.getText(); }
    public String getYearText() { return yearField.getText(); }
    public Category getSelectedCategory() { return (Category) categoryJComboBox.getSelectedItem(); }
    public Status getSelectedStatus() { return (Status) statusComboBox.getSelectedItem(); }
    public Integer getSelectedRating() { return (Integer) ratingComboBox.getSelectedItem(); }
    public JButton getSubmitButton() { return submitButton; }
    public JButton getCancelButton() { return cancelButton; }

    // --- Helpers ---
    /** Clears all input fields. */
    public void clearFields() {
        titleField.setText("");
        directorField.setText("");
        yearField.setText("");
        categoryJComboBox.setSelectedIndex(0);
        statusComboBox.setSelectedIndex(0);
        ratingComboBox.setSelectedIndex(0);
    }

    /** Populates the form with data from an existing movie. */
    public void populateForm(Movie movie) {
        titleField.setText(movie.getTitle());
        directorField.setText(movie.getDirector());
        yearField.setText(String.valueOf(movie.getYear()));
        categoryJComboBox.setSelectedItem(movie.getCategory());
        statusComboBox.setSelectedItem(movie.getStatus());
        ratingComboBox.setSelectedItem(movie.getRating());
    }

    /** Requests focus on the title field. */
    public void focusTitleField() {
        titleField.requestFocusInWindow();
    }
}
