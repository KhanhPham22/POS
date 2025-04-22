package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.Item;
import model.Supplier;
import model.Warehouse;
import service.SupplierService;
import service.ItemServiceImpl;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierPanel extends JPanel {
    private SupplierService supplierService;

    private JTable supplierTable;
    private DefaultTableModel tableModel;

    private JTextField txtName, txtContact, txtPhone, txtEmail, txtAddress, txtTaxCode, txtSearch;
    private JButton btnAdd, btnUpdate, btnDelete, btnClear, btnSearch;

    private JTable itemTable;
    private DefaultTableModel itemModel;
    private JButton btnAddItem, btnUpdateItem, btnDeleteItem;

    private Supplier currentSupplier;

    public SupplierPanel(SupplierService supplierService) {
        this.supplierService = supplierService;
        setLayout(new BorderLayout());
        initUI();
        loadSuppliers();
    }

    private void initUI() {
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        txtName = new JTextField();
        txtContact = new JTextField();
        txtPhone = new JTextField();
        txtEmail = new JTextField();
        txtAddress = new JTextField();
        txtTaxCode = new JTextField();
        txtSearch = new JTextField();

        formPanel.add(new JLabel("Name:"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("Contact Name:"));
        formPanel.add(txtContact);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(txtPhone);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(txtAddress);
        formPanel.add(new JLabel("Tax Code:"));
        formPanel.add(txtTaxCode);
        formPanel.add(new JLabel("Search by Name:"));
        formPanel.add(txtSearch);

        add(formPanel, BorderLayout.NORTH);

        // Buttons
        JPanel buttonPanel = new JPanel();
        btnAdd = new JButton("Add");
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        btnClear = new JButton("Clear");
        btnSearch = new JButton("Search");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);
        buttonPanel.add(btnSearch);

        add(buttonPanel, BorderLayout.CENTER);

        // Table
        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Phone", "Email"}, 0);
        supplierTable = new JTable(tableModel);
        add(new JScrollPane(supplierTable), BorderLayout.SOUTH);

        // Item Table Panel
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBorder(BorderFactory.createTitledBorder("Items of Selected Supplier"));

        itemModel = new DefaultTableModel(new Object[]{"Name", "Type", "Unit", "Description"}, 0);
        itemTable = new JTable(itemModel);

        JPanel itemButtonPanel = new JPanel();
        btnAddItem = new JButton("Add Item");
        btnUpdateItem = new JButton("Update Item");
        btnDeleteItem = new JButton("Delete Item");

        itemButtonPanel.add(btnAddItem);
        itemButtonPanel.add(btnUpdateItem);
        itemButtonPanel.add(btnDeleteItem);

        itemPanel.add(new JScrollPane(itemTable), BorderLayout.CENTER);
        itemPanel.add(itemButtonPanel, BorderLayout.SOUTH);

        add(itemPanel, BorderLayout.EAST);

        addListeners();
    }

    private void addListeners() {
        btnAdd.addActionListener(e -> createSupplier());
        btnUpdate.addActionListener(e -> updateSupplier());
        btnDelete.addActionListener(e -> deleteSupplier());
        btnClear.addActionListener(e -> clearFields());
        btnSearch.addActionListener(e -> searchSuppliers());

        btnAddItem.addActionListener(e -> addItemToSupplier());
        btnDeleteItem.addActionListener(e -> deleteSelectedItem());

        supplierTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) loadSelectedSupplier();
        });
    }

    private void loadSuppliers() {
        List<Supplier> suppliers = supplierService.getAllSuppliers();
        tableModel.setRowCount(0);
        for (Supplier s : suppliers) {
            tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getPhone(), s.getEmail()});
        }
    }

    private void createSupplier() {
        Supplier s = buildSupplierFromForm();
        if (supplierService.createSupplier(s)) {
            JOptionPane.showMessageDialog(this, "Supplier created!");
            loadSuppliers();
            clearFields();
        }
    }

    private void updateSupplier() {
        int row = supplierTable.getSelectedRow();
        if (row < 0) return;
        Long id = (Long) supplierTable.getValueAt(row, 0);
        Supplier s = buildSupplierFromForm();
        s.setId(id);
        if (supplierService.updateSupplier(s)) {
            JOptionPane.showMessageDialog(this, "Supplier updated!");
            loadSuppliers();
            clearFields();
        }
    }

    private void deleteSupplier() {
        int row = supplierTable.getSelectedRow();
        if (row < 0) return;
        Long id = (Long) supplierTable.getValueAt(row, 0);
        if (supplierService.deleteSupplierById(id)) {
            JOptionPane.showMessageDialog(this, "Supplier deleted!");
            loadSuppliers();
            clearFields();
        }
    }

    private void searchSuppliers() {
        String keyword = txtSearch.getText();
        List<Supplier> list = supplierService.getSuppliersByName(keyword);
        tableModel.setRowCount(0);
        for (Supplier s : list) {
            tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getPhone(), s.getEmail()});
        }
    }

    private void loadSelectedSupplier() {
        int row = supplierTable.getSelectedRow();
        if (row < 0) return;

        Long id = (Long) supplierTable.getValueAt(row, 0);
        Supplier s = supplierService.getSupplierById(id);
        if (s == null) return;

        currentSupplier = s;

        txtName.setText(s.getName());
        txtContact.setText(s.getContactName());
        txtPhone.setText(s.getPhone());
        txtEmail.setText(s.getEmail());
        txtAddress.setText(s.getAddress());
        txtTaxCode.setText(s.getTaxCode());

        loadItemTable(s);
    }

    private void loadItemTable(Supplier supplier) {
        itemModel.setRowCount(0);
        for (Item item : supplier.getItems()) {
            itemModel.addRow(new Object[]{
                item.getName(),
                item.getType(),
                item.getUnit(),
                item.getDescription()
            });
        }
    }

    private void addItemToSupplier() {
        if (currentSupplier == null) {
            JOptionPane.showMessageDialog(this, "Please select a supplier first.");
            return;
        }

        String name = JOptionPane.showInputDialog(this, "Enter item name:");
        if (name == null || name.isEmpty()) return;

        Item item = new Item();
        item.setName(name);
        item.setType("Default Type");
        item.setUnit("pcs");
        item.setDescription("Description");
        item.setSupplier(currentSupplier);

        currentSupplier.getItems().add(item);
        supplierService.updateSupplier(currentSupplier);

        loadItemTable(currentSupplier);
    }

    private void deleteSelectedItem() {
        int row = itemTable.getSelectedRow();
        if (row < 0 || currentSupplier == null) return;

        currentSupplier.getItems().remove(row);
        supplierService.updateSupplier(currentSupplier);
        loadItemTable(currentSupplier);
    }

    private Supplier buildSupplierFromForm() {
        Supplier s = new Supplier();
        s.setName(txtName.getText());
        s.setContactName(txtContact.getText());
        s.setPhone(txtPhone.getText());
        s.setEmail(txtEmail.getText());
        s.setAddress(txtAddress.getText());
        s.setTaxCode(txtTaxCode.getText());
        s.setItems(new ArrayList<>());
        return s;
    }

    private void clearFields() {
        txtName.setText("");
        txtContact.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        txtTaxCode.setText("");
        txtSearch.setText("");
        tableModel.setRowCount(0);
        itemModel.setRowCount(0);
        currentSupplier = null;
        loadSuppliers();
    }
}
