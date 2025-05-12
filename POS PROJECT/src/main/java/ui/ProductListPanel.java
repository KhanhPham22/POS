package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import model.Category;
import model.Product;
import service.ProductService;
import service.CategoryService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;
import ui.Elements.SearchBar;
import javax.imageio.ImageIO;

public class ProductListPanel extends JPanel {

	private final ProductService productService;
	private final CategoryService categoryService;
	private JComboBox<Object> categoryComboBox;
	private JTable productTable;
	private DefaultTableModel tableModel;
	private JButton addButton;
	private JButton categoryButton;
	private JLabel pageInfoLabel;
	private JButton prevButton, nextButton;
	private SearchBar searchBar;

	private int currentPage = 1;
	private int pageSize = 10;
	private int totalRecords = 0;
	private String currentSearchQuery = "";

	private static final String BARCODE_DIR = "C:\\TTTN\\POS PROJECT\\img\\Barcode\\";
	private static final String PRODUCT_IMG_DIR = "C:\\TTTN\\POS PROJECT\\img\\Product\\";
	private static final String[] SIZE_OPTIONS = { "S", "M", "L", "Default" };
	private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#,###.000 VND"); //handle decimal

	public ProductListPanel(CategoryService categoryService, ProductService productService) {
		this.productService = productService;
		this.categoryService = categoryService;
		initializeUI();
		loadProductData();
	}

	private void initializeUI() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		createDirectories();

		JPanel topPanel = new JPanel(new BorderLayout(10, 0));
		topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		List<Category> categories = loadCategories();
		categoryComboBox = new JComboBox<>();
		categoryComboBox.addItem("All");
		for (Category category : categories) {
			categoryComboBox.addItem(category);
		}
		categoryComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		categoryComboBox.setBackground(Color.WHITE);
		categoryComboBox.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
		categoryComboBox.setPreferredSize(new Dimension(200, 40));
		categoryComboBox.addActionListener(e -> {
			currentSearchQuery = "";
			currentPage = 1;
			loadProductData();
		});
		categoryPanel.add(new JLabel("Category:"));
		categoryPanel.add(categoryComboBox); //combo box

		categoryButton = new JButton("Manage Categories"); //category button
		categoryButton.setBackground(new Color(70, 130, 180));
		categoryButton.setForeground(Color.WHITE);
		categoryButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
		categoryButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		categoryButton.addActionListener(e -> showCategoryManagementDialog());
		categoryPanel.add(categoryButton);

