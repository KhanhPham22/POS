package ui;

import model.Category;
import service.CategoryService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * CategoryPanel displays a list of category buttons and optionally includes CRUD operations
 * for managing categories. Clicking on a category will notify a listener.
 */
public class CategoryPanel extends JPanel {

    private final CategoryService categoryService;
    private final OnCategoryClickListener listener;
    private final boolean showCrudButtons;

    /**
     * Listener interface to notify when a category is selected.
     */
    public interface OnCategoryClickListener {
        void onCategorySelected(Category category);
    }

    /**
     * Constructor to initialize the CategoryPanel.
     *
     * @param categoryService   Service used to fetch and manage categories
     * @param listener          Callback when a category is selected
     * @param showCrudButtons   Whether to show CRUD buttons (Add, Edit, Delete)
     */
    public CategoryPanel(CategoryService categoryService, OnCategoryClickListener listener, boolean showCrudButtons) {
        this.categoryService = categoryService;
        this.listener = listener;
        this.showCrudButtons = showCrudButtons;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        loadCategoryButtons(); // Load category buttons into the panel

        if (showCrudButtons) {
            add(createCrudPanel(), BorderLayout.SOUTH); // Add CRUD buttons if required
        }
    }

    // Load category buttons from the service
    private void loadCategoryButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        List<Category> categories = categoryService.getAllCategories(1, Integer.MAX_VALUE); // Get all categories

        if (categories == null || categories.isEmpty()) {
            buttonPanel.add(new JLabel("No categories available"));
        } else {
            for (Category category : categories) {
                JButton button = createCategoryButton(category);
                buttonPanel.add(button);
            }
        }

        add(new JScrollPane(buttonPanel), BorderLayout.CENTER);
    }

    // Create a styled button for a given category
    private JButton createCategoryButton(Category category) {
        JButton button = new JButton(category.getName());
        button.setBackground(new Color(70, 130, 180)); // Steel Blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.addActionListener(e -> listener.onCategorySelected(category));

        return button;
    }

    // Create the CRUD control panel with Add, Edit, Delete buttons
    private JPanel createCrudPanel() {
        JPanel crudPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        crudPanel.setBackground(Color.WHITE);

        JButton addButton = createCrudButton("Add", new Color(46, 125, 50));
        addButton.addActionListener(this::handleAddCategory);

        JButton editButton = createCrudButton("Edit", new Color(237, 108, 0));
        editButton.addActionListener(this::handleEditCategory);

        JButton deleteButton = createCrudButton("Delete", new Color(198, 40, 40));
        deleteButton.addActionListener(this::handleDeleteCategory);

        crudPanel.add(addButton);
        crudPanel.add(editButton);
        crudPanel.add(deleteButton);

        return crudPanel;
    }

    // Helper to create styled CRUD buttons
    private JButton createCrudButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(80, 30));
        return button;
    }

    // Handler for adding a new category
    private void handleAddCategory(ActionEvent e) {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JLabel label = new JLabel("Enter new category name:");
        JTextField textField = new JTextField(20);
        panel.add(label);
        panel.add(textField);

        Object[] options = {
            createDialogButton("Confirm", new Color(46, 125, 50)),
            createDialogButton("Cancel", new Color(198, 40, 40))
        };

        int result = JOptionPane.showOptionDialog(this, panel, "Add Category", JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, UIManager.getIcon("OptionPane.informationIcon"), options, options[0]);

        if (result == 0) {
            String name = textField.getText().trim();
            if (!name.isEmpty()) {
                try {
                    Category newCategory = new Category();
                    newCategory.setName(name);
                    categoryService.createCategory(newCategory);
                    showMessageDialog("Category added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshPanel();
                } catch (Exception ex) {
                    showMessageDialog("Failed to add category: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                showMessageDialog("Category name cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // Handler for editing an existing category
    private void handleEditCategory(ActionEvent e) {
        JTextField inputField = new JTextField();
        Object[] message = { "Enter the category name to edit:", inputField };
        Object[] options = {
            createDialogButton("Continue", new Color(237, 108, 0)),
            createDialogButton("Cancel", new Color(198, 40, 40))
        };

        int result = JOptionPane.showOptionDialog(this, message, "Edit Category", JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (result == 0) {
            String oldName = inputField.getText().trim();
            if (!oldName.isEmpty()) {
                Category category = categoryService.getCategoryByName(oldName);
                if (category != null) {
                    JTextField newNameField = new JTextField(category.getName());
                    Object[] editMessage = { "Enter new name for the category:", newNameField };
                    Object[] editOptions = {
                        createDialogButton("Save", new Color(46, 125, 50)),
                        createDialogButton("Cancel", new Color(198, 40, 40))
                    };

                    int editResult = JOptionPane.showOptionDialog(this, editMessage, "Edit Category: " + oldName,
                            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, editOptions, editOptions[0]);

                    if (editResult == 0) {
                        String newName = newNameField.getText().trim();
                        if (!newName.isEmpty()) {
                            try {
                                category.setName(newName);
                                categoryService.updateCategory(category);
                                showMessageDialog("Category updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                                refreshPanel();
                            } catch (Exception ex) {
                                showMessageDialog("Failed to update category: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            showMessageDialog("Category name cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                } else {
                    showMessageDialog("Category not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                showMessageDialog("Category name cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // Handler for deleting a category
    private void handleDeleteCategory(ActionEvent e) {
        JTextField inputField = new JTextField();
        Object[] message = { "Enter the category name to delete:", inputField };
        Object[] options = {
            createDialogButton("Continue", new Color(237, 108, 0)),
            createDialogButton("Cancel", new Color(198, 40, 40))
        };

        int result = JOptionPane.showOptionDialog(this, message, "Delete Category", JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE, null, options, options[0]);

        if (result == 0) {
            String name = inputField.getText().trim();
            if (!name.isEmpty()) {
                Category category = categoryService.getCategoryByName(name);
                if (category != null) {
                    Object[] confirmOptions = {
                        createDialogButton("Delete", new Color(198, 40, 40)),
                        createDialogButton("Cancel", new Color(100, 100, 100))
                    };

                    int confirm = JOptionPane.showOptionDialog(this,
                            "Are you sure you want to delete the category '" + name + "'?\nThis will disassociate all products from this category.",
                            "Confirm Deletion", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                            null, confirmOptions, confirmOptions[1]);

                    if (confirm == 0) {
                        try {
                            categoryService.deleteCategory(category);
                            showMessageDialog("Category deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            refreshPanel();
                        } catch (Exception ex) {
                            showMessageDialog("Failed to delete category: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    showMessageDialog("Category not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                showMessageDialog("Category name cannot be empty!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    // Helper to create buttons used inside dialogs
    private JButton createDialogButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(100, 30));
        return button;
    }

    // Utility method to show a message dialog
    private void showMessageDialog(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

    // Reload the entire panel UI after a CRUD operation
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
