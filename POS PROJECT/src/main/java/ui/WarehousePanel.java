package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.itextpdf.text.Font;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.Color;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import model.Supplier;
import service.SupplierService;
import service.ItemService;
import service.StoreServiceImpl;
import util.ExcelIO;
import model.Store;
import model.Item;

/**
 * A panel for displaying suppliers and exporting store, supplier, and item data to Excel.
 */
public class WarehousePanel extends JPanel {
    private final SupplierService supplierService;
    private final ItemService itemService;
    private final StoreServiceImpl storeService;
    private JTable supplierTable;
    private DefaultTableModel supplierTableModel;
    private List<Supplier> currentSuppliers;
    private JButton btnExportExcel;

    /**
     * Constructs a WarehousePanel with the specified services.
     * 
     * @param supplierService the service for supplier operations
     * @param itemService the service for item operations
     * @param storeService the service for store operations
     */
    public WarehousePanel(SupplierService supplierService, ItemService itemService, StoreServiceImpl storeService) {
        this.supplierService = supplierService;
        this.itemService = itemService;
        this.storeService = storeService;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        currentSuppliers = new ArrayList<>();
        initUI();
        loadSuppliers();
    }

    /**
     * Initializes the user interface components.
     */
    private void initUI() {
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(66, 133, 244)), "Danh sách Supplier"));

        // Supplier Table setup
        supplierTableModel = new DefaultTableModel(
            new Object[] { "Tên supplier", "Người liên hệ", "Điện thoại", "Email", "Mã số thuế", "Items" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };
        
        supplierTable = new JTable(supplierTableModel);
        supplierTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        supplierTable.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
        supplierTable.getTableHeader().setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        supplierTable.setGridColor(new Color(149, 165, 166));
        supplierTable.setRowHeight(30);
        supplierTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Column width adjustments
        TableColumnModel columnModel = supplierTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(200);
        columnModel.getColumn(1).setPreferredWidth(120);
        columnModel.getColumn(2).setPreferredWidth(120);
        columnModel.getColumn(3).setPreferredWidth(180);
        columnModel.getColumn(4).setPreferredWidth(120);
        columnModel.getColumn(5).setPreferredWidth(60);

        // Set table height
        int rowHeight = 30;
        int headerHeight = supplierTable.getTableHeader().getPreferredSize().height;
        int totalHeight = headerHeight + (rowHeight * 10); // Assuming 10 rows for display
        supplierTable.setPreferredSize(new Dimension(supplierTable.getPreferredSize().width, totalHeight));

        JScrollPane supplierScrollPane = new JScrollPane(supplierTable);
        supplierScrollPane.setPreferredSize(new Dimension(supplierScrollPane.getPreferredSize().width, totalHeight + 5));
        supplierScrollPane.setBorder(BorderFactory.createLineBorder(new Color(149, 165, 166)));
        mainPanel.add(supplierScrollPane, BorderLayout.CENTER);

        // Export Excel Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnExportExcel = new JButton("Xuất Excel");
        btnExportExcel.setBackground(new Color(46, 204, 113));
        btnExportExcel.setForeground(Color.WHITE);
        btnExportExcel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        btnExportExcel.addActionListener(e -> exportToExcel());
        buttonPanel.add(btnExportExcel);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Loads suppliers from the database and updates the table.
     */
    private void loadSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers(1, Integer.MAX_VALUE);
        supplierTableModel.setRowCount(0);
        currentSuppliers.clear();
        if (suppliers != null && !suppliers.isEmpty()) {
            for (Supplier s : suppliers) {
                supplierTableModel.addRow(new Object[] {
                    s.getName() != null ? s.getName() : "(Chưa có)",
                    s.getContactName() != null ? s.getContactName() : "(Chưa có)",
                    s.getPhone() != null ? s.getPhone() : "(Chưa có)",
                    s.getEmail() != null ? s.getEmail() : "(Chưa có)",
                    s.getTaxCode() != null ? s.getTaxCode() : "(Chưa có)",
                    s.getItems() != null ? s.getItems().size() : 0
                });
                currentSuppliers.add(s);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy supplier!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Exports store, supplier, and item data to an Excel file.
     */
    private void exportToExcel() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel files", "xlsx"));
            fileChooser.setSelectedFile(new java.io.File("WarehouseData.xlsx"));
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }

                // Fetch data
                List<Store> stores = storeService.getAllStores(1, Integer.MAX_VALUE);
                List<Supplier> suppliers = supplierService.getAllSuppliers(1, Integer.MAX_VALUE);
                List<Item> items = new ArrayList<>();

                // Collect items for suppliers in currentSuppliers
                for (Supplier supplier : currentSuppliers) {
                    List<Item> supplierItems = itemService.getItemsBySupplierId(supplier.getId(), 1, Integer.MAX_VALUE);
                    if (supplierItems != null) {
                        items.addAll(supplierItems);
                    }
                }

                // Write data to Excel
                Workbook workbook = new XSSFWorkbook();
                
                // Stores sheet
                Sheet storeSheet = workbook.createSheet("Stores");
                Row storeHeader = storeSheet.createRow(0);
                String[] storeHeaders = {"ID", "Name", "ShortName", "Description", "Address", "City", "State", "Zip", "Phone", "Email", "Website", "Fax"};
                for (int i = 0; i < storeHeaders.length; i++) {
                    storeHeader.createCell(i).setCellValue(storeHeaders[i]);
                }
                int storeRowNum = 1;
                for (Store store : stores) {
                    Row row = storeSheet.createRow(storeRowNum++);
                    row.createCell(0).setCellValue(store.getId());
                    row.createCell(1).setCellValue(store.getName() != null ? store.getName() : "");
                    row.createCell(2).setCellValue(store.getShortName() != null ? store.getShortName() : "");
                    row.createCell(3).setCellValue(store.getDescription() != null ? store.getDescription() : "");
                    row.createCell(4).setCellValue(store.getAddress() != null ? store.getAddress() : "");
                    row.createCell(5).setCellValue(store.getCity() != null ? store.getCity() : "");
                    row.createCell(6).setCellValue(store.getState() != null ? store.getState() : "");
                    row.createCell(7).setCellValue(store.getZip() != null ? store.getZip() : "");
                    row.createCell(8).setCellValue(store.getPhone() != null ? store.getPhone() : "");
                    row.createCell(9).setCellValue(store.getEmail() != null ? store.getEmail() : "");
                    row.createCell(10).setCellValue(store.getWebsite() != null ? store.getWebsite() : "");
                    row.createCell(11).setCellValue(store.getFax() != null ? store.getFax() : "");
                }

                // Suppliers sheet
                Sheet supplierSheet = workbook.createSheet("Suppliers");
                Row supplierHeader = supplierSheet.createRow(0);
                String[] supplierHeaders = {"ID", "Name", "ContactName", "Phone", "Email", "Address", "TaxCode"};
                for (int i = 0; i < supplierHeaders.length; i++) {
                    supplierHeader.createCell(i).setCellValue(supplierHeaders[i]);
                }
                int supplierRowNum = 1;
                for (Supplier supplier : suppliers) {
                    Row row = supplierSheet.createRow(supplierRowNum++);
                    row.createCell(0).setCellValue(supplier.getId());
                    row.createCell(1).setCellValue(supplier.getName() != null ? supplier.getName() : "");
                    row.createCell(2).setCellValue(supplier.getContactName() != null ? supplier.getContactName() : "");
                    row.createCell(3).setCellValue(supplier.getPhone() != null ? supplier.getPhone() : "");
                    row.createCell(4).setCellValue(supplier.getEmail() != null ? supplier.getEmail() : "");
                    row.createCell(5).setCellValue(supplier.getAddress() != null ? supplier.getAddress() : "");
                    row.createCell(6).setCellValue(supplier.getTaxCode() != null ? supplier.getTaxCode() : "");
                }

                // Items sheet
                Sheet itemSheet = workbook.createSheet("Items");
                Row itemHeader = itemSheet.createRow(0);
                String[] itemHeaders = {"ID", "Name", "Type", "Unit", "Description", "Quantity", "SupplierID"};
                for (int i = 0; i < itemHeaders.length; i++) {
                    itemHeader.createCell(i).setCellValue(itemHeaders[i]);
                }
                int itemRowNum = 1;
                for (Item item : items) {
                    Row row = itemSheet.createRow(itemRowNum++);
                    row.createCell(0).setCellValue(item.getId());
                    row.createCell(1).setCellValue(item.getName() != null ? item.getName() : "");
                    row.createCell(2).setCellValue(item.getType() != null ? item.getType() : "");
                    row.createCell(3).setCellValue(item.getUnit() != null ? item.getUnit() : "");
                    row.createCell(4).setCellValue(item.getDescription() != null ? item.getDescription() : "");
                    row.createCell(5).setCellValue(item.getQuantity());
                    row.createCell(6).setCellValue(item.getSupplier() != null ? item.getSupplier().getId() : 0);
                }

                // Auto-size columns
                for (int i = 0; i < storeHeaders.length; i++) storeSheet.autoSizeColumn(i);
                for (int i = 0; i < supplierHeaders.length; i++) supplierSheet.autoSizeColumn(i);
                for (int i = 0; i < itemHeaders.length; i++) itemSheet.autoSizeColumn(i);

                // Write to file
                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    workbook.write(fos);
                }
                workbook.close();
                JOptionPane.showMessageDialog(this, "Xuất Excel thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xuất Excel: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}