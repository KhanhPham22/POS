package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import model.Store;
import service.StoreServiceImpl;

import java.awt.*;
import java.util.List;

/**
 * A panel for managing store information in a Point of Sale (POS) system.
 * This panel allows users to view, add, and edit store details.
 */
public class StorePanel extends JPanel {
    private final StoreServiceImpl storeService; // Service for store-related operations
    private JComboBox<String> storeComboBox;    // Dropdown for selecting stores
    private JLabel logoLabel;                  // Label for displaying store logo
    private JTable storeTable;                 // Table for displaying store details
    private List<Store> stores;                // List of stores
    private final String iconPath = "C:\\TTTN\\POS PROJECT\\img\\"; // Path to store logos

    /**
     * Constructs a StorePanel with the specified store service.
     * 
     * @param storeService the service for store operations
     */
    public StorePanel(StoreServiceImpl storeService) {
        this.storeService = storeService;
        initializeUI();    // Initialize the user interface
        loadStoreData();   // Load store data from the service
    }

    /**
     * Initializes the user interface components.
     */
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel titleLabel = new JLabel("Quản lý cửa hàng");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(titleLabel);
        titlePanel.setBackground(Color.WHITE);
        add(titlePanel, BorderLayout.NORTH);

        // Main content panel
        JPanel mainContentPanel = new JPanel(new BorderLayout(10, 10));
        mainContentPanel.setBackground(Color.WHITE);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Store selection and buttons panel
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectionPanel.setBackground(Color.WHITE);

        JLabel storeLabel = new JLabel("Chọn cửa hàng:");
        storeComboBox = new JComboBox<>();
        selectionPanel.add(storeLabel);
        selectionPanel.add(storeComboBox);

        // View button
        JButton viewButton = new JButton("Xem");
        viewButton.setBackground(new Color(70, 130, 180)); // Steel blue
        viewButton.setForeground(Color.WHITE);
        viewButton.addActionListener(e -> viewStore());

