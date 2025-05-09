package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
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

public class ProductListPanel extends JPanel {

    private final ProductService productService;
    private final CategoryService categoryService;
    private JComboBox<Category> categoryComboBox;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private CategoryPanel categoryPanel;
    private JTextField nameField;
    private JTextField priceField;
    private JTextField quantityField;
    private JTextField sizeField;
    private JTextField imagePathField;
    private SearchBar searchBar;

    public ProductListPanel(CategoryService categoryService, ProductService productService) {
        this.productService = productService;
        this.categoryService = categoryService;

        List<Category> categories;
        try {
            categories = categoryService.getAllCategories(1, Integer.MAX_VALUE);
        } catch (Exception e) {
            categories = List.of();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải danh mục: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel with category selection and search
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Category panel for CRUD operations
        JPanel categoryWrapper = new JPanel(new BorderLayout());
        categoryWrapper.setBackground(Color.WHITE);
        categoryWrapper.setBorder(BorderFactory.createTitledBorder("Danh mục"));
        categoryPanel = new CategoryPanel(categoryService, category -> {
            categoryComboBox.setSelectedItem(category);
            loadProducts();
        }, true);
        categoryWrapper.add(categoryPanel, BorderLayout.CENTER);
        topPanel.add(categoryWrapper, BorderLayout.WEST);

        // Search panel with SearchBar
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm sản phẩm"));
        searchBar = new SearchBar(query -> loadProducts());
        searchBar.setPlaceholder("Nhập tên sản phẩm...");
        searchPanel.add(searchBar, BorderLayout.CENTER);
        topPanel.add(searchPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Product table
        tableModel = new DefaultTableModel(new Object[]{"Tên", "Giá", "Số lượng", "Trạng thái", "Barcode"}, 0);
        productTable = new JTable(tableModel);
        productTable.getColumnModel().getColumn(4).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            if (value != null) {
                JLabel label = new JLabel();
                label.setIcon((ImageIcon) value);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                return label;
            }
            return new JLabel();
        });
        productTable.setRowHeight(50);
        productTable.setGridColor(new Color(200, 200, 200));
        productTable.setShowGrid(true);
        add(new JScrollPane(productTable), BorderLayout.CENTER);

        // Bottom panel for adding/editing products
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Add product form
        JPanel addProductPanel = new JPanel(new GridBagLayout());
        addProductPanel.setBackground(Color.WHITE);
        addProductPanel.setBorder(BorderFactory.createTitledBorder("Thêm sản phẩm"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Category selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        addProductPanel.add(new JLabel("Danh mục đồ uống:"), gbc);
        gbc.gridx = 1;
        categoryComboBox = new JComboBox<>(categories.toArray(new Category[0]));
        categoryComboBox.setPreferredSize(new Dimension(200, 30));
        addProductPanel.add(categoryComboBox, gbc);

        // Name field
        gbc.gridx = 0;
        gbc.gridy = 1;
        addProductPanel.add(new JLabel("Tên sản phẩm:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(20);
        nameField.setPreferredSize(new Dimension(200, 30));
        addProductPanel.add(nameField, gbc);

        // Price field
        gbc.gridx = 0;
        gbc.gridy = 2;
        addProductPanel.add(new JLabel("Giá (VND):"), gbc);
        gbc.gridx = 1;
        priceField = new JTextField(20);
        priceField.setPreferredSize(new Dimension(200, 30));
        addProductPanel.add(priceField, gbc);

        // Quantity field
        gbc.gridx = 0;
        gbc.gridy = 3;
        addProductPanel.add(new JLabel("Số lượng:"), gbc);
        gbc.gridx = 1;
        quantityField = new JTextField(20);
        quantityField.setPreferredSize(new Dimension(200, 30));
        addProductPanel.add(quantityField, gbc);

        // Size field
        gbc.gridx = 0;
        gbc.gridy = 4;
        addProductPanel.add(new JLabel("Kích cỡ:"), gbc);
        gbc.gridx = 1;
        sizeField = new JTextField(20);
        sizeField.setPreferredSize(new Dimension(200, 30));
        addProductPanel.add(sizeField, gbc);

        // Image path field
        gbc.gridx = 0;
        gbc.gridy = 5;
        addProductPanel.add(new JLabel("Đường dẫn ảnh:"), gbc);
        gbc.gridx = 1;
        imagePathField = new JTextField(20);
        imagePathField.setPreferredSize(new Dimension(200, 30));
        addProductPanel.add(imagePathField, gbc);

        // Choose image button
        gbc.gridx = 1;
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JButton chooseImageBtn = new JButton("Chọn ảnh");
        chooseImageBtn.setBackground(new Color(70, 130, 180));
        chooseImageBtn.setForeground(Color.WHITE);
        chooseImageBtn.setFocusPainted(false);
        chooseImageBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                imagePathField.setText(selectedFile.getAbsolutePath());
            }
        });
        addProductPanel.add(chooseImageBtn, gbc);

        bottomPanel.add(addProductPanel, BorderLayout.WEST);

        // Buttons for add/save/cancel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton addBtn = new JButton("Thêm sản phẩm");
        addBtn.setBackground(new Color(46, 125, 50));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.addActionListener(this::handleAddProduct);
        buttonPanel.add(addBtn);

        JButton saveBtn = new JButton("Lưu thay đổi");
        saveBtn.setBackground(new Color(237, 108, 0));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.addActionListener(e -> loadProducts());
        buttonPanel.add(saveBtn);

        JButton cancelBtn = new JButton("Hủy");
        cancelBtn.setBackground(new Color(198, 40, 40));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setFocusPainted(false);
        cancelBtn.addActionListener(e -> clearForm());
        buttonPanel.add(cancelBtn);

        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        // Initial load
        loadProducts();
    }

    private void handleAddProduct(ActionEvent e) {
        try {
            Product product = new Product();
            product.setName(nameField.getText().trim());
            product.setPrice(new BigDecimal(priceField.getText().trim()));
            product.setQuantity(Integer.parseInt(quantityField.getText().trim()));
            product.setSize(sizeField.getText().trim());
            product.setImagePath(imagePathField.getText().trim());
            Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
            product.setCategory(selectedCategory);

            boolean success = productService.createProduct(product);
            if (success) {
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                loadProducts();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số hợp lệ cho giá và số lượng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm sản phẩm: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        nameField.setText("");
        priceField.setText("");
        quantityField.setText("");
        sizeField.setText("");
        imagePathField.setText("");
    }

    private void loadProducts() {
        String rawKeyword = searchBar.getSearchText().toLowerCase();
        String keyword = rawKeyword.equals(searchBar.getPlaceholder()) ? "" : rawKeyword;
        Category selectedCategory = (Category) categoryComboBox.getSelectedItem();

        try {
            List<Product> allProducts = productService.getAllProducts(1, Integer.MAX_VALUE);

            // Filter products by category and keyword
            List<Product> filtered = allProducts.stream()
                    .filter(p -> selectedCategory == null || p.getCategory().getId() == selectedCategory.getId())
                    .filter(p -> keyword.isEmpty() || p.getName().toLowerCase().contains(keyword))
                    .sorted(Comparator.comparing(Product::getName))
                    .collect(Collectors.toList());

            // Display products
            tableModel.setRowCount(0);
            for (Product p : filtered) {
                ImageIcon barcodeIcon = null;
                try {
                    barcodeIcon = generateBarcode(p.getName());
                } catch (WriterException e) {
                    e.printStackTrace();
                    barcodeIcon = null;
                }
                tableModel.addRow(new Object[]{
                    p.getName(),
                    p.getPrice() + "đ",
                    p.getQuantity(),
                    (p.getQuantity() > 0 ? "Còn hàng" : "Hết hàng"),
                    barcodeIcon
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải sản phẩm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ImageIcon generateBarcode(String data) throws WriterException {
        EAN13Writer barcodeWriter = new EAN13Writer();
        String barcodeData = data.length() >= 12 ? data.substring(0, 12) : String.format("%12s", data).replace(' ', '0');
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeData, BarcodeFormat.EAN_13, 150, 50);
        return new ImageIcon(MatrixToImageWriter.toBufferedImage(bitMatrix));
    }
}