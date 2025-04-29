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
    private final OnCategoryClickListener listener;
    private final boolean showCrudButtons;

    public interface OnCategoryClickListener {
        void onCategorySelected(Category category);
    }

    public CategoryPanel(CategoryService categoryService, OnCategoryClickListener listener, boolean showCrudButtons) {
        this.categoryService = categoryService;
        this.listener = listener;
        this.showCrudButtons = showCrudButtons;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        loadCategoryButtons();

        if (showCrudButtons) {
            add(createCrudPanel(), BorderLayout.SOUTH);
        }
    }

    private void loadCategoryButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        List<Category> categories = categoryService.getAllCategories(1, Integer.MAX_VALUE); // Lấy hết

        if (categories == null || categories.isEmpty()) {
            buttonPanel.add(new JLabel("Không có danh mục nào"));
        } else {
            for (Category category : categories) {
                JButton button = createCategoryButton(category);
                buttonPanel.add(button);
            }
        }

        add(new JScrollPane(buttonPanel), BorderLayout.CENTER);
    }

    private JButton createCategoryButton(Category category) {
        JButton button = new JButton(category.getName());
        button.setBackground(new Color(70, 130, 180)); // Steel Blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.addActionListener(e -> listener.onCategorySelected(category));

        return button;
    }

    private JPanel createCrudPanel() {
        JPanel crudPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        crudPanel.setBackground(Color.WHITE);

        JButton addButton = createCrudButton("Thêm");
        addButton.addActionListener(this::handleAddCategory);

        JButton editButton = createCrudButton("Sửa");
        editButton.addActionListener(this::handleEditCategory);

        JButton deleteButton = createCrudButton("Xoá");
        deleteButton.addActionListener(this::handleDeleteCategory);

        crudPanel.add(addButton);
        crudPanel.add(editButton);
        crudPanel.add(deleteButton);

        return crudPanel;
    }

    private JButton createCrudButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        return button;
    }

    private void handleAddCategory(ActionEvent e) {
        String name = JOptionPane.showInputDialog(this, "Nhập tên danh mục mới:");
        if (name != null && !name.trim().isEmpty()) {
            Category newCategory = new Category();
            newCategory.setName(name.trim());
            boolean success = categoryService.createCategory(newCategory);
            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm danh mục thành công!");
                refreshPanel();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm danh mục thất bại.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleEditCategory(ActionEvent e) {
        String oldName = JOptionPane.showInputDialog(this, "Nhập tên danh mục cần sửa:");
        if (oldName != null && !oldName.trim().isEmpty()) {
            Category category = categoryService.getCategoryByName(oldName.trim());
            if (category != null) {
                String newName = JOptionPane.showInputDialog(this, "Nhập tên mới:");
                if (newName != null && !newName.trim().isEmpty()) {
                    category.setName(newName.trim());
                    boolean success = categoryService.updateCategory(category);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Cập nhật danh mục thành công!");
                        refreshPanel();
                    } else {
                        JOptionPane.showMessageDialog(this, "Cập nhật danh mục thất bại.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy danh mục.");
            }
        }
    }

    private void handleDeleteCategory(ActionEvent e) {
        String name = JOptionPane.showInputDialog(this, "Nhập tên danh mục cần xoá:");
        if (name != null && !name.trim().isEmpty()) {
            Category category = categoryService.getCategoryByName(name.trim());
            if (category != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xoá danh mục này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = categoryService.deleteCategory(category);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Xoá danh mục thành công!");
                        refreshPanel();
                    } else {
                        JOptionPane.showMessageDialog(this, "Xoá danh mục thất bại.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy danh mục.");
            }
        }
    }

    private void refreshPanel() {
        removeAll();
        loadCategoryButtons();
        if (showCrudButtons) {
            add(createCrudPanel(), BorderLayout.SOUTH);
        }
        revalidate();
        repaint();
    }
}
