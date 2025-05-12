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

		JButton addButton = createCrudButton("Thêm", new Color(46, 125, 50)); // Green
		addButton.addActionListener(this::handleAddCategory);

		JButton editButton = createCrudButton("Sửa", new Color(237, 108, 0)); // Orange
		editButton.addActionListener(this::handleEditCategory);

		JButton deleteButton = createCrudButton("Xoá", new Color(198, 40, 40)); // Red
		deleteButton.addActionListener(this::handleDeleteCategory);

		crudPanel.add(addButton);
		crudPanel.add(editButton);
		crudPanel.add(deleteButton);

		return crudPanel;
	}

	private JButton createCrudButton(String text, Color bgColor) {
		JButton button = new JButton(text);
		button.setBackground(bgColor);
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setPreferredSize(new Dimension(80, 30));
		return button;
	}

	private void handleAddCategory(ActionEvent e) {
	    JPanel panel = new JPanel(new GridLayout(0, 1));
	    JLabel label = new JLabel("Nhập tên danh mục mới:");
	    JTextField textField = new JTextField(20);
	    panel.add(label);
	    panel.add(textField);
	    Object[] options = { createDialogButton("Xác nhận", new Color(46, 125, 50)), createDialogButton("Hủy bỏ", new Color(198, 40, 40)) };
	    int result = JOptionPane.showOptionDialog(this, panel, "Thêm danh mục mới", JOptionPane.DEFAULT_OPTION,
	            JOptionPane.PLAIN_MESSAGE, UIManager.getIcon("OptionPane.informationIcon"), options, options[0]);
	    if (result == 0) {
	        String name = textField.getText().trim();
	        if (!name.isEmpty()) {
	            try {
	                Category newCategory = new Category();
	                newCategory.setName(name);
	                categoryService.createCategory(newCategory);
	                showMessageDialog("Thêm danh mục thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
	                refreshPanel();
	            } catch (Exception ex) {
	                showMessageDialog("Thêm danh mục thất bại: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	            }
	        } else {
	            showMessageDialog("Tên danh mục không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
	        }
	    }
	}

	private void handleEditCategory(ActionEvent e) {
	    JTextField inputField = new JTextField();
	    Object[] message = { "Nhập tên danh mục cần sửa:", inputField };
	    Object[] options = { createDialogButton("Tiếp tục", new Color(237, 108, 0)), createDialogButton("Hủy", new Color(198, 40, 40)) };
	    int result = JOptionPane.showOptionDialog(this, message, "Sửa danh mục", JOptionPane.DEFAULT_OPTION,
	            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
	    if (result == 0) {
	        String oldName = inputField.getText().trim();
	        if (!oldName.isEmpty()) {
	            Category category = categoryService.getCategoryByName(oldName);
	            if (category != null) {
	                JTextField newNameField = new JTextField(category.getName());
	                Object[] editMessage = { "Nhập tên mới cho danh mục:", newNameField };
	                Object[] editOptions = { createDialogButton("Lưu", new Color(46, 125, 50)), createDialogButton("Hủy", new Color(198, 40, 40)) };
	                int editResult = JOptionPane.showOptionDialog(this, editMessage, "Sửa danh mục: " + oldName,
	                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, editOptions, editOptions[0]);
	                if (editResult == 0) {
	                    String newName = newNameField.getText().trim();
	                    if (!newName.isEmpty()) {
	                        try {
	                            category.setName(newName);
	                            categoryService.updateCategory(category);
	                            showMessageDialog("Cập nhật danh mục thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
	                            refreshPanel();
	                        } catch (Exception ex) {
	                            showMessageDialog("Cập nhật danh mục thất bại: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	                        }
	                    } else {
	                        showMessageDialog("Tên danh mục không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
	                    }
	                }
	            } else {
	                showMessageDialog("Không tìm thấy danh mục.", "Lỗi", JOptionPane.ERROR_MESSAGE);
	            }
	        } else {
	            showMessageDialog("Tên danh mục không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
	        }
	    }
	}

	private void handleDeleteCategory(ActionEvent e) {
	    JTextField inputField = new JTextField();
	    Object[] message = { "Nhập tên danh mục cần xoá:", inputField };
	    Object[] options = { createDialogButton("Tiếp tục", new Color(237, 108, 0)), createDialogButton("Hủy", new Color(198, 40, 40)) };
	    int result = JOptionPane.showOptionDialog(this, message, "Xoá danh mục", JOptionPane.DEFAULT_OPTION,
	            JOptionPane.WARNING_MESSAGE, null, options, options[0]);
	    if (result == 0) {
	        String name = inputField.getText().trim();
	        if (!name.isEmpty()) {
	            Category category = categoryService.getCategoryByName(name);
	            if (category != null) {
	                Object[] confirmOptions = { createDialogButton("Xoá", new Color(198, 40, 40)), createDialogButton("Hủy", new Color(100, 100, 100)) };
	                int confirm = JOptionPane.showOptionDialog(this, "Bạn có chắc muốn xoá danh mục '" + name + "'? This will disassociate all products from this category.",
	                        "Xác nhận xoá", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
	                        confirmOptions, confirmOptions[1]);
	                if (confirm == 0) {
	                    try {
	                        categoryService.deleteCategory(category);
	                        showMessageDialog("Xoá danh mục thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
	                        refreshPanel();
	                    } catch (Exception ex) {
	                        showMessageDialog("Xoá danh mục thất bại: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	                    }
	                }
	            } else {
	                showMessageDialog("Không tìm thấy danh mục.", "Lỗi", JOptionPane.ERROR_MESSAGE);
	            }
	        } else {
	            showMessageDialog("Tên danh mục không được để trống!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
	        }
	    }
	}

	private JButton createDialogButton(String text, Color bgColor) {
		JButton button = new JButton(text);
		button.setBackground(bgColor);
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setPreferredSize(new Dimension(100, 30));
		return button;
	}

	private void showMessageDialog(String message, String title, int messageType) {
		JOptionPane.showMessageDialog(this, message, title, messageType);
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