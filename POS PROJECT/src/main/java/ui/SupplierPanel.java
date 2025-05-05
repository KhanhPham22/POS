package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import model.Item;
import model.Supplier;
import model.Warehouse;
import service.SupplierService;
import service.SupplierServiceImpl;
import ui.Elements.SearchBar;
import service.ItemService;
import service.ItemServiceImpl;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SupplierPanel extends JPanel {
    private final SupplierService supplierService;
    private final ItemService itemService;
    private JTable supplierTable;
    private DefaultTableModel supplierTableModel;
    private List<Supplier> currentSuppliers;
    private JTextField txtName, txtContactName, txtPhone, txtEmail, txtAddress, txtTaxCode;
    private JButton btnUpdate, btnDelete;
    private JTable itemTable;
    private DefaultTableModel itemTableModel;
    private JButton btnAddItem, btnDeleteItem;
    private Supplier currentSupplier;
    private JTextField searchField;

    // Pagination fields for suppliers
    private int supplierPageNumber = 1;
    private final int supplierPageSize = 10;
    private JButton btnPrevSupplier, btnNextSupplier;
    private JLabel lblSupplierPageInfo;

    // Pagination fields for items
    private int itemPageNumber = 1;
    private final int itemPageSize = 10;
    private JButton btnPrevItem, btnNextItem;
    private JLabel lblItemPageInfo;

    // Logo
    private final ImageIcon logoIcon = new ImageIcon("C:\\TTTN\\POS PROJECT\\img\\lck.png");

    public SupplierPanel(SupplierService supplierService, ItemService itemService) {
        this.supplierService = supplierService;
        this.itemService = itemService;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        currentSuppliers = new ArrayList<>();
        initUI();
        loadSuppliers();
    }

    private void initUI() {
        // Left Panel: Supplier Table, Search, and Logo
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(700, 0));
        leftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(66, 133, 244)), "Quản lý Supplier"));

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        searchField = new JTextField();
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(149, 165, 166)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.addActionListener(e -> {
            supplierPageNumber = 1;
            searchSuppliers(searchField.getText());
        });

        JButton searchButton = new JButton("Tìm");
        searchButton.setBackground(new Color(66, 133, 244));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("Arial", Font.BOLD, 14));
        searchButton.addActionListener(e -> {
            supplierPageNumber = 1;
            searchSuppliers(searchField.getText());
        });

        JPanel searchInputPanel = new JPanel(new BorderLayout(5, 0));
        searchInputPanel.add(searchField, BorderLayout.CENTER);
        searchInputPanel.add(searchButton, BorderLayout.EAST);
        searchPanel.add(searchInputPanel, BorderLayout.NORTH);

        // Supplier Pagination Controls
        JPanel supplierPaginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        supplierPaginationPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        btnPrevSupplier = new JButton("Previous");
        btnPrevSupplier.setBackground(new Color(149, 165, 166));
        btnPrevSupplier.setForeground(Color.WHITE);
        btnPrevSupplier.addActionListener(e -> {
            if (supplierPageNumber > 1) {
                supplierPageNumber--;
                loadSuppliers();
            }
        });
        btnNextSupplier = new JButton("Next");
        btnNextSupplier.setBackground(new Color(66, 133, 244));
        btnNextSupplier.setForeground(Color.WHITE);
        btnNextSupplier.addActionListener(e -> {
            supplierPageNumber++;
            loadSuppliers();
        });
        lblSupplierPageInfo = new JLabel("Page 1");
        lblSupplierPageInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        supplierPaginationPanel.add(btnPrevSupplier);
        supplierPaginationPanel.add(lblSupplierPageInfo);
        supplierPaginationPanel.add(btnNextSupplier);
        searchPanel.add(supplierPaginationPanel, BorderLayout.SOUTH);

        leftPanel.add(searchPanel, BorderLayout.NORTH);

        // Supplier Table
        supplierTableModel = new DefaultTableModel(
            new Object[] { "Tên supplier", "Người liên hệ", "Điện thoại", "Email", "Mã số thuế", "Items" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        supplierTable = new JTable(supplierTableModel);
        supplierTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        supplierTable.setFont(new Font("Arial", Font.PLAIN, 14));
        supplierTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        supplierTable.setGridColor(new Color(149, 165, 166));
        supplierTable.setRowHeight(30);
        supplierTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Adjust column widths to fill the table
        TableColumnModel columnModel = supplierTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(200);
        columnModel.getColumn(1).setPreferredWidth(120);
        columnModel.getColumn(2).setPreferredWidth(120);
        columnModel.getColumn(3).setPreferredWidth(180);
        columnModel.getColumn(4).setPreferredWidth(120);
        columnModel.getColumn(5).setPreferredWidth(60);

        // Set table height for 10 rows + header
        int rowHeight = 30;
        int headerHeight = supplierTable.getTableHeader().getPreferredSize().height;
        int totalHeight = headerHeight + (rowHeight * supplierPageSize);
        supplierTable.setPreferredSize(new Dimension(supplierTable.getPreferredSize().width, totalHeight));

        // Selection listener for supplier table
        supplierTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                itemPageNumber = 1;
                loadSelectedSupplier();
            }
        });

        JScrollPane supplierScrollPane = new JScrollPane(supplierTable);
        supplierScrollPane.setPreferredSize(new Dimension(supplierScrollPane.getPreferredSize().width, totalHeight + 5));
        supplierScrollPane.setBorder(BorderFactory.createLineBorder(new Color(149, 165, 166)));
        supplierScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        supplierScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        leftPanel.add(supplierScrollPane, BorderLayout.CENTER);

        // Bottom Panel: Add Supplier Button and Logo
        JPanel bottomPanel = new JPanel(new BorderLayout(0, 5));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // Reduced top padding
        
        // Add New Supplier Button
        JButton btnAddSupplier = new JButton("+ Thêm mới");
        btnAddSupplier.setBackground(new Color(46, 204, 113));
        btnAddSupplier.setForeground(Color.WHITE);
        btnAddSupplier.setFont(new Font("Arial", Font.BOLD, 14));
        btnAddSupplier.addActionListener(e -> createSupplier());
    
        // Create a wrapper panel with minimal padding
        JPanel buttonWrapper = new JPanel(new BorderLayout());
        buttonWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0)); // Minimal bottom padding
        buttonWrapper.add(btnAddSupplier, BorderLayout.CENTER);
        bottomPanel.add(buttonWrapper, BorderLayout.NORTH);
        
        // Add Logo
        try {
            // Scale the logo to fit (150x150 pixels to match the UI)
            Image scaledImage = logoIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            ImageIcon scaledLogoIcon = new ImageIcon(scaledImage);
            JLabel logoLabel = new JLabel(scaledLogoIcon, JLabel.CENTER);
            logoLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0)); // Add padding
            bottomPanel.add(logoLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            System.err.println("Error loading logo: " + e.getMessage());
            // Fallback: Add an empty label if the logo fails to load
            bottomPanel.add(new JLabel(""), BorderLayout.CENTER);
        }

        leftPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);

        // Right Panel: Supplier Details and Items
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        // Supplier Details Panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(66, 133, 244)), "Chi tiết Supplier"));

        // Supplier Info
        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtName = new JTextField();
        txtContactName = new JTextField();
        txtPhone = new JTextField();
        txtEmail = new JTextField();
        txtAddress = new JTextField();
        txtTaxCode = new JTextField();

        Font fieldFont = new Font("Arial", Font.PLAIN, 14);
        txtName.setFont(fieldFont);
        txtContactName.setFont(fieldFont);
        txtPhone.setFont(fieldFont);
        txtEmail.setFont(fieldFont);
        txtAddress.setFont(fieldFont);
        txtTaxCode.setFont(fieldFont);

        infoPanel.add(new JLabel("Tên supplier:"));
        infoPanel.add(txtName);
        infoPanel.add(new JLabel("Người liên hệ:"));
        infoPanel.add(txtContactName);
        infoPanel.add(new JLabel("Điện thoại:"));
        infoPanel.add(txtPhone);
        infoPanel.add(new JLabel("Email:"));
        infoPanel.add(txtEmail);
        infoPanel.add(new JLabel("Địa chỉ:"));
        infoPanel.add(txtAddress);
        infoPanel.add(new JLabel("Mã số thuế:"));
        infoPanel.add(txtTaxCode);

        detailsPanel.add(infoPanel);

        // Buttons for Supplier
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnUpdate = new JButton("Sửa");
        btnUpdate.setBackground(new Color(52, 152, 219));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.addActionListener(e -> updateSupplier());

        btnDelete = new JButton("Xóa");
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.setForeground(Color.WHITE);
        btnDelete.addActionListener(e -> deleteSupplier());

        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        detailsPanel.add(buttonPanel);

        rightPanel.add(detailsPanel, BorderLayout.NORTH);

        // Item Table Panel
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(66, 133, 244)), "Danh sách items"));

        // Item Search
        JPanel itemSearchPanel = new JPanel(new BorderLayout());
        JTextField itemSearchField = new JTextField();
        itemSearchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(149, 165, 166)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        itemSearchField.setFont(new Font("Arial", Font.PLAIN, 14));
        itemSearchField.addActionListener(e -> {
            itemPageNumber = 1;
            searchItems(itemSearchField.getText());
        });

        JButton itemSearchButton = new JButton("Tìm");
        itemSearchButton.setBackground(new Color(66, 133, 244));
        itemSearchButton.setForeground(Color.WHITE);
        itemSearchButton.addActionListener(e -> {
            itemPageNumber = 1;
            searchItems(itemSearchField.getText());
        });

        JPanel itemSearchInputPanel = new JPanel(new BorderLayout(5, 0));
        itemSearchInputPanel.add(itemSearchField, BorderLayout.CENTER);
        itemSearchInputPanel.add(itemSearchButton, BorderLayout.EAST);
        itemSearchPanel.add(itemSearchInputPanel, BorderLayout.NORTH);

        // Item Pagination Controls
        JPanel itemPaginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPrevItem = new JButton("Previous");
        btnPrevItem.setBackground(new Color(149, 165, 166));
        btnPrevItem.setForeground(Color.WHITE);
        btnPrevItem.addActionListener(e -> {
            if (itemPageNumber > 1) {
                itemPageNumber--;
                loadItemTable(currentSupplier);
            }
        });
        btnNextItem = new JButton("Next");
        btnNextItem.setBackground(new Color(66, 133, 244));
        btnNextItem.setForeground(Color.WHITE);
        btnNextItem.addActionListener(e -> {
            List<Item> allItems = itemService.getItemsBySupplierId(currentSupplier.getId(), 1, Integer.MAX_VALUE);
            int totalItems = allItems.size();
            int totalPages = (int) Math.ceil((double) totalItems / itemPageSize);
            if (itemPageNumber < totalPages) {
                itemPageNumber++;
                loadItemTable(currentSupplier);
            }
        });
        lblItemPageInfo = new JLabel("Page 1");
        lblItemPageInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        itemPaginationPanel.add(btnPrevItem);
        itemPaginationPanel.add(lblItemPageInfo);
        itemPaginationPanel.add(btnNextItem);
        itemSearchPanel.add(itemPaginationPanel, BorderLayout.SOUTH);

        itemPanel.add(itemSearchPanel, BorderLayout.NORTH);

        // Item Table
        itemTableModel = new DefaultTableModel(
            new Object[] { "Mã item", "Tên sản phẩm", "Đơn vị", "Số lượng", "Thao tác" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        itemTable = new JTable(itemTableModel);
        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemTable.setFont(new Font("Arial", Font.PLAIN, 14));
        itemTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        itemTable.setGridColor(new Color(149, 165, 166));
        itemTable.setRowHeight(30);

        // Đặt chiều cao tổng thể của bảng để vừa đủ 10 hàng + tiêu đề
        int itemRowHeight = 30;
        int itemHeaderHeight = itemTable.getTableHeader().getPreferredSize().height;
        int itemTotalHeight = itemHeaderHeight + (itemRowHeight * itemPageSize);
        itemTable.setPreferredSize(new Dimension(itemTable.getPreferredSize().width, itemTotalHeight));

        JScrollPane itemTableScrollPane = new JScrollPane(itemTable);
        itemTableScrollPane.setPreferredSize(new Dimension(itemTableScrollPane.getPreferredSize().width, itemTotalHeight + 5));
        itemTableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(149, 165, 166)));

        itemTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        itemTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), itemService, this));

        itemPanel.add(itemTableScrollPane, BorderLayout.CENTER);

        // Item Buttons
        JPanel itemButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAddItem = new JButton("Thêm item");
        btnAddItem.setBackground(new Color(46, 204, 113));
        btnAddItem.setForeground(Color.WHITE);
        btnAddItem.addActionListener(e -> addItemToSupplier());

        btnDeleteItem = new JButton("Xóa");
        btnDeleteItem.setBackground(new Color(231, 76, 60));
        btnDeleteItem.setForeground(Color.WHITE);
        btnDeleteItem.addActionListener(e -> deleteSelectedItem());

        itemButtonPanel.add(btnAddItem);
        itemButtonPanel.add(btnDeleteItem);
        itemPanel.add(itemButtonPanel, BorderLayout.SOUTH);

        rightPanel.add(itemPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.CENTER);
    }

    private static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setText("Sửa");
            setBackground(new Color(52, 152, 219));
            setForeground(Color.WHITE);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            return this;
        }
    }

    private static class ButtonEditor extends DefaultCellEditor { //edit item
        private JButton button;
        private ItemService itemService;
        private SupplierPanel supplierPanel;
        private Item currentItem;

        public ButtonEditor(JCheckBox checkBox, ItemService itemService, SupplierPanel supplierPanel) {
            super(checkBox);
            this.itemService = itemService;
            this.supplierPanel = supplierPanel;
            button = new JButton("Sửa");
            button.setOpaque(true);
            button.setBackground(new Color(52, 152, 219));
            button.setForeground(Color.WHITE);

            button.addActionListener(e -> {
                if (currentItem != null) {
                    ItemPanel itemPanel = new ItemPanel(currentItem, supplierPanel.currentSupplier, itemService, supplierPanel);
                    itemPanel.setItem(currentItem);
                    itemPanel.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(supplierPanel, "Lỗi: Không tìm thấy item để sửa!");
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentItem = supplierPanel.getItemAtRow(row);
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Sửa";
        }
    }

    private Item getItemAtRow(int row) { 
        if (currentSupplier == null || row < 0 || row >= itemTableModel.getRowCount()) {
            return null;
        }

        Object idValue = itemTableModel.getValueAt(row, 0);
        if (!(idValue instanceof Long)) {
            System.out.println("Error: Item ID is not a Long value. Found: " + idValue);
            return null;
        }
        long itemId = (long) idValue;

        for (Item item : currentSupplier.getItems()) {
            if (item.getId() == itemId) {
                return item;
            }
        }
        System.out.println("Error: Item with ID " + itemId + " not found in supplier's items.");
        return null;
    }

    private void deleteSelectedItem() { //delete item selected
        if (currentSupplier == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn supplier trước!");
            return;
        }

        int row = itemTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn item để xóa!");
            return;
        }

        Item itemToDelete = getItemAtRow(row);
        if (itemToDelete == null) {
            JOptionPane.showMessageDialog(this, "Lỗi: Không tìm thấy item!");
            return;
        }

        deleteItem(itemToDelete);
    }

    private void deleteItem(Item item) { //function delete item choice
        if (item == null) {
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa item này?", "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION)
            return;

        if (itemService.deleteItem(item.getId())) {
            currentSupplier.getItems().removeIf(i -> i.getId() == item.getId());
            supplierService.updateSupplier(currentSupplier);
            loadItemTable(currentSupplier);
            JOptionPane.showMessageDialog(this, "Item đã được xóa!");
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa item!");
        }
    }

    private void loadSuppliers() { //load supplier from table supplier in db
        String query = searchField.getText().trim();
        List<Supplier> suppliers;

        if (!query.isEmpty()) {
            suppliers = supplierService.getSuppliersByName(query);
        } else {
            suppliers = supplierService.getAllSuppliers(supplierPageNumber, supplierPageSize);
        }

        supplierTableModel.setRowCount(0);
        currentSuppliers.clear();
        if (suppliers == null || suppliers.isEmpty()) {
            lblSupplierPageInfo.setText("Page " + supplierPageNumber + " (No data)");
            btnNextSupplier.setEnabled(false);
        } else {
            for (Supplier s : suppliers) {
                supplierTableModel.addRow(new Object[] {
                    s.getName(),
                    s.getContactName() != null ? s.getContactName() : "(Chưa có)",
                    s.getPhone() != null ? s.getPhone() : "(Chưa có)",
                    s.getEmail() != null ? s.getEmail() : "(Chưa có)",
                    s.getTaxCode() != null ? s.getTaxCode() : "(Chưa có)",
                    s.getItems().size()
                });
                currentSuppliers.add(s);
            }
            List<Supplier> allSuppliers = supplierService.getAllSuppliers(1, Integer.MAX_VALUE);
            int totalSuppliers = allSuppliers.size();
            int totalPages = (int) Math.ceil((double) totalSuppliers / supplierPageSize);
            lblSupplierPageInfo.setText("Page " + supplierPageNumber + " of " + totalPages);
            btnNextSupplier.setEnabled(supplierPageNumber < totalPages);
        }
        btnPrevSupplier.setEnabled(supplierPageNumber > 1);
    }

    private void searchSuppliers(String query) {
        List<Supplier> suppliers = supplierService.getSuppliersByName(query);
        supplierTableModel.setRowCount(0);
        currentSuppliers.clear();
        if (suppliers == null || suppliers.isEmpty()) {
            lblSupplierPageInfo.setText("Page " + supplierPageNumber + " (No results)");
            btnNextSupplier.setEnabled(false);
            btnPrevSupplier.setEnabled(false);
            JOptionPane.showMessageDialog(this, "Không tìm thấy supplier!");
            return;
        }
        int startIndex = (supplierPageNumber - 1) * supplierPageSize;
        int endIndex = Math.min(startIndex + supplierPageSize, suppliers.size());
        if (startIndex >= suppliers.size()) {
            supplierPageNumber = 1;
            startIndex = 0;
            endIndex = Math.min(supplierPageSize, suppliers.size());
        }
        for (int i = startIndex; i < endIndex; i++) {
            Supplier s = suppliers.get(i);
            supplierTableModel.addRow(new Object[] {
                s.getName(),
                s.getContactName() != null ? s.getContactName() : "(Chưa có)",
                s.getPhone() != null ? s.getPhone() : "(Chưa có)",
                s.getEmail() != null ? s.getEmail() : "(Chưa có)",
                s.getTaxCode() != null ? s.getTaxCode() : "(Chưa có)",
                s.getItems().size()
            });
            currentSuppliers.add(s);
        }
        int totalPages = (int) Math.ceil((double) suppliers.size() / supplierPageSize);
        lblSupplierPageInfo.setText("Page " + supplierPageNumber + " of " + totalPages);
        btnPrevSupplier.setEnabled(supplierPageNumber > 1);
        btnNextSupplier.setEnabled(endIndex < suppliers.size());
    }

    private void searchItems(String query) {
        if (currentSupplier == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn supplier trước!");
            return;
        }
        itemTableModel.setRowCount(0);
        List<Item> items = itemService.getItemsBySupplierId(currentSupplier.getId(), itemPageNumber, itemPageSize);
        for (Item item : items) {
            if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                itemTableModel.addRow(new Object[] { item.getId(), item.getName(), item.getType(), item.getUnit(),
                        item.getQuantity(), "Sửa" });
            }
        }
        lblItemPageInfo.setText("Page " + itemPageNumber);
        btnPrevItem.setEnabled(itemPageNumber > 1);
        btnNextItem.setEnabled(items.size() == itemPageSize);
    }

    private void loadSelectedSupplier() {
        int selectedRow = supplierTable.getSelectedRow();
        if (selectedRow < 0 || selectedRow >= currentSuppliers.size()) {
            currentSupplier = null;
            return;
        }

        currentSupplier = currentSuppliers.get(selectedRow);

        txtName.setText(currentSupplier.getName());
        txtContactName.setText(currentSupplier.getContactName());
        txtPhone.setText(currentSupplier.getPhone());
        txtEmail.setText(currentSupplier.getEmail());
        txtAddress.setText(currentSupplier.getAddress());
        txtTaxCode.setText(currentSupplier.getTaxCode());

        loadItemTable(currentSupplier);
    }

    private void loadItemTable(Supplier supplier) {
        if (supplier == null)
            return;
        itemTableModel.setRowCount(0);

        List<Item> items = itemService.getItemsBySupplierId(supplier.getId(), itemPageNumber, itemPageSize);
        for (Item item : items) {
            itemTableModel.addRow(new Object[] { item.getId(), item.getName(), item.getType(), item.getUnit(),
                    item.getQuantity(), "Sửa" });
        }

        List<Item> allItems = itemService.getItemsBySupplierId(supplier.getId(), 1, Integer.MAX_VALUE);
        int totalItems = allItems.size();
        int totalPages = (int) Math.ceil((double) totalItems / itemPageSize);

        lblItemPageInfo.setText("Page " + itemPageNumber + " of " + totalPages + (items.isEmpty() ? " (No data)" : ""));
        btnPrevItem.setEnabled(itemPageNumber > 1);
        btnNextItem.setEnabled(itemPageNumber < totalPages);
    }

    private void createSupplier() {
        JTextField txtName = new JTextField();
        JTextField txtContactName = new JTextField();
        JTextField txtPhone = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtAddress = new JTextField();
        JTextField txtTaxCode = new JTextField();

        JPanel panel = new JPanel(new GridLayout(6, 2, 5, 5));
        panel.add(new JLabel("Tên supplier:"));
        panel.add(txtName);
        panel.add(new JLabel("Người liên hệ:"));
        panel.add(txtContactName);
        panel.add(new JLabel("Điện thoại:"));
        panel.add(txtPhone);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(new JLabel("Địa chỉ:"));
        panel.add(txtAddress);
        panel.add(new JLabel("Mã số thuế:"));
        panel.add(txtTaxCode);

        int result = JOptionPane.showConfirmDialog(this, panel, "Thêm Supplier Mới", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION)
            return;

        String name = txtName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên supplier không được để trống!");
            return;
        }
     
        Supplier supplier = new Supplier();
        supplier.setName(txtName.getText().trim());
        supplier.setContactName(txtContactName.getText().trim());
        supplier.setPhone(txtPhone.getText().trim());
        supplier.setEmail(txtEmail.getText().trim());
        supplier.setAddress(txtAddress.getText().trim());
        supplier.setTaxCode(txtTaxCode.getText().trim());
        supplier.setItems(new HashSet<>());
        supplier.setWarehouseImports(new HashSet<>());

        if (supplierService.createSupplier(supplier)) {
            JOptionPane.showMessageDialog(this, "Supplier đã được tạo!");
            supplierPageNumber = 1;
            loadSuppliers();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi: Tên,số điện thoại hoặc mã số thuế đã tồn tại!");
        }
    }

    private void updateSupplier() {
        if (currentSupplier == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn supplier trước!");
            return;
        }

        if (txtName.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên supplier không được để trống!");
            return;
        }

        currentSupplier.setName(txtName.getText());
        currentSupplier.setContactName(txtContactName.getText());
        currentSupplier.setPhone(txtPhone.getText());
        currentSupplier.setEmail(txtEmail.getText());
        currentSupplier.setAddress(txtAddress.getText());
        currentSupplier.setTaxCode(txtTaxCode.getText());

        if (supplierService.updateSupplier(currentSupplier)) {
            JOptionPane.showMessageDialog(this, "Supplier đã được cập nhật!");
            loadSuppliers();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật supplier!");
        }
    }

    private void deleteSupplier() {
        if (currentSupplier == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn supplier trước!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa supplier này?", "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION)
            return;

        if (supplierService.deleteSupplierById(currentSupplier.getId())) {
            JOptionPane.showMessageDialog(this, "Supplier đã được xóa!");
            currentSupplier = null;
            txtName.setText("");
            txtContactName.setText("");
            txtPhone.setText("");
            txtEmail.setText("");
            txtAddress.setText("");
            txtTaxCode.setText("");
            itemTableModel.setRowCount(0);
            supplierPageNumber = 1;
            loadSuppliers();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi khi xóa supplier!");
        }
    }

    private void addItemToSupplier() {
        if (currentSupplier == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn supplier trước!");
            return;
        }

        ItemPanel itemPanel = new ItemPanel(null, currentSupplier, itemService, this);
        itemPanel.setVisible(true);
    }

    public void refreshItems() {
        if (currentSupplier != null) {
            Supplier refreshedSupplier = supplierService.getSupplierById(currentSupplier.getId());
            if (refreshedSupplier != null) {
                currentSupplier = refreshedSupplier;
                loadItemTable(currentSupplier);
            }
        }
    }
}