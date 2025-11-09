package com.zhou.movies.view.components;

import com.zhou.movies.pojo.Category;
import com.zhou.movies.pojo.Status;
import com.zhou.movies.service.strategy.SortStrategyType;
import javax.swing.*;
import java.awt.*;

/**
 * Toolbar panel providing controls for sorting, filtering, and searching movies.
 *
 * Responsibilities:
 * - Manage sort criteria and direction
 * - Provide filters (category, status, rating)
 * - Enable search, reset, undo, and redo actions
 */
public class ToolbarPanel extends JToolBar {

    private final JComboBox<SortStrategyType> sortComboBox;
    private final JToggleButton sortDirectionButton;

    private final JComboBox<Category> categoryFilterComboBox;
    private final JComboBox<Status> statusFilterComboBox;
    private final JComboBox<Integer> ratingFilterComboBox;

    private final JButton resetButton;
    private final JTextField searchField;
    private final JButton searchButton;

    private final JButton undoButton;
    private final JButton redoButton;

    public ToolbarPanel() {

        // --- Undo / Redo ---
        undoButton = new JButton("Undo ↩️");
        redoButton = new JButton("Redo ↪️");
        add(undoButton);
        add(redoButton);
        addSeparator();

        // --- Sorting ---
        add(new JLabel(" Sort by: "));
        sortComboBox = new JComboBox<>(SortStrategyType.values());
        add(sortComboBox);

        sortDirectionButton = new JToggleButton("Ascending ⬆️");
        add(sortDirectionButton);
        addSeparator();

        // --- Filtering ---
        add(new JLabel(" Filter by: "));
        categoryFilterComboBox = createEnumComboBox(Category.values());
        statusFilterComboBox = createEnumComboBox(Status.values());
        ratingFilterComboBox = createEnumComboBox(new Integer[]{1, 2, 3, 4, 5});
        add(categoryFilterComboBox);
        add(statusFilterComboBox);
        add(ratingFilterComboBox);
        addSeparator();

        // --- Searching ---
        add(new JLabel(" Search: "));
        searchField = new JTextField(12);
        searchButton = new JButton("Search 🔍");
        add(searchField);
        add(searchButton);
        addSeparator();

        // --- Global actions ---
        resetButton = new JButton("Reset All 🔄");
        resetButton.setPreferredSize(new Dimension(120, 28));
        add(resetButton);
    }

    /** Creates a combo box with an "All" (null) option. */
    private <T> JComboBox<T> createEnumComboBox(T[] items) {
        JComboBox<T> comboBox = new JComboBox<>();
        comboBox.addItem(null);
        for (T item : items) comboBox.addItem(item);

        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) setText("All");
                return this;
            }
        });
        return comboBox;
    }

    // --- Getters ---
    public JComboBox<SortStrategyType> getSortComboBox() { return sortComboBox; }
    public JToggleButton getSortDirectionButton() { return sortDirectionButton; }
    public JComboBox<Category> getCategoryFilterComboBox() { return categoryFilterComboBox; }
    public JComboBox<Status> getStatusFilterComboBox() { return statusFilterComboBox; }
    public JComboBox<Integer> getRatingFilterComboBox() { return ratingFilterComboBox; }
    public JButton getResetButton() { return resetButton; }
    public JButton getSearchButton() { return searchButton; }
    public JTextField getSearchField() { return searchField; }
    public String getSearchQuery() { return searchField.getText(); }
    public JButton getUndoButton() { return undoButton; }
    public JButton getRedoButton() { return redoButton; }

    /** Resets all filter controls to "All" and clears the search field. */
    public void resetFilterControls() {
        categoryFilterComboBox.setSelectedItem(null);
        statusFilterComboBox.setSelectedItem(null);
        ratingFilterComboBox.setSelectedItem(null);
        searchField.setText("");
    }

    /** Resets sorting controls to default state. */
    public void resetSortControls() {
        sortComboBox.setSelectedItem(SortStrategyType.TITLE);
        sortDirectionButton.setSelected(false);
        sortDirectionButton.setText("Ascending ⬆️");
    }
}
