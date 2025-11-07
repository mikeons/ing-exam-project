package com.zhou.movies.view;

import com.zhou.movies.pojo.Category;
import com.zhou.movies.pojo.Status;
import com.zhou.movies.service.strategy.SortStrategyType;

import javax.swing.*;
import java.awt.*;

/**
 * Toolbar providing sorting and filtering controls for the movie list.
 * Provides sorting (field + direction) and basic filters.
 * Filters include category, status and rating, each supporting an "All" state.
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

    public ToolbarPanel() {

        // ---------------------------------
        // Group 1: SORTING
        // ---------------------------------
        add(new JLabel(" Sort by: "));
        sortComboBox = new JComboBox<>(SortStrategyType.values());
        add(sortComboBox);

        sortDirectionButton = new JToggleButton("Ascending ⬆️");
        sortDirectionButton.setSelected(false);
        add(sortDirectionButton);

        // --- SEPARATOR ---
        addSeparator();

        // ---------------------------------
        // Group 2: FILTERING
        // ---------------------------------
        add(new JLabel(" Filter by: "));

        // category filter
        categoryFilterComboBox = createEnumComboBox(Category.values());
        add(categoryFilterComboBox);

        // status filter
        statusFilterComboBox = createEnumComboBox(Status.values());
        add(statusFilterComboBox);

        // rating filter (1-5)
        Integer[] ratings = {1, 2, 3, 4, 5};
        ratingFilterComboBox = createEnumComboBox(ratings);
        add(ratingFilterComboBox);

        // --- SEPARATOR ---
        addSeparator();

        // ---------------------------------
        // Group 3: SEARCHING
        // ---------------------------------
        add(new JLabel(" Search: "));
        searchField = new JTextField(12);
        add(searchField);

        searchButton = new JButton("Search \uD83D\uDD0D");
        add(searchButton);

        // --- SEPARATOR ---
        addSeparator();

        // ---------------------------------
        // Group 4: GLOBAL ACTIONS
        // ---------------------------------
        resetButton = new JButton("Reset All 🔄");
        resetButton.setPreferredSize(new Dimension(120, 28));
        add(resetButton);

    }

    //Create ComboBox with "All" (null) option
    private <T> JComboBox<T> createEnumComboBox(T[] items) {
        JComboBox<T> comboBox = new JComboBox<>();
        comboBox.addItem(null);
        for (T item : items) {
            comboBox.addItem(item);
        }

        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("All");
                }
                return this;
            }
        });
        return comboBox;
    }

    public JComboBox<SortStrategyType> getSortComboBox() {
        return sortComboBox;
    }

    public JToggleButton getSortDirectionButton() {
        return sortDirectionButton;
    }

    public JComboBox<Category> getCategoryFilterComboBox() {
        return categoryFilterComboBox;
    }

    public JComboBox<Status> getStatusFilterComboBox() {
        return statusFilterComboBox;
    }

    public JComboBox<Integer> getRatingFilterComboBox() {
        return ratingFilterComboBox;
    }

    public JButton getResetButton() {
        return resetButton;
    }

    public JButton getSearchButton() {
        return searchButton;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public String getSearchQuery() {
        return searchField.getText();
    }

    public void resetFilterControls() {
        categoryFilterComboBox.setSelectedItem(null);
        statusFilterComboBox.setSelectedItem(null);
        ratingFilterComboBox.setSelectedItem(null);
        searchField.setText("");
    }

    public void resetSortControls() {
        sortComboBox.setSelectedItem(SortStrategyType.TITLE);

        sortDirectionButton.setSelected(false);
        sortDirectionButton.setText("Ascending ⬆️");
    }
}
