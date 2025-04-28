package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.Category;
import model.Product;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import service.ProductService;

//public class ProductListPanel extends JPanel {
//
//    private final ProductService productService;
//    private JComboBox<Category> categoryComboBox;
//    private JTextField searchField;
//    private JComboBox<String> statusFilterComboBox;
//    private JTable productTable;
//    private DefaultTableModel tableModel;
//    private int currentPage = 1;
//    private final int pageSize = 10;
//
//    public ProductListPanel(ProductService productService, List<Category> categories) {
//        this.productService = productService;
//        setLayout(new BorderLayout());
//
//        // Top filter bar
//        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//        categoryComboBox = new JComboBox<>(categories.toArray(new Category[0]));
//        filterPanel.add(new JLabel("Danh mục:"));
//        filterPanel.add(categoryComboBox);
//
//        searchField = new JTextField(15);
//        filterPanel.add(new JLabel("Tìm tên:"));
//        filterPanel.add(searchField);
//
//        statusFilterComboBox = new JComboBox<>(new String[]{"Tất cả", "Còn hàng", "Hết hàng"});
//        filterPanel.add(new JLabel("Trạng thái:"));
//        filterPanel.add(statusFilterComboBox);
//
//        JButton searchBtn = new JButton("Tìm");
//        searchBtn.addActionListener(e -> {
//            currentPage = 1;
//            loadProducts();
//        });
//        filterPanel.add(searchBtn);
//
//        add(filterPanel, BorderLayout.NORTH);
//
//        // Table
//        tableModel = new DefaultTableModel(new Object[]{"ID", "Tên", "Giá", "Giảm giá", "Số lượng", "Kích cỡ", "Trạng thái"}, 0);
//        productTable = new JTable(tableModel);
//        add(new JScrollPane(productTable), BorderLayout.CENTER);
//
//        // Pagination
//        JPanel paginationPanel = new JPanel();
//        JButton prevBtn = new JButton("← Trang trước");
//        JButton nextBtn = new JButton("Trang sau →");
//
//        prevBtn.addActionListener(e -> {
//            if (currentPage > 1) {
//                currentPage--;
//                loadProducts();
//            }
//        });
//
//        nextBtn.addActionListener(e -> {
//            currentPage++;
//            loadProducts(); // Có thể thêm kiểm tra tổng số trang nếu cần
//        });
//
//        paginationPanel.add(prevBtn);
//        paginationPanel.add(nextBtn);
//        add(paginationPanel, BorderLayout.SOUTH);
//
//        // Load ban đầu
//        loadProducts();
//    }
//
//    private void loadProducts() {
//        Category selectedCategory = (Category) categoryComboBox.getSelectedItem();
//        String keyword = searchField.getText().toLowerCase().trim();
//        String selectedStatus = (String) statusFilterComboBox.getSelectedItem();
//
//        try {
//            List<Product> allProducts = productService.getAllProducts();
//
//            // Lọc
//            List<Product> filtered = allProducts.stream()
//            		.filter(p -> selectedCategory == null || p.getCategory().getId() == selectedCategory.getId())
//                .filter(p -> keyword.isEmpty() || p.getName().toLowerCase().contains(keyword))
//                .filter(p -> {
//                    if ("Còn hàng".equals(selectedStatus)) return p.getQuantity() > 0;
//                    if ("Hết hàng".equals(selectedStatus)) return p.getQuantity() <= 0;
//                    return true; // "Tất cả"
//                })
//                .sorted(Comparator.comparing(Product::getName))
//                .toList();
//
//            // Phân trang
//            int fromIndex = (currentPage - 1) * pageSize;
//            int toIndex = Math.min(fromIndex + pageSize, filtered.size());
//            List<Product> paginated = (fromIndex < filtered.size()) ? filtered.subList(fromIndex, toIndex) : List.of();
//
//            // Hiển thị
//            tableModel.setRowCount(0);
//            for (Product p : paginated) {
//                tableModel.addRow(new Object[]{
//                    p.getId(),
//                    p.getName(),
//                    p.getPrice(),
//                    p.getDiscount(),  // Có thể xử lý format giảm giá ở đây
//                    p.getQuantity(),
//                    p.getSize(),
//                    (p.getQuantity() > 0 ? "Còn hàng" : "Hết hàng")
//                });
//            }
//
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Lỗi khi tải sản phẩm: " + e.getMessage());
//        }
//    }
//}


