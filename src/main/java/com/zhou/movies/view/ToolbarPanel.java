package com.zhou.movies.view;

import javax.swing.*;
import com.zhou.movies.service.strategy.SortStrategyType;

/**
 * ToolbarPanel provides UI controls for sorting, searching,
 * filtering, saving, and undo operations in the movie application.
 * It holds key interactive components for user actions.
 */
public class ToolbarPanel extends JToolBar {

    private final JComboBox<SortStrategyType> sortComboBox;
    private final JTextField searchField;
    private final JToggleButton sortDirectionButton;

    public ToolbarPanel() {
        add(new JLabel("Sort by:"));
        sortComboBox = new JComboBox<>(SortStrategyType.values());
        add(sortComboBox);

        // Toggle ascending/descending sort
        sortDirectionButton = new JToggleButton("Ascending ⬆️");
        sortDirectionButton.setSelected(false);
        add(sortDirectionButton);

        addSeparator();

        add(new JLabel("Search:"));
        searchField = new JTextField(15);
        add(searchField);
    }

    public JComboBox<SortStrategyType> getSortComboBox() {
        return sortComboBox;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JToggleButton getSortDirectionButton() {
        return sortDirectionButton;
    }
}
