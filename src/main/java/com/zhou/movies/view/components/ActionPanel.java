package com.zhou.movies.view.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Panel containing contextual action buttons such as Edit and Delete.
 *
 * Responsibility:
 * Provides a clear, isolated section for user actions related to movie management.
 */
public class ActionPanel extends JPanel {

    private final JButton editButton;
    private final JButton deleteButton;

    public ActionPanel() {
        // Set titled border with padding
        setBorder(BorderFactory.createTitledBorder(
                new EmptyBorder(10, 10, 10, 10),
                "Actions",
                TitledBorder.LEFT,
                TitledBorder.TOP
        ));

        // Use vertical BoxLayout to stack buttons
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Initialize buttons
        editButton = new JButton("Edit Movie ✍️");
        deleteButton = new JButton("Delete Movie 🗑️");

        // Ensure consistent button sizing
        Dimension buttonSize = new Dimension(150, 30);
        editButton.setPreferredSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);
        editButton.setMaximumSize(buttonSize);
        deleteButton.setMaximumSize(buttonSize);

        // Add buttons with spacing
        add(editButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(deleteButton);
        add(Box.createVerticalGlue()); // Prevent vertical stretching
    }

    // --- Getters ---
    public JButton getEditButton() { return editButton; }
    public JButton getDeleteButton() { return deleteButton; }
}
