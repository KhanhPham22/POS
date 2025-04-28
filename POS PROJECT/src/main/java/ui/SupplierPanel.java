package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

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
    private JList<Supplier> supplierList;
    private DefaultListModel<Supplier> supplierListModel;
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

    public SupplierPanel(SupplierService supplierService, ItemService itemService) {
        this.supplierService = supplierService;
        this.itemService = itemService;
        setLayout(new BorderLayout());
        initUI();
        loadSuppliers();
    }

    private void initUI() {
        // Left Panel: Supplier List and Search
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(300, 0));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Quản lý Supplier"));

        // Search Panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchField = new JTextField();
        searchField.setBorder(BorderFactory.createTitledBorder("Tìm kiếm supplier..."));
        searchField.addActionListener(e -> {
            supplierPageNumber = 1; // Reset to first page on search
            searchSuppliers(searchField.getText());
        });
        
        JButton searchButton = new JButton("Tìm");
        searchButton.addActionListener(e -> {
            supplierPageNumber = 1;
            searchSuppliers(searchField.getText());
        });
        
        JPanel searchInputPanel = new JPanel(new BorderLayout());
        searchInputPanel.add(searchField, BorderLayout.CENTER);
        searchInputPanel.add(searchButton, BorderLayout.EAST);
        searchPanel.add(searchInputPanel, BorderLayout.NORTH);
        
        // Supplier Pagination Controls
        JPanel supplierPaginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPrevSupplier = new JButton("Previous");
        btnPrevSupplier.addActionListener(e -> {
            if (supplierPageNumber > 1) {
                supplierPageNumber--;
                loadSuppliers();
            }
        });
        btnNextSupplier = new JButton("Next");
        btnNextSupplier.addActionListener(e -> {
            supplierPageNumber++;
            loadSuppliers();
        });
        lblSupplierPageInfo = new JLabel("Page 1");
        supplierPaginationPanel.add(btnPrevSupplier);
        supplierPaginationPanel.add(lblSupplierPageInfo);
        supplierPaginationPanel.add(btnNextSupplier);
        searchPanel.add(supplierPaginationPanel, BorderLayout.SOUTH);
        
        leftPanel.add(searchPanel, BorderLayout.NORTH);

        // Supplier List
        supplierListModel = new DefaultListModel<>();
        supplierList = new JList<>(supplierListModel);
        supplierList.setCellRenderer(new SupplierListRenderer());
        supplierList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        supplierList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                itemPageNumber = 1; // Reset item page when selecting a new supplier
                loadSelectedSupplier();
            }
        });
        leftPanel.add(new JScrollPane(supplierList), BorderLayout.CENTER);

        // Add New Supplier Button
        JButton btnAddSupplier = new JButton("+ Thêm mới");
        btnAddSupplier.setBackground(new Color(46, 204, 113));
        btnAddSupplier.addActionListener(e -> createSupplier());
        leftPanel.add(btnAddSupplier, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);

        // Right Panel: Supplier Details and Items
        JPanel rightPanel = new JPanel(new BorderLayout());

        // Supplier Details Panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBorder(BorderFactory.createTitledBorder(""));

        // Supplier Info
        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtName = new JTextField();
        txtContactName = new JTextField();
        txtPhone = new JTextField();
        txtEmail = new JTextField();
        txtAddress = new JTextField();
        txtTaxCode = new JTextField();

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

        // Buttons for Supplier (Update, Delete)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnUpdate = new JButton("Sửa");
        btnUpdate.setBackground(new Color(52, 152, 219));
        btnUpdate.addActionListener(e -> updateSupplier());

        btnDelete = new JButton("Xóa");
        btnDelete.setBackground(new Color(231, 76, 60));
        btnDelete.addActionListener(e -> deleteSupplier());

        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        detailsPanel.add(buttonPanel);

        rightPanel.add(detailsPanel, BorderLayout.NORTH);

        // Item Table Panel
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBorder(BorderFactory.createTitledBorder("Danh sách items"));

        // Item Search
        JPanel itemSearchPanel = new JPanel(new BorderLayout());
        JTextField itemSearchField = new JTextField();
        itemSearchField.setBorder(BorderFactory.createTitledBorder("Tìm kiếm item..."));
        itemSearchField.addActionListener(e -> {
            itemPageNumber = 1; // Reset to first page on search
            searchItems(itemSearchField.getText());
        });
        
        JButton itemSearchButton = new JButton("Tìm");
        itemSearchButton.addActionListener(e -> {
            itemPageNumber = 1;
            searchItems(itemSearchField.getText());
        });
        
        JPanel itemSearchInputPanel = new JPanel(new BorderLayout());
        itemSearchInputPanel.add(itemSearchField, BorderLayout.CENTER);
        itemSearchInputPanel.add(itemSearchButton, BorderLayout.EAST);
        itemSearchPanel.add(itemSearchInputPanel, BorderLayout.NORTH);
        
        // Item Pagination Controls
        JPanel itemPaginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPrevItem = new JButton("Previous");
        btnPrevItem.addActionListener(e -> {
            if (itemPageNumber > 1) {
                itemPageNumber--;
                loadItemTable(currentSupplier);
            }
        });
        btnNextItem = new JButton("Next");
        btnNextItem.addActionListener(e -> {
            itemPageNumber++;
            loadItemTable(currentSupplier);
        });
        lblItemPageInfo = new JLabel("Page 1");
        itemPaginationPanel.add(btnPrevItem);
        itemPaginationPanel.add(lblItemPageInfo);
        itemPaginationPanel.add(btnNextItem);
        itemSearchPanel.add(itemPaginationPanel, BorderLayout.SOUTH);
        
        itemPanel.add(itemSearchPanel, BorderLayout.NORTH);

        // Item Table
        itemTableModel = new DefaultTableModel(new Object[]{"Mã item", "Tên sản phẩm", "Đơn giá", "Số lượng", "Thao tác"}, 0) {
            @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };
                itemTable = new JTable(itemTableModel);
                itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                
                // Add renderer and editor for action buttons
                itemTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
                itemTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox(), itemService, this));
        
                itemPanel.add(new JScrollPane(itemTable), BorderLayout.CENTER);
        
                // Item Buttons
                JPanel itemButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                btnAddItem = new JButton("Thêm item");
                btnAddItem.setBackground(new Color(46, 204, 113));
                btnAddItem.addActionListener(e -> addItemToSupplier());
        
                btnDeleteItem = new JButton("Xóa");
                btnDeleteItem.setBackground(new Color(231, 76, 60));
                btnDeleteItem.addActionListener(e -> deleteSelectedItem());
        
                itemButtonPanel.add(btnAddItem);
                itemButtonPanel.add(btnDeleteItem);
                itemPanel.add(itemButtonPanel, BorderLayout.SOUTH);
        
                rightPanel.add(itemPanel, BorderLayout.CENTER);
        
                add(rightPanel, BorderLayout.CENTER);
            }
        
            // Custom renderer for supplier list
            private static class SupplierListRenderer extends DefaultListCellRenderer {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index, 
                                                            boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value instanceof Supplier) {
                        Supplier supplier = (Supplier) value;
                        setText("<html><b>" + supplier.getName() + "</b><br>" +
                               "Điện thoại: " + supplier.getPhone() + "<br>" +
                               supplier.getItems().size() + " items</html>");
                    }
                    return this;
                }
            }
        
            // Button renderer for action column
            private static class ButtonRenderer extends JButton implements TableCellRenderer {
                public ButtonRenderer() {
                    setOpaque(true);
                    setText("Sửa");
                    setBackground(new Color(52, 152, 219));
                }
        
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                             boolean isSelected, boolean hasFocus, 
                                                             int row, int column) {
                     
                    return this;
                }
            }
        
            // Button editor for action column
            private static class ButtonEditor extends DefaultCellEditor {
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
                    button.addActionListener(e -> fireEditingStopped());
                }
        
                public Component getTableCellEditorComponent(JTable table, Object value,
                                                          boolean isSelected, int row, int column) {
                    currentItem = supplierPanel.getItemAtRow(row);
                    
                    return button;
                }
        
                public Object getCellEditorValue() {
                    if (currentItem == null) {
                        JOptionPane.showMessageDialog(supplierPanel, "Lỗi: Không tìm thấy item để sửa!");
                        return "Sửa";
                    }
                    ItemPanel itemPanel = new ItemPanel(null, supplierPanel.currentSupplier, itemService, supplierPanel);
                    itemPanel.setItem(currentItem);
                    itemPanel.setVisible(true);
                    return "Sửa";
                }
            }
        
            private Item getItemAtRow(int row) {
                if (currentSupplier == null || row < 0 || row >= itemTableModel.getRowCount()) {
                    return null;
                }
                
                // Lấy ID từ cột đầu tiên của bảng (cột Mã item)
                long itemId = (long) itemTableModel.getValueAt(row, 0);
                
                // Tìm item trong danh sách của supplier hiện tại
                for (Item item : currentSupplier.getItems()) {
                    if (item.getId() == itemId) {
                        return item;
                    }
                }
                return null;
            }
        
            
        
            private void deleteSelectedItem() {
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
        
            private void deleteItem(Item item) {
                if (item == null) {
                    return;
                }
        
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Bạn có chắc muốn xóa item này?", 
                    "Xác nhận xóa", 
                    JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;
        
                // Xóa item từ database bằng ID (kiểu long)
                if (itemService.deleteItem(item.getId())) {
                    // Nếu xóa thành công từ database thì xóa khỏi danh sách hiện tại
                    currentSupplier.getItems().removeIf(i -> i.getId() == item.getId());
                    supplierService.updateSupplier(currentSupplier);
                    loadItemTable(currentSupplier);
                    JOptionPane.showMessageDialog(this, "Item đã được xóa!");
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi xóa item!");
                }
            }
        
            private void loadSuppliers() {
                List<Supplier> suppliers = supplierService.getAllSuppliers(supplierPageNumber, supplierPageSize);
                supplierListModel.clear();
                if (suppliers.isEmpty()) {
                    lblSupplierPageInfo.setText("Page " + supplierPageNumber + " (No data)");
                    btnNextSupplier.setEnabled(false);
                } else {
                    for (Supplier s : suppliers) {
                        supplierListModel.addElement(s);
                    }
                    lblSupplierPageInfo.setText("Page " + supplierPageNumber);
                    btnNextSupplier.setEnabled(suppliers.size() == supplierPageSize); // Enable Next if full page
                }
                btnPrevSupplier.setEnabled(supplierPageNumber > 1);
            }
        
            private void searchSuppliers(String query) {
                List<Supplier> suppliers = supplierService.getSuppliersByName(query);
                supplierListModel.clear();
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
                    supplierListModel.addElement(suppliers.get(i));
                }
                lblSupplierPageInfo.setText("Page " + supplierPageNumber + " of " + 
                    ((suppliers.size() + supplierPageSize - 1) / supplierPageSize));
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
                        itemTableModel.addRow(new Object[]{
                            item.getId(),
                            item.getName(),
                            item.getType(),
                            item.getUnit(),
                            item.getQuantity(),
                            "✏️"
                        });
                    }
                }
                lblItemPageInfo.setText("Page " + itemPageNumber);
                btnPrevItem.setEnabled(itemPageNumber > 1);
                btnNextItem.setEnabled(items.size() == itemPageSize);
            }
        
            private void loadSelectedSupplier() {
                Supplier selectedSupplier = supplierList.getSelectedValue();
                if (selectedSupplier == null) return;
        
                currentSupplier = selectedSupplier;
        
                txtName.setText(currentSupplier.getName());
                txtContactName.setText(currentSupplier.getContactName());
                txtPhone.setText(currentSupplier.getPhone());
                txtEmail.setText(currentSupplier.getEmail());
                txtAddress.setText(currentSupplier.getAddress());
                txtTaxCode.setText(currentSupplier.getTaxCode());
        
                loadItemTable(currentSupplier);
            }
        
            private void loadItemTable(Supplier supplier) {
                if (supplier == null) return;
                itemTableModel.setRowCount(0);
                List<Item> items = itemService.getItemsBySupplierId(supplier.getId(), itemPageNumber, itemPageSize);
                for (Item item : items) {
                    itemTableModel.addRow(new Object[]{
                        item.getId(),
                        item.getName(),
                        item.getType(),
                        item.getUnit(),
                        item.getQuantity(),
                        "✏️"
                    });
                }
                lblItemPageInfo.setText("Page " + itemPageNumber);
                btnPrevItem.setEnabled(itemPageNumber > 1);
                btnNextItem.setEnabled(items.size() == itemPageSize);
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
                if (result != JOptionPane.OK_OPTION) return;
        
                if (txtName.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Tên supplier không được để trống!");
                    return;
                }
        
                Supplier supplier = new Supplier();
                supplier.setName(txtName.getText());
                supplier.setContactName(txtContactName.getText());
                supplier.setPhone(txtPhone.getText());
                supplier.setEmail(txtEmail.getText());
                supplier.setAddress(txtAddress.getText());
                supplier.setTaxCode(txtTaxCode.getText());
                // Initialize Sets instead of Lists
                supplier.setItems(new HashSet<>());
                supplier.setWarehouseImports(new HashSet<>());
        
                if (supplierService.createSupplier(supplier)) {
                    JOptionPane.showMessageDialog(this, "Supplier đã được tạo!");
                    supplierPageNumber = 1; // Reset to first page
                    loadSuppliers();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi khi tạo supplier!");
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
        
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Bạn có chắc muốn xóa supplier này?", 
                    "Xác nhận xóa", 
                    JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;
        
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
                    // Lấy lại supplier mới nhất từ database
                    Supplier refreshedSupplier = supplierService.getSupplierById(currentSupplier.getId());
                    if (refreshedSupplier != null) {
                        currentSupplier = refreshedSupplier;
                        loadItemTable(currentSupplier);
                    }
                }
            }
        
}