		searchBar = new SearchBar(query -> { //search bar
			currentSearchQuery = query;
			currentPage = 1;
			loadProductData();
		});
		searchBar.setPlaceholder("Search by product name");
		searchBar.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		searchBar.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
						BorderFactory.createEmptyBorder(8, 12, 8, 12)));
		searchBar.setBackground(new Color(245, 245, 245));
		searchBar.setPreferredSize(new Dimension(400, 40));

		addButton = new JButton("+ Add Product"); //add product button
		addButton.setBackground(new Color(50, 205, 50));
		addButton.setForeground(Color.WHITE);
		addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
		addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		addButton.addActionListener(e -> showAddProductDialog());

		topPanel.add(categoryPanel, BorderLayout.WEST);
		topPanel.add(searchBar, BorderLayout.CENTER);
		topPanel.add(addButton, BorderLayout.EAST);
		add(topPanel, BorderLayout.NORTH);

		String[] columnNames = { "Image", "Barcode", "Name", "Category", "Size", "Price", "Quantity", "Status",
				"Actions" };
		tableModel = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 8;
			}
		};

		productTable = new JTable(tableModel); //product table
		productTable.setFillsViewportHeight(true);
		productTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		productTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
		productTable.setRowHeight(61);

		productTable.getColumnModel().getColumn(0).setCellRenderer(new ImageCellRenderer());
		productTable.getColumnModel().getColumn(1).setCellRenderer(new BarcodeCellRenderer());
		productTable.getColumnModel().getColumn(5).setCellRenderer(new PriceCellRenderer());
		productTable.getColumnModel().getColumn(7).setCellRenderer(new StatusCellRenderer());
		productTable.getColumnModel().getColumn(8).setCellRenderer(new ActionCellRenderer());
		productTable.getColumnModel().getColumn(8).setCellEditor(new ActionCellEditor());

		productTable.getColumnModel().getColumn(0).setPreferredWidth(170); // image
		productTable.getColumnModel().getColumn(1).setPreferredWidth(170); // barcode
		productTable.getColumnModel().getColumn(2).setPreferredWidth(170); // name
		productTable.getColumnModel().getColumn(3).setPreferredWidth(150); // category
		productTable.getColumnModel().getColumn(4).setPreferredWidth(110); // size
		productTable.getColumnModel().getColumn(5).setPreferredWidth(120); // price
		productTable.getColumnModel().getColumn(6).setPreferredWidth(120); // quantity
		productTable.getColumnModel().getColumn(7).setPreferredWidth(150); // status
		productTable.getColumnModel().getColumn(8).setPreferredWidth(250); // action

		JScrollPane scrollPane = new JScrollPane(productTable);
		add(scrollPane, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

		JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); //pagination panel
		prevButton = new JButton("Previous");
		prevButton.setBackground(new Color(70, 130, 180));
		prevButton.setForeground(Color.WHITE);
		prevButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		prevButton.addActionListener(e -> {
			if (currentPage > 1) {
				currentPage--;
				loadProductData();
			}
		});

		nextButton = new JButton("Next");
		nextButton.setBackground(new Color(70, 130, 180));
		nextButton.setForeground(Color.WHITE);
		nextButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		nextButton.addActionListener(e -> {
			if (currentPage * pageSize < totalRecords) {
				currentPage++;
				loadProductData();
			}
		});

		pageInfoLabel = new JLabel();
		pageInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

		paginationPanel.add(prevButton);
		paginationPanel.add(pageInfoLabel);
		paginationPanel.add(nextButton);
		bottomPanel.add(paginationPanel, BorderLayout.CENTER);

		add(bottomPanel, BorderLayout.SOUTH);
	}

	private void createDirectories() { //create directory to handle file
		try {
			Files.createDirectories(Paths.get(BARCODE_DIR));
			Files.createDirectories(Paths.get(PRODUCT_IMG_DIR));
		} catch (IOException e) {
			showError("Error creating directories: " + e.getMessage());
		}
	}

	private class PriceCellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
					column);
			if (value instanceof BigDecimal) {
				label.setText(PRICE_FORMAT.format(value));
			}
			label.setHorizontalAlignment(SwingConstants.RIGHT);
			return label;
		}
	}

	private class ImageCellRenderer extends DefaultTableCellRenderer { //image cell render
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			JLabel label = new JLabel();
			if (value instanceof ImageIcon) {
				label.setIcon((ImageIcon) value);
			}
			label.setHorizontalAlignment(SwingConstants.CENTER);
			return label;
		}
	}

	private class BarcodeCellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			JLabel label = new JLabel();
			if (value instanceof ImageIcon) {
				label.setIcon((ImageIcon) value);
			}
			label.setHorizontalAlignment(SwingConstants.CENTER);
			return label;
		}
	}

	private class StatusCellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			JLabel label = (JLabel) c;

			if ("In Stock".equals(value)) {
				label.setBackground(new Color(144, 238, 144));
				label.setForeground(Color.BLACK);
				label.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 1));
			} else {
				label.setBackground(new Color(255, 182, 193));
				label.setForeground(Color.BLACK);
				label.setBorder(BorderFactory.createLineBorder(new Color(139, 0, 0), 1));
			}
			label.setHorizontalAlignment(SwingConstants.CENTER);
			return c;
		}
	}

	private class ActionCellRenderer implements TableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			return createActionPanel(row);
		}
	}

	private class ActionCellEditor extends AbstractCellEditor implements TableCellEditor { //edit action cell to handle edit,detail,delete button
		private JPanel panel;

		@Override
		public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
				int column) {
			panel = createActionPanel(row);
			return panel;
		}

		@Override
		public Object getCellEditorValue() {
			return "";
		}
	}

	private JPanel createActionPanel(int row) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.setBackground(Color.WHITE);

		JButton editButton = new JButton("Edit");
		editButton.setBackground(new Color(255, 165, 0));
		editButton.setForeground(Color.WHITE);
		editButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		editButton.setPreferredSize(new Dimension(70, 50));
		editButton.addActionListener(e -> {
			int modelRow = productTable.convertRowIndexToModel(row);
			String name = (String) tableModel.getValueAt(modelRow, 2);
			String size = (String) tableModel.getValueAt(modelRow, 4);
			editProduct(modelRow, name, size);
		});

		JButton deleteButton = new JButton("Delete");
		deleteButton.setBackground(new Color(255, 0, 0));
		deleteButton.setForeground(Color.WHITE);
		deleteButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		deleteButton.setPreferredSize(new Dimension(70, 50));
		deleteButton.addActionListener(e -> {
			int modelRow = productTable.convertRowIndexToModel(row);
			String name = (String) tableModel.getValueAt(modelRow, 2);
			String size = (String) tableModel.getValueAt(modelRow, 4);
			int confirm = JOptionPane.showConfirmDialog(this,
					"Are you sure you want to delete " + name + " (Size: " + size + ")?", "Confirm Delete",
					JOptionPane.YES_NO_OPTION);
			if (confirm == JOptionPane.YES_OPTION) {
				try {
					Product product = productService.getProductByNameAndSize(name, size);
					if (product != null) {
						productService.deleteProduct(product);
						tableModel.removeRow(modelRow);
						updatePaginationInfo();
						productTable.revalidate();
						productTable.repaint();
					}
				} catch (Exception ex) {
					showError("Error deleting product: " + ex.getMessage());
				}
			}
		});

		JButton detailButton = new JButton("Detail");
		detailButton.setBackground(new Color(70, 130, 180));
		detailButton.setForeground(Color.WHITE);
		detailButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		detailButton.setPreferredSize(new Dimension(70, 50));
		detailButton.addActionListener(e -> {
			int modelRow = productTable.convertRowIndexToModel(row);
			String name = (String) tableModel.getValueAt(modelRow, 2);
			String size = (String) tableModel.getValueAt(modelRow, 4);
			showProductDetail(modelRow, name, size);
		});

		panel.add(editButton);
		panel.add(deleteButton);
		panel.add(detailButton);
		return panel;
	}

	private void loadProductData() { //load category and product from db
	    try {
	        tableModel.setRowCount(0);
	        Object selectedItem = categoryComboBox.getSelectedItem();
	        Category selectedCategory = selectedItem instanceof Category ? (Category) selectedItem : null;
	        List<Product> products = productService.getAllProducts(currentPage, pageSize);
	        totalRecords = productService.getAllProducts(1, Integer.MAX_VALUE).size();

	        products = products.stream()
	                .filter(p -> {
	                    if (selectedItem == null || "All".equals(selectedItem)) {
	                        return true; // Include all products if "All" or null is selected
	                    }
	                    return selectedCategory != null && p.getCategory() != null && 
	                           selectedCategory.getId() == p.getCategory().getId();
	                })
	                .filter(p -> currentSearchQuery.isEmpty() || 
	                           (p.getName() != null && p.getName().toLowerCase().contains(currentSearchQuery.toLowerCase())))
	                .collect(Collectors.toList());

	        for (Product p : products) {
	            ImageIcon imageIcon = null;
	            ImageIcon barcodeIcon = null;
	            try {
	                if (p.getImagePath() != null && !p.getImagePath().isEmpty()) {
	                    imageIcon = new ImageIcon(new ImageIcon(p.getImagePath()).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
	                }
	                String barcodePath = BARCODE_DIR + p.getName() + "_" + p.getSize() + ".png";
	                if (Files.exists(Paths.get(barcodePath))) {
	                    barcodeIcon = new ImageIcon(new ImageIcon(barcodePath).getImage().getScaledInstance(150, 50, Image.SCALE_SMOOTH));
	                } else {
	                    barcodeIcon = generateAndSaveBarcode(p);
	                }
	            } catch (WriterException | IOException e) {
	                e.printStackTrace();
	                barcodeIcon = new ImageIcon();
	            }
	            tableModel.addRow(new Object[]{
	                    imageIcon,
	                    barcodeIcon,
	                    p.getName(),
	                    p.getCategory() != null ? p.getCategory().getName() : "N/A",
	                    p.getSize(),
	                    p.getPrice(),
	                    p.getQuantity(),
	                    p.getQuantity() > 0 ? "In Stock" : "Out of Stock",
	                    ""
	            });
	        }

	        updatePaginationInfo();
	        productTable.revalidate();
	        productTable.repaint();
	    } catch (Exception e) {
	        showError("Error loading product data: " + e.getMessage());
	    }
	}

	private List<Category> loadCategories() {
		try {
			return categoryService.getAllCategories(1, Integer.MAX_VALUE);
		} catch (Exception e) {
			showError("Error loading categories: " + e.getMessage());
			return List.of();
		}
	}

	private void updatePaginationInfo() { //update pagination
		int start = (currentPage - 1) * pageSize + 1;
		int end = Math.min(currentPage * pageSize, totalRecords);
		int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
		pageInfoLabel.setText(String.format("Page %d of %d (%d products)", currentPage, totalPages, totalRecords));
		prevButton.setEnabled(currentPage > 1);
		nextButton.setEnabled(currentPage * pageSize < totalRecords);
	}

	 private void showAddProductDialog() { //show add product dialog to add new product
	        JDialog dialog = createDialog("Add New Product", 400, 450);
	        JTextField nameField = new JTextField();
	        JTextField priceField = new JTextField();
	        JTextField quantityField = new JTextField();
	        JComboBox<String> sizeCombo = new JComboBox<>(SIZE_OPTIONS);
	        JTextField imagePathField = new JTextField();
	        imagePathField.setEditable(false);
	        imagePathField.setBackground(new Color(245, 245, 245));
	        JComboBox<Category> categoryCombo = new JComboBox<>(loadCategories().toArray(new Category[0]));
	        JButton selectImageBtn = new JButton("Select Image");

	        selectImageBtn.setBackground(new Color(50, 205, 50));
	        selectImageBtn.setForeground(Color.WHITE);
	        selectImageBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
	        selectImageBtn.setPreferredSize(new Dimension(120, 30));

	        selectImageBtn.addActionListener(e -> {
	            JFileChooser fileChooser = new JFileChooser();
	            fileChooser.setFileFilter(
	                    new javax.swing.filechooser.FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));
	            fileChooser.setDialogTitle("Select Product Image");
	            if (fileChooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
	                File selectedFile = fileChooser.getSelectedFile();
	                imagePathField.setText(selectedFile.getAbsolutePath());
	            }
	        });

	        JPanel imagePanel = new JPanel(new BorderLayout(5, 0));
	        imagePanel.add(imagePathField, BorderLayout.CENTER);
	        imagePanel.add(selectImageBtn, BorderLayout.EAST);

	        addDialogFields(dialog, new String[] { "Name:", "Price (VND):", "Quantity:", "Size:", "Image:", "Category:" },
	                new JComponent[] { nameField, priceField, quantityField, sizeCombo, imagePanel, categoryCombo });

	        JButton saveButton = new JButton("Save");
	        saveButton.addActionListener(e -> {
	            try {
	                Product product = new Product();
	                String productName = nameField.getText().trim();
	                String size = (String) sizeCombo.getSelectedItem();

	                if (productService.existsByNameAndSize(productName, size)) {
	                    showError("A product with the same name and size already exists.");
	                    return;
	                }

	                product.setName(productName);
	                product.setPrice(new BigDecimal(priceField.getText().trim()));
	                product.setQuantity(Integer.parseInt(quantityField.getText().trim()));
	                product.setSize(size);
	                String originalImagePath = imagePathField.getText().trim();
	                String newImagePath = saveProductImage(productName, originalImagePath, size);
	                product.setImagePath(newImagePath);
	                Category selectedCategory = (Category) categoryCombo.getSelectedItem();
	                product.setCategory(selectedCategory);

	                if (productService.createProduct(product)) {
	                    String ean13 = generateNumericEAN13(product.getId());
	                    product.setEan13(ean13);
	                    productService.updateProduct(product);
	                    if (selectedCategory != null) {
	                        categoryService.addProductToCategory(selectedCategory, product);
	                    }
	                    generateAndSaveBarcode(product);
	                    loadProductData();
	                    dialog.dispose();
	                } else {
	                    showError("Failed to add product");
	                }
	            } catch (Exception ex) {
	                showError("Error adding product: " + ex.getMessage());
	            }
	        });

	        addDialogButtons(dialog, saveButton);
	    }
   
	 //save product img to folder
	private String saveProductImage(String productName, String originalImagePath, String size) throws IOException {
		if (originalImagePath == null || originalImagePath.isEmpty()) {
			return "";
		}
		Path sourcePath = Paths.get(originalImagePath);
		String extension = originalImagePath.substring(originalImagePath.lastIndexOf("."));
		String newFileName = productName + "_" + size + extension;

		Path targetPath = Paths.get(PRODUCT_IMG_DIR, newFileName);
		Files.copy(sourcePath, targetPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		return targetPath.toString();
	}

	private void editProduct(int row, String name, String size) { //handle edit product
	    try {
	        Product product = productService.getProductByNameAndSize(name, size);
	        if (product == null) throw new Exception("Product not found");

	        JDialog dialog = createDialog("Edit Product", 400, 450);
	        JTextField nameField = new JTextField(product.getName());
	        JTextField priceField = new JTextField(product.getPrice().toString());
	        JTextField quantityField = new JTextField(String.valueOf(product.getQuantity()));
	        JTextField sizeField = new JTextField(product.getSize() != null ? product.getSize() : "Default");
	        sizeField.setEditable(false);
	        sizeField.setBackground(new Color(245, 245, 245));
	        JTextField imagePathField = new JTextField(product.getImagePath());
	        imagePathField.setEditable(false);
	        imagePathField.setBackground(new Color(245, 245, 245));
	        JComboBox<Category> categoryCombo = new JComboBox<>(loadCategories().toArray(new Category[0]));
	        categoryCombo.setSelectedItem(product.getCategory());
	        JButton selectImageBtn = new JButton("Select Image");

	        selectImageBtn.setBackground(new Color(50, 205, 50));
	        selectImageBtn.setForeground(Color.WHITE);
	        selectImageBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
	        selectImageBtn.setPreferredSize(new Dimension(120, 30));

	        selectImageBtn.addActionListener(e -> {
	            JFileChooser fileChooser = new JFileChooser();
	            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
	                    "Image files", "jpg", "jpeg", "png", "gif"));
	            fileChooser.setDialogTitle("Select Product Image");
	            if (fileChooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
	                imagePathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
	            }
	        });

	        JPanel imagePanel = new JPanel(new BorderLayout(5, 0));
	        imagePanel.add(imagePathField, BorderLayout.CENTER);
	        imagePanel.add(selectImageBtn, BorderLayout.EAST);

	        addDialogFields(dialog,
	                new String[]{"Name:", "Price (VND):", "Quantity:", "Size:", "Image:", "Category:"},
	                new JComponent[]{nameField, priceField, quantityField, sizeField, imagePanel, categoryCombo});

	        JButton saveButton = new JButton("Save");
	        saveButton.addActionListener(e -> { //save button to save edit product have made
	            try {
	                String productName = nameField.getText().trim();
	                String currentSize = product.getSize();

	                if (!productName.equalsIgnoreCase(product.getName()) && 
	                    productService.existsByNameAndSize(productName, currentSize)) {
	                    showError("A product with the same name and size already exists.");
	                    return;
	                }

	                product.setName(productName);
	                product.setPrice(new BigDecimal(priceField.getText().trim()));
	                product.setQuantity(Integer.parseInt(quantityField.getText().trim()));
	                String originalImagePath = imagePathField.getText().trim();
	                String newImagePath = saveProductImage(productName, originalImagePath, product.getSize());
	                product.setImagePath(newImagePath);
	                Category newCategory = (Category) categoryCombo.getSelectedItem();
	                product.setCategory(newCategory); // Update the product's category directly

	                String ean13 = product.getEan13();
	                if (ean13 == null || !isValidEAN13Input(ean13)) {
	                    ean13 = generateNumericEAN13(product.getId());
	                    product.setEan13(ean13);
	                }

	                if (productService.updateProduct(product)) {
	                    generateAndSaveBarcode(product);
	                    loadProductData();
	                    dialog.dispose();
	                } else {
	                    showError("Failed to update product");
	                }
	            } catch (Exception ex) {
	                showError("Error updating product: " + ex.getMessage());
	            }
	        });

	        addDialogButtons(dialog, saveButton);
	    } catch (Exception e) {
	        showError("Error loading product data: " + e.getMessage());
	    }
	}

	private void showProductDetail(int row, String name, String size) { //handle to show information of product
        try {
            Product product = productService.getProductByNameAndSize(name, size);
            if (product == null)
                throw new Exception("Product not found");

            JDialog dialog = createDialog("Product Details", 400, 400);
            JTextField nameField = new JTextField(product.getName());
            JTextField priceField = new JTextField(PRICE_FORMAT.format(product.getPrice()));
            JTextField quantityField = new JTextField(String.valueOf(product.getQuantity()));
            JTextField sizeField = new JTextField(product.getSize());
            JTextField imagePathField = new JTextField(product.getImagePath());
            JTextField categoryField = new JTextField(product.getCategory() != null ? product.getCategory().getName() : "N/A");

            JComponent[] fields = { nameField, priceField, quantityField, sizeField, imagePathField, categoryField };
            for (JComponent field : fields) {
                if (field instanceof JTextField) {
                    ((JTextField) field).setEditable(false);
                }
                field.setBackground(new Color(245, 245, 245));
                field.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
            }

            addDialogFields(dialog,
                    new String[] { "Name:", "Price (VND):", "Quantity:", "Size:", "Image Path:", "Category:" }, fields);

            JButton backButton = new JButton("Back");
            backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            backButton.setBackground(new Color(220, 220, 220));
            backButton.addActionListener(e -> dialog.dispose());

            JPanel buttonPanel = (JPanel) dialog.getContentPane().getComponent(1);
            buttonPanel.add(backButton);

            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        } catch (Exception e) {
            showError("Error loading product details: " + e.getMessage());
        }
    }

	private void showCategoryManagementDialog() { //crud category
		JDialog dialog = createDialog("Manage Categories", 600, 400);
		DefaultTableModel categoryTableModel = new DefaultTableModel(new String[] { "Category Name" }, 0);
		JTable categoryTable = new JTable(categoryTableModel);
		JScrollPane scrollPane = new JScrollPane(categoryTable);
		scrollPane.setPreferredSize(new Dimension(550, 200));

		List<Category> categories = loadCategories();
		for (Category category : categories) {
			categoryTableModel.addRow(new Object[] { category.getName() });
		}

		JPanel contentPanel = (JPanel) dialog.getContentPane().getComponent(0);
		contentPanel.setLayout(new BorderLayout());
		contentPanel.add(scrollPane, BorderLayout.CENTER);

		JButton addButton = new JButton("Add"); //handle add category
		addButton.setBackground(new Color(50, 205, 50));
		addButton.addActionListener(e -> showAddCategoryDialog(dialog, categoryTableModel));

		JButton editButton = new JButton("Edit"); //handle edit category
		editButton.setBackground(new Color(255, 165, 0));
		editButton.addActionListener(e -> {
			int selectedRow = categoryTable.getSelectedRow();
			if (selectedRow >= 0) {
				String oldName = (String) categoryTableModel.getValueAt(selectedRow, 0);
				showEditCategoryDialog(dialog, categoryTableModel, selectedRow, oldName);
			} else {
				showError("Please select a category to edit");
			}
		});

		JButton deleteButton = new JButton("Delete"); //handle delete category
		deleteButton.setBackground(new Color(255, 0, 0));
		deleteButton.addActionListener(e -> {
			int selectedRow = categoryTable.getSelectedRow();
			if (selectedRow >= 0) {
				String name = (String) categoryTableModel.getValueAt(selectedRow, 0);
				Category category = categoryService.getCategoryByName(name);
				if (category != null) {
					int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + name + "?",
							"Confirm Delete", JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {
						if (categoryService.deleteCategory(category)) {
							categoryTableModel.removeRow(selectedRow);
							updateCategoryComboBox();
							loadProductData();
						} else {
							showError("Failed to delete category");
						}
					}
				} else {
					showError("Category not found");
				}
			} else {
				showError("Please select a category to delete");
			}
		});

		JPanel buttonPanel = (JPanel) dialog.getContentPane().getComponent(1);
		buttonPanel.add(addButton);
		buttonPanel.add(editButton);
		buttonPanel.add(deleteButton);

		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	private void showAddCategoryDialog(JDialog parentDialog, DefaultTableModel categoryTableModel) { //add category dialog 
	    JDialog dialog = createDialog("Add New Category", 300, 150);
	    JTextField nameField = new JTextField();
	    addDialogFields(dialog, new String[] { "Category Name:" }, new JComponent[] { nameField });
	    JButton saveButton = new JButton("Save");
	    saveButton.addActionListener(e -> {
	        String name = nameField.getText().trim();
	        if (!name.isEmpty()) {
	            try {
	                Category category = new Category();
	                category.setName(name);
	                categoryService.createCategory(category);
	                categoryTableModel.addRow(new Object[] { name });
	                updateCategoryComboBox();
	                loadProductData();
	                dialog.dispose();
	            } catch (Exception ex) {
	                showError("Failed to add category: " + ex.getMessage());
	            }
	        } else {
	            showError("Category name cannot be empty");
	        }
	    });
	    addDialogButtons(dialog, saveButton);
	}

	private void showEditCategoryDialog(JDialog parentDialog, DefaultTableModel categoryTableModel, int row, String oldName) {
	    JDialog dialog = createDialog("Edit Category", 300, 150);
	    JTextField nameField = new JTextField(oldName);
	    addDialogFields(dialog, new String[] { "Category Name:" }, new JComponent[] { nameField });
	    JButton saveButton = new JButton("Save");
	    saveButton.addActionListener(e -> {
	        String newName = nameField.getText().trim();
	        if (!newName.isEmpty()) {
	            try {
	                Category category = categoryService.getCategoryByName(oldName);
	                if (category != null) {
	                    category.setName(newName);
	                    categoryService.updateCategory(category);
	                    categoryTableModel.setValueAt(newName, row, 0);
	                    updateCategoryComboBox();
	                    loadProductData();
	                    dialog.dispose();
	                } else {
	                    showError("Category not found");
	                }
	            } catch (Exception ex) {
	                showError("Failed to update category: " + ex.getMessage());
	            }
	        } else {
	            showError("Category name cannot be empty");
	        }
	    });
	    addDialogButtons(dialog, saveButton);
	}

	private void updateCategoryComboBox() { //handle to update new cateogry to combo box
		Object selected = categoryComboBox.getSelectedItem();
		categoryComboBox.removeAllItems();
		categoryComboBox.addItem("All");
		for (Category category : loadCategories()) {
			categoryComboBox.addItem(category);
		}
		if (selected != null) {
			categoryComboBox.setSelectedItem(selected);
		}
	}

	private ImageIcon generateAndSaveBarcode(Product product) throws WriterException, IOException { //handle generate barcode
		String barcodeData = product.getEan13();

		if (barcodeData == null || !isValidEAN13Input(barcodeData)) {
			barcodeData = generateNumericEAN13(product.getId());
			product.setEan13(barcodeData);
			productService.updateProduct(product);
		}

		EAN13Writer barcodeWriter = new EAN13Writer();
		BitMatrix bitMatrix = barcodeWriter.encode(barcodeData, BarcodeFormat.EAN_13, 150, 50);
		Path barcodePath = Paths.get(BARCODE_DIR, product.getName() + "_" + product.getSize() + ".png");
		ImageIO.write(MatrixToImageWriter.toBufferedImage(bitMatrix), "PNG", barcodePath.toFile());
		return new ImageIcon(
				new ImageIcon(barcodePath.toString()).getImage().getScaledInstance(150, 50, Image.SCALE_SMOOTH));
	}

	private boolean isValidEAN13Input(String input) { //check EAN13 valid
		return input != null && input.matches("\\d{12,13}");
	}

	private String generateNumericEAN13(long productId) {
		String idStr = String.valueOf(productId);
		if (idStr.length() >= 12) {
			return idStr.substring(0, 12);
		} else {
			return String.format("%012d", productId);
		}
	}

	private JDialog createDialog(String title, int width, int height) {
		JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
		dialog.setSize(width, height);
		dialog.setLayout(new BorderLayout());
		dialog.getContentPane().setBackground(Color.WHITE);

		JPanel contentPanel = new JPanel(new GridLayout(0, 2, 10, 10));
		contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		contentPanel.setBackground(Color.WHITE);

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
		buttonPanel.setBackground(Color.WHITE);

		dialog.add(contentPanel, BorderLayout.CENTER);
		dialog.add(buttonPanel, BorderLayout.SOUTH);
		return dialog;
	}

	private void addDialogFields(JDialog dialog, String[] labels, JComponent[] components) {
		JPanel contentPanel = (JPanel) dialog.getContentPane().getComponent(0);
		for (int i = 0; i < labels.length; i++) {
			JLabel label = new JLabel(labels[i]);
			label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
			components[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
			if (components[i] instanceof JTextField) {
				components[i].setPreferredSize(new Dimension(200, 30));
				components[i].setBorder(
						BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)),
								BorderFactory.createEmptyBorder(5, 8, 5, 8)));
			} else if (components[i] instanceof JComboBox) {
				((JComboBox<?>) components[i]).setBackground(Color.WHITE);
				((JComboBox<?>) components[i]).setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
			} else if (components[i] instanceof JPanel) {
			}
			contentPanel.add(label);
			contentPanel.add(components[i]);
		}
	}

	private void addDialogButtons(JDialog dialog, JButton saveButton) {
		JPanel buttonPanel = (JPanel) dialog.getContentPane().getComponent(1);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cancelButton.setBackground(new Color(220, 220, 220));
		cancelButton.addActionListener(e -> dialog.dispose());

		saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
		saveButton.setBackground(new Color(70, 130, 180));
		saveButton.setForeground(Color.WHITE);

		buttonPanel.add(cancelButton);
		buttonPanel.add(saveButton);

		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);
	}

	private void showError(String message) { //show error
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}