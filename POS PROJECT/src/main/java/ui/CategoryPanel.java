package ui;

import model.Category;
import service.CategoryService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CategoryPanel extends JPanel {

    private final CategoryService categoryService;
    private final String iconPath;
    private final OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategorySelected(Category category);
    }

    public CategoryPanel(CategoryService categoryService, String iconPath, OnCategoryClickListener listener) {
        this.categoryService = categoryService;
        this.iconPath = iconPath;
        this.listener = listener;
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setBackground(Color.WHITE);
        //refreshCategories(); // Load lần đầu
    }

//    public void refreshCategories() {
//        removeAll(); // Xoá các button cũ
//        List<Category> categories = categoryService.getAllCategories();
//        if (categories == null || categories.isEmpty()) {
//            add(new JLabel("No categories available"));
//            revalidate();
//            repaint();
//            return;
//        }
//
//        for (Category category : categories) {
//            JButton button = createCategoryButton(category);
//            add(button);
//        }
//
//        revalidate();
//        repaint();
//    }

    private JButton createCategoryButton(Category category) {
        JButton button;
        String iconFileName = category.getName().toLowerCase() + ".png"; // Ví dụ: "Coffee" → "coffee.png"
        try {
            ImageIcon icon = new ImageIcon(iconPath + iconFileName);
            if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                throw new Exception("Icon not found");
            }
            Image scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            button = new JButton(new ImageIcon(scaledImage));
        } catch (Exception e) {
            System.err.println("Could not load icon for category: " + category.getName());
            button = new JButton(category.getName());
        }

        button.setToolTipText(category.getName());
        button.setBackground(Color.LIGHT_GRAY);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.onCategorySelected(category);
            }
        });

        return button;
    }
}
