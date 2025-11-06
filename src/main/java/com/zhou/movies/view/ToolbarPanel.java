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
    private final JButton editButton;
    private final JButton deleteButton;


    public ToolbarPanel() {
        add(new JLabel("Sort by:"));
        sortComboBox = new JComboBox<>(SortStrategyType.values());
        add(sortComboBox);

        // sort direction toggle
        sortDirectionButton = new JToggleButton("Ascending ⬆️");
        sortDirectionButton.setSelected(false);
        add(sortDirectionButton);

        addSeparator();

        add(new JLabel("Filter by:"));

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

        // reset all fields button
        resetButton = new JButton("Reset 🔄");
        add(resetButton);

        // edite movie button
        editButton = new JButton("Edit ✍️");
        add(editButton);

        // delete movie button
        deleteButton = new JButton("Delete 🗑️");
        add(deleteButton);

        addSeparator();
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

    public JButton getEditButton(){
        return editButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }

    public void resetFilterControls() {
        categoryFilterComboBox.setSelectedItem(null);
        statusFilterComboBox.setSelectedItem(null);
        ratingFilterComboBox.setSelectedItem(null);
    }

    public void resetSortControls() {
        sortComboBox.setSelectedItem(SortStrategyType.TITLE);

        sortDirectionButton.setSelected(false);
        sortDirectionButton.setText("Ascending ⬆️");
    }
}