        // Add button
        JButton addButton = new JButton("Thêm");
        addButton.setBackground(new Color(50, 205, 50)); // Green
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> showCreateDialog());

        // Edit button
        JButton editButton = new JButton("Sửa");
        editButton.setBackground(new Color(255, 165, 0)); // Orange
        editButton.setForeground(Color.WHITE);
        editButton.addActionListener(e -> showEditDialog());

        selectionPanel.add(viewButton);
        selectionPanel.add(addButton);
        selectionPanel.add(editButton);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(selectionPanel, BorderLayout.WEST);

        // Logo panel
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(Color.WHITE);
        logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        logoLabel = new JLabel();
        logoLabel.setPreferredSize(new Dimension(100, 100));
        logoLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        logoPanel.add(logoLabel);

        // Details panel
        JPanel detailsWrapper = new JPanel(new BorderLayout());
        Color headerBg = new Color(70, 130, 180); // Header background color
        Color headerFg = Color.WHITE;              // Header foreground color
        Color rowBg = Color.WHITE;                 // Row background color
        Color selectionBg = new Color(200, 220, 240); // Selection background color
        Color gridColor = new Color(220, 220, 220);   // Grid line color

        detailsWrapper.setBackground(rowBg);
        detailsWrapper.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        detailsWrapper.setPreferredSize(new Dimension(0, 400));

        // Table model setup
        String[] columnNames = { "Thông tin", "Chi tiết" };
        Object[][] data = new Object[11][2]; // 11 rows for store details
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
            }
        };

        storeTable = new JTable(model);
        storeTable.setBackground(rowBg);
        storeTable.setSelectionBackground(selectionBg);
        storeTable.setSelectionForeground(Color.BLACK);
        storeTable.setGridColor(gridColor);
        storeTable.setShowGrid(true);
        storeTable.setRowHeight(30); // Set row height
        storeTable.setIntercellSpacing(new Dimension(0, 1)); // Row spacing
        storeTable.setFillsViewportHeight(true);

        // Customize table header
        JTableHeader header = storeTable.getTableHeader();
        header.setBackground(headerBg);
        header.setForeground(headerFg);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        // Custom cell renderer
        storeTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Bold font for the first column
                if (column == 0) {
                    c.setFont(new Font("Arial", Font.BOLD, 13));
                    c.setForeground(new Color(70, 70, 70));
                } else {
                    c.setFont(new Font("Arial", Font.PLAIN, 13));
                }

                // Alternate row colors
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? rowBg : new Color(245, 245, 245));
                }

                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(storeTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(rowBg);
        detailsWrapper.add(scrollPane, BorderLayout.CENTER);

        mainContentPanel.add(topPanel, BorderLayout.NORTH);
        mainContentPanel.add(logoPanel, BorderLayout.CENTER);
        mainContentPanel.add(detailsWrapper, BorderLayout.SOUTH);

        add(mainContentPanel, BorderLayout.CENTER);
    }

    /**
     * Loads store data from the service and updates the UI.
     */
    private void loadStoreData() {
        stores = storeService.getAllStores(1, 10); // Fetch first page with 10 stores
        if (stores != null && !stores.isEmpty()) {
            storeComboBox.removeAllItems();
            for (Store store : stores) {
                storeComboBox.addItem(store.getName());
            }
            updateStoreDetails(stores.get(0)); // Display first store by default
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy cửa hàng trong cơ sở dữ liệu.", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Updates the UI with details of the specified store.
     * 
     * @param store the store whose details to display
     */
    private void updateStoreDetails(Store store) {
        DefaultTableModel model = (DefaultTableModel) storeTable.getModel();
        // Set all store details in the table
        model.setValueAt("Tên cửa hàng:", 0, 0);
        model.setValueAt(store.getName() != null ? store.getName() : "", 0, 1);
        model.setValueAt("Tên viết tắt cửa hàng:", 1, 0);
        model.setValueAt(store.getShortName() != null ? store.getShortName() : "", 1, 1);
        model.setValueAt("Mô tả:", 2, 0);
        model.setValueAt(store.getDescription() != null ? store.getDescription() : "", 2, 1);
        model.setValueAt("Địa chỉ:", 3, 0);
        model.setValueAt(store.getAddress() != null ? store.getAddress() : "", 3, 1);
        model.setValueAt("Thành phố:", 4, 0);
        model.setValueAt(store.getCity() != null ? store.getCity() : "", 4, 1);
        model.setValueAt("Quận:", 5, 0);
        model.setValueAt(store.getState() != null ? store.getState() : "", 5, 1);
        model.setValueAt("Zip:", 6, 0);
        model.setValueAt(store.getZip() != null ? store.getZip() : "", 6, 1);
        model.setValueAt("Số điện thoại:", 7, 0);
        model.setValueAt(store.getPhone() != null ? store.getPhone() : "", 7, 1);
        model.setValueAt("Email:", 8, 0);
        model.setValueAt(store.getEmail() != null ? store.getEmail() : "", 8, 1);
        model.setValueAt("Website:", 9, 0);
        model.setValueAt(store.getWebsite() != null ? store.getWebsite() : "", 9, 1);
        model.setValueAt("Số fax:", 10, 0);
        model.setValueAt(store.getFax() != null ? store.getFax() : "", 10, 1);

        // Load and display store logo
        try {
            ImageIcon logoIcon = new ImageIcon(iconPath + "lck.png");
            if (logoIcon.getImage() != null) {
                Image scaledImage = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(scaledImage));
                logoLabel.setText("");
            } else {
                logoLabel.setIcon(null);
                logoLabel.setText("LOGO SHOP");
            }
        } catch (Exception e) {
            logoLabel.setIcon(null);
            logoLabel.setText("LOGO SHOP");
        }
    }

    /**
     * Displays details of the currently selected store.
     */
    private void viewStore() {
        int selectedIndex = storeComboBox.getSelectedIndex();
        if (selectedIndex >= 0 && selectedIndex < stores.size()) {
            updateStoreDetails(stores.get(selectedIndex));
        }
    }

    /**
     * Shows a dialog for creating a new store.
     */
    private void showCreateDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Thêm cửa hàng", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create input fields
        JTextField nameField = new JTextField(20);
        JTextField shortNameField = new JTextField(20);
        JTextField descriptionField = new JTextField(20);
        JTextField addressField = new JTextField(20);
        JTextField cityField = new JTextField(20);
        JTextField stateField = new JTextField(20);
        JTextField zipField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField websiteField = new JTextField(20);
        JTextField faxField = new JTextField(20);

        // Add fields to dialog with labels
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Tên cửa hàng:"), gbc);
        gbc.gridx = 1;
        dialog.add(nameField, gbc);

        // ... (other field additions omitted for brevity)

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Lưu");
        saveButton.setBackground(new Color(70, 130, 180));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> {
            // Create new store from input fields
            Store newStore = new Store();
            newStore.setName(nameField.getText());
            // ... (set other fields)

            if (storeService.createStore(newStore)) {
                JOptionPane.showMessageDialog(dialog, "Tạo cửa hàng thành công!", "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                loadStoreData(); // Refresh data
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Không thể tạo cửa hàng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = new JButton("Hủy");
        cancelButton.setBackground(Color.GRAY);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);

        dialog.setVisible(true);
    }

    /**
     * Shows a dialog for editing an existing store.
     */
    private void showEditDialog() {
        int selectedIndex = storeComboBox.getSelectedIndex();
        if (selectedIndex < 0 || selectedIndex >= stores.size()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một cửa hàng để sửa.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Store selectedStore = stores.get(selectedIndex);
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Sửa cửa hàng", true);
        dialog.setLayout(new GridBagLayout());
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create input fields pre-populated with store data
        JTextField nameField = new JTextField(selectedStore.getName(), 20);
        // ... (other field creations omitted for brevity)

        // Add fields to dialog with labels
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Tên cửa hàng:"), gbc);
        gbc.gridx = 1;
        dialog.add(nameField, gbc);

        // ... (other field additions omitted for brevity)

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Lưu");
        saveButton.setBackground(new Color(70, 130, 180));
        saveButton.setForeground(Color.WHITE);
        saveButton.addActionListener(e -> {
            // Update store from input fields
            selectedStore.setName(nameField.getText());
            // ... (update other fields)

            if (storeService.updateStore(selectedStore)) {
                JOptionPane.showMessageDialog(dialog, "Cập nhật cửa hàng thành công!", "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                loadStoreData(); // Refresh data
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Không thể cập nhật cửa hàng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = new JButton("Hủy");
        cancelButton.setBackground(Color.GRAY);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);

        dialog.setVisible(true);
    }
}