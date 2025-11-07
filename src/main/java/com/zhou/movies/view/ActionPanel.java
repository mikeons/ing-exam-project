package com.zhou.movies.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * [V6.1] A dedicated panel for contextual actions like Edit and Delete.
 * This demonstrates UI-level Separation of Concerns (SoC).
 */
public class ActionPanel extends JPanel {

    private final JButton editButton;
    private final JButton deleteButton;

    public ActionPanel() {
        // 1. 设置带标题的边框
        setBorder(BorderFactory.createTitledBorder(
                new EmptyBorder(10, 10, 10, 10), // Padding
                "Actions",
                TitledBorder.LEFT,
                TitledBorder.TOP
        ));

        // 2. 使用 BoxLayout (Y_AXIS) 来垂直堆叠按钮
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // 3. 初始化按钮
        editButton = new JButton("Edit Movie ✍️");
        deleteButton = new JButton("Delete Movie 🗑️");

        // (可选) 统一按钮大小
        Dimension buttonSize = new Dimension(150, 30);
        editButton.setPreferredSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);
        editButton.setMaximumSize(buttonSize);
        deleteButton.setMaximumSize(buttonSize);

        // 4. 添加按钮
        add(editButton);
        add(Box.createRigidArea(new Dimension(0, 10))); // 按钮间的垂直间距
        add(deleteButton);

        // 确保面板不会在垂直方向上被拉伸
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