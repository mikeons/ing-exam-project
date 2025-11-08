package com.zhou.movies.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * A dedicated panel for contextual actions like Edit and Delete.
 * Demonstrates UI-level Separation of Concerns (SoC).
 */
public class ActionPanel extends JPanel {

    private final JButton editButton;
    private final JButton deleteButton;

    public ActionPanel() {
        // Create a titled border with padding
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

        // Set consistent button size
        Dimension buttonSize = new Dimension(150, 30);
        editButton.setPreferredSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);
        editButton.setMaximumSize(buttonSize);
        deleteButton.setMaximumSize(buttonSize);

        // Add buttons with spacing
        add(editButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(deleteButton);

        // Prevent vertical stretching
        add(Box.createVerticalGlue());
    }

    // --- Getters ---
    public JButton getEditButton() {
        return editButton;
    }

    public JButton getDeleteButton() {
        return deleteButton;
    }
}
