package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.regex.Pattern;

import model.Customer;
import service.PersonService;
import ui.Elements.SearchBar;

public class CustomerPanel extends JPanel {

    private JTable customerTable;
    private DefaultTableModel tableModel;
    private JButton addButton;
    private JLabel pageInfoLabel;
    private JButton prevButton, nextButton;
    private SearchBar searchBar;

    private int currentPage = 1;
    private int pageSize = 10;
    private int totalRecords = 0;
    private String currentSearchQuery = "";

    private final PersonService personService;

    public CustomerPanel(PersonService personService) {
        this.personService = personService;
        initializeUI();
        loadCustomerData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Top panel with search bar and add button
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Search bar
        searchBar = new SearchBar(query -> {
            currentSearchQuery = query;
            currentPage = 1;
            loadCustomerData();
        });
        searchBar.setPlaceholder("Search by name or phone");
        searchBar.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchBar.setBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        searchBar.setBackground(new Color(245, 245, 245));
        searchBar.setPreferredSize(new Dimension(400, 40));

        // Add button
        addButton = new JButton("+ Thêm khách hàng");
        addButton.setBackground(new Color(50, 205, 50));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addButton.addActionListener(e -> showAddCustomerDialog());

        topPanel.add(searchBar, BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Table setup with columns
        String[] columnNames = { "ID", "Full Name", "Phone", "Date of Birth", "Gender", "Address", "Points", "Actions" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Allow editing only for the Actions column
            }
        };

        customerTable = new JTable(tableModel);
        customerTable.setFillsViewportHeight(true);
        customerTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Set custom renderer and editor for the Actions column
        customerTable.getColumnModel().getColumn(7).setCellRenderer(new ActionCellRenderer());
        customerTable.getColumnModel().getColumn(7).setCellEditor(new ActionCellEditor());

        // Make header bold
        customerTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));

        customerTable.setRowHeight(61);

        // Adjusted column widths
        customerTable.getColumnModel().getColumn(0).setPreferredWidth(110); // ID
        customerTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Full Name
        customerTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Phone
        customerTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Date of Birth
        customerTable.getColumnModel().getColumn(4).setPreferredWidth(125); // Gender
        customerTable.getColumnModel().getColumn(5).setPreferredWidth(200); // Address
        customerTable.getColumnModel().getColumn(6).setPreferredWidth(125); // Points
        customerTable.getColumnModel().getColumn(7).setPreferredWidth(300); // Actions

        JScrollPane scrollPane = new JScrollPane(customerTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with pagination
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Pagination controls
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        prevButton = new JButton("Previous");
        prevButton.setBackground(new Color(70, 130, 180));
        prevButton.setForeground(Color.WHITE);
        prevButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        prevButton.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadCustomerData();
            }
        });

        nextButton = new JButton("Next");
        nextButton.setBackground(new Color(70, 130, 180));
        nextButton.setForeground(Color.WHITE);
        nextButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nextButton.addActionListener(e -> {
            if (currentPage * pageSize < totalRecords) {
                currentPage++;
                loadCustomerData();
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

    private void loadCustomerData() {
        if (tableModel == null) {
            System.err.println("TableModel is null, cannot load data");
            return;
        }
        try {
            tableModel.setRowCount(0);
            // Load all customers for counting total records and filtering
            List<Customer> allCustomers = personService.getAllCustomers(1, Integer.MAX_VALUE);
            // Apply search query filter
            List<Customer> filteredCustomers = allCustomers.stream()
                    .filter(this::matchesSearchQuery)
                    .toList();
            totalRecords = filteredCustomers.size();

            // Apply pagination
            int startIndex = (currentPage - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, filteredCustomers.size());
            List<Customer> paginatedCustomers = filteredCustomers.subList(startIndex, endIndex);

            for (Customer customer : paginatedCustomers) {
                tableModel.addRow(new Object[] {
                    customer.getCustomerNumber(),
                    getFullName(customer),
                    customer.getPhone(),
                    customer.getDateOfBirth(),
                    customer.getPersonGender(),
                    customer.getAddress(),
                    customer.getPoints(),
                    ""
                });
            }

            updatePaginationInfo();
            customerTable.revalidate();
            customerTable.repaint();
        } catch (Exception e) {
            showError("Error loading customer data: " + e.getMessage());
        }
    }

    private boolean matchesSearchQuery(Customer customer) {
        if (currentSearchQuery.isEmpty()) {
            return true;
        }
        String query = currentSearchQuery.toLowerCase();
        return getFullName(customer).toLowerCase().contains(query)
                || (customer.getPhone() != null && customer.getPhone().toLowerCase().contains(query));
    }

    private String getFullName(Customer customer) {
        return (customer.getPersonFirstName() != null ? customer.getPersonFirstName() : "") + " "
                + (customer.getPersonMiddleName() != null ? customer.getPersonMiddleName() : "") + " "
                + (customer.getPersonLastName() != null ? customer.getPersonLastName() : "");
    }

    private void updatePaginationInfo() {
        int start = (currentPage - 1) * pageSize + 1;
        int end = Math.min(currentPage * pageSize, totalRecords);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        pageInfoLabel.setText(String.format("Page %d of %d (%d customers)", currentPage, totalPages, totalRecords));

        prevButton.setEnabled(currentPage > 1);
        nextButton.setEnabled(currentPage * pageSize < totalRecords);
    }

    private boolean isPhoneValid(String phone, long excludePersonId) throws Exception {
        if (!Pattern.matches("\\d{9}", phone)) {
            showError("Phone number must be exactly 9 digits.");
            return false;
        }
        Customer existingCustomer = personService.getCustomerByPhone(phone);
        if (existingCustomer != null && existingCustomer.getPersonId() != excludePersonId) {
            showError("Phone number already exists. Please enter a different phone number.");
            return false;
        }
        return true;
    }

    private void showAddCustomerDialog() {
        JDialog dialog = createDialog("Add New Customer", 400, 450);
        JTextField firstNameField = new JTextField();
        JTextField middleNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JComboBox<String> genderCombo = new JComboBox<>(new String[] { "Male", "Female", "Other" });
        genderCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        genderCombo.setBackground(Color.WHITE);
        genderCombo.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        JTextField dobField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField addressField = new JTextField();
        JTextArea descriptionField = new JTextArea(3, 20);

        descriptionField.setLineWrap(true);
        descriptionField.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(descriptionField);
        descriptionScroll.setPreferredSize(new Dimension(200, 60));

        addDialogFields(dialog,
                new String[] { "First Name:", "Middle Name:", "Last Name:", "Gender:", "Date of Birth:", "Phone:", "Address:", "Description:" },
                new JComponent[] { firstNameField, middleNameField, lastNameField, genderCombo, dobField, phoneField, addressField, descriptionScroll });

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                String phone = phoneField.getText().trim();
                if (!isPhoneValid(phone, -1)) {
                    return;
                }

                String dob = dobField.getText().trim();
                if (!dob.isEmpty()) {
                    if (!Pattern.matches("\\d{4}-\\d{2}-\\d{2}", dob)) {
                        showError("Date of Birth must be in YYYY-MM-DD format (e.g., 2003-02-22)");
                        return;
                    }
                    try {
                        LocalDate date = LocalDate.parse(dob);
                        int month = date.getMonthValue();
                        int day = date.getDayOfMonth();
                        if (month < 1 || month > 12) {
                            showError("Month must be between 1 and 12");
                            return;
                        }
                        int maxDays = date.getMonth().length(date.isLeapYear());
                        if (day < 1 || day > maxDays) {
                            showError("Day must be between 1 and " + maxDays + " for the given month");
                            return;
                        }
                    } catch (DateTimeParseException ex) {
                        showError("Invalid date format: " + ex.getMessage());
                        return;
                    }
                }

                Customer customer = new Customer();
                customer.setPersonFirstName(firstNameField.getText());
                customer.setPersonMiddleName(middleNameField.getText());
                customer.setPersonLastName(lastNameField.getText());
                customer.setPersonGender((String) genderCombo.getSelectedItem());
                customer.setDateOfBirth(dob);
                customer.setPhone(phone);
                customer.setAddress(addressField.getText());
                customer.setDescription(descriptionField.getText());
                customer.setPoints(0.0);
                customer.generateCustomerNumber();

                personService.createCustomer(customer);
                loadCustomerData();
                dialog.dispose();
            } catch (Exception ex) {
                showError("Error saving customer: " + ex.getMessage());
            }
        });

        addDialogButtons(dialog, saveButton);
    }

    private void showCustomerDetail(int row, String code) {
        try {
            Customer customer = personService.getAllCustomers(1, Integer.MAX_VALUE).stream()
                    .filter(c -> c.getCustomerNumber().equals(code)).findFirst()
                    .orElseThrow(() -> new Exception("Customer not found"));

            JDialog dialog = createDialog("Customer Details", 400, 400);
            dialog.setLocationRelativeTo(null);

            JTextField firstNameField = new JTextField(customer.getPersonFirstName() != null ? customer.getPersonFirstName() : "");
            JTextField middleNameField = new JTextField(customer.getPersonMiddleName() != null ? customer.getPersonMiddleName() : "");
            JTextField lastNameField = new JTextField(customer.getPersonLastName() != null ? customer.getPersonLastName() : "");
            JComboBox<String> genderCombo = new JComboBox<>(new String[] { "Male", "Female", "Other" });
            genderCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            genderCombo.setBackground(new Color(245, 245, 245));
            genderCombo.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
            genderCombo.setSelectedItem(customer.getPersonGender() != null ? customer.getPersonGender() : "Other");
            genderCombo.setEnabled(false);
            JTextField phoneField = new JTextField(customer.getPhone() != null ? customer.getPhone() : "");
            JTextField dobField = new JTextField(customer.getDateOfBirth() != null ? customer.getDateOfBirth() : "");
            JTextField addressField = new JTextField(customer.getAddress() != null ? customer.getAddress() : "");
            JTextArea descriptionField = new JTextArea(customer.getDescription() != null ? customer.getDescription() : "", 3, 20);
            JTextField pointsField = new JTextField(String.valueOf(customer.getPoints()));

            descriptionField.setLineWrap(true);
            descriptionField.setWrapStyleWord(true);
            descriptionField.setEditable(false);
            JScrollPane descriptionScroll = new JScrollPane(descriptionField);
            descriptionScroll.setPreferredSize(new Dimension(200, 60));

            JComponent[] fields = { firstNameField, middleNameField, lastNameField, genderCombo, dobField, phoneField, addressField, descriptionScroll, pointsField };
            for (JComponent field : fields) {
                if (field instanceof JTextField) {
                    ((JTextField) field).setEditable(false);
                    field.setBackground(new Color(245, 245, 245));
                    field.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
                } else if (field instanceof JScrollPane) {
                    ((JTextArea) ((JScrollPane) field).getViewport().getView()).setEditable(false);
                    field.setBackground(new Color(245, 245, 245));
                    field.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
                }
            }

            addDialogFields(dialog,
                    new String[] { "First Name:", "Middle Name:", "Last Name:", "Gender:", "Date of Birth:", "Phone:", "Address:", "Description:", "Points:" },
                    fields);

            JButton backButton = new JButton("Back");
            backButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            backButton.setBackground(new Color(220, 220, 220));
            backButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
            backButton.addActionListener(e -> dialog.dispose());

            JPanel buttonPanel = (JPanel) dialog.getContentPane().getComponent(1);
            buttonPanel.add(backButton);

            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
        } catch (Exception e) {
            showError("Error loading customer details: " + e.getMessage());
        }
    }

    private void editCustomer(int row, String code) {
        try {
            Customer customer = personService.getAllCustomers(1, Integer.MAX_VALUE).stream()
                    .filter(c -> c.getCustomerNumber().equals(code)).findFirst()
                    .orElseThrow(() -> new Exception("Customer not found"));

            JDialog dialog = createDialog("Edit Customer", 400, 450);
            dialog.setLocationRelativeTo(null);

            JTextField firstNameField = new JTextField(customer.getPersonFirstName() != null ? customer.getPersonFirstName() : "");
            JTextField middleNameField = new JTextField(customer.getPersonMiddleName() != null ? customer.getPersonMiddleName() : "");
            JTextField lastNameField = new JTextField(customer.getPersonLastName() != null ? customer.getPersonLastName() : "");
            JComboBox<String> genderCombo = new JComboBox<>(new String[] { "Male", "Female", "Other" });
            genderCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            genderCombo.setBackground(Color.WHITE);
            genderCombo.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
            genderCombo.setSelectedItem(customer.getPersonGender() != null ? customer.getPersonGender() : "Other");
            JTextField dobField = new JTextField(customer.getDateOfBirth() != null ? customer.getDateOfBirth() : "");
            JTextField phoneField = new JTextField(customer.getPhone() != null ? customer.getPhone() : "");
            JTextField addressField = new JTextField(customer.getAddress() != null ? customer.getAddress() : "");
            JTextArea descriptionField = new JTextArea(customer.getDescription() != null ? customer.getDescription() : "", 3, 20);

            descriptionField.setLineWrap(true);
            descriptionField.setWrapStyleWord(true);
            JScrollPane descriptionScroll = new JScrollPane(descriptionField);
            descriptionScroll.setPreferredSize(new Dimension(200, 60));

            addDialogFields(dialog,
                    new String[] { "First Name:", "Middle Name:", "Last Name:", "Gender:", "Date of Birth:", "Phone:", "Address:", "Description:" },
                    new JComponent[] { firstNameField, middleNameField, lastNameField, genderCombo, dobField, phoneField, addressField, descriptionScroll });

            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(e -> {
                try {
                    String phone = phoneField.getText().trim();
                    if (!isPhoneValid(phone, customer.getPersonId())) {
                        return;
                    }

                    String dob = dobField.getText().trim();
                    if (!dob.isEmpty()) {
                        if (!Pattern.matches("\\d{4}-\\d{2}-\\d{2}", dob)) {
                            showError("Date of Birth must be in YYYY-MM-DD format (e.g., 2003-02-22)");
                            return;
                        }
                        try {
                            LocalDate date = LocalDate.parse(dob);
                            int month = date.getMonthValue();
                            int day = date.getDayOfMonth();
                            if (month < 1 || month > 12) {
                                showError("Month must be between 1 and 12");
                                return;
                            }
                            int maxDays = date.getMonth().length(date.isLeapYear());
                            if (day < 1 || day > maxDays) {
                                showError("Day must be between 1 and " + maxDays + " for the given month");
                                return;
                            }
                        } catch (DateTimeParseException ex) {
                            showError("Invalid date format: " + ex.getMessage());
                            return;
                        }
                    }

                    customer.setPersonFirstName(firstNameField.getText());
                    customer.setPersonMiddleName(middleNameField.getText());
                    customer.setPersonLastName(lastNameField.getText());
                    customer.setPersonGender((String) genderCombo.getSelectedItem());
                    customer.setDateOfBirth(dob);
                    customer.setPhone(phone);
                    customer.setAddress(addressField.getText());
                    customer.setDescription(descriptionField.getText());

                    personService.updateCustomer(customer);
                    loadCustomerData();
                    dialog.dispose();
                } catch (Exception ex) {
                    showError("Error updating customer: " + ex.getMessage());
                }
            });

            addDialogButtons(dialog, saveButton);
        } catch (Exception e) {
            showError("Error loading customer data: " + e.getMessage());
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
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 10));

        dialog.add(contentPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        return dialog;
    }

    private void addDialogFields(JDialog dialog, String[] labels, JComponent[] components) {
        JPanel contentPanel = (JPanel) dialog.getContentPane().getComponent(0);

        for (int i = 0; i < labels.length; i++) {
            JLabel label = new JLabel(labels[i]);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            components[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
            if (components[i] instanceof JTextField) {
                components[i].setPreferredSize(new Dimension(200, 30));
                components[i].setBorder(
                        BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)),
                                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
            } else if (components[i] instanceof JScrollPane) {
                ((JTextArea) ((JScrollPane) components[i]).getViewport().getView()).setFont(new Font("Segoe UI", Font.PLAIN, 14));
                components[i].setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
            } else if (components[i] instanceof JComboBox) {
                ((JComboBox<?>) components[i]).setBackground(Color.WHITE);
                ((JComboBox<?>) components[i]).setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
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
        cancelButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        cancelButton.addActionListener(e -> dialog.dispose());

        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.setBackground(new Color(70, 130, 180));
        saveButton.setForeground(Color.WHITE);
        saveButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private class ActionCellRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            if (row >= table.getModel().getRowCount() || table.getModel().getValueAt(row, column) == null) {
                return new JPanel();
            }
            return createActionPanel(row, null);
        }
    }

    private class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private int row;

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            panel = createActionPanel(row, this);
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }

        @Override
        public boolean stopCellEditing() {
            if (panel != null) {
                panel.removeAll();
                panel = null;
            }
            return super.stopCellEditing();
        }

        @Override
        public void cancelCellEditing() {
            if (panel != null) {
                panel.removeAll();
                panel = null;
            }
            super.cancelCellEditing();
        }
    }

    private JPanel createActionPanel(int row, ActionCellEditor editor) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel.setBackground(Color.WHITE);

        JButton editButton = new JButton("Edit");
        editButton.setBackground(new Color(255, 165, 0));
        editButton.setForeground(Color.WHITE);
        editButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        editButton.setPreferredSize(new Dimension(70, 50));
        editButton.setMargin(new Insets(2, 5, 2, 5));
        editButton.addActionListener(evt -> {
            int modelRow = customerTable.convertRowIndexToModel(row);
            String code = (String) tableModel.getValueAt(modelRow, 0);
            editCustomer(modelRow, code);
            if (editor != null) editor.stopCellEditing();
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(new Color(255, 0, 0));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        deleteButton.setPreferredSize(new Dimension(70, 50));
        deleteButton.setMargin(new Insets(2, 5, 2, 5));
        deleteButton.addActionListener(evt -> {
            int modelRow = customerTable.convertRowIndexToModel(row);
            if (modelRow >= tableModel.getRowCount()) return;
            String code = (String) tableModel.getValueAt(modelRow, 0);
            String name = (String) tableModel.getValueAt(modelRow, 1);

            int confirm = JOptionPane.showConfirmDialog(CustomerPanel.this,
                    "Are you sure you want to delete " + name + " (" + code + ")?", "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Customer customer = personService.getAllCustomers(1, Integer.MAX_VALUE).stream()
                            .filter(c -> c.getCustomerNumber().equals(code))
                            .findFirst()
                            .orElseThrow(() -> new Exception("Customer not found"));
                    personService.deleteCustomer(customer.getPersonId());
                    SwingUtilities.invokeLater(() -> {
                        loadCustomerData();
                        customerTable.revalidate();
                        customerTable.repaint();
                        if (editor != null) editor.stopCellEditing();
                    });
                } catch (Exception ex) {
                    showError("Error deleting customer: " + ex.getMessage());
                }
            }
            if (editor != null) editor.stopCellEditing();
        });

        JButton detailButton = new JButton("Detail");
        detailButton.setBackground(new Color(70, 130, 180));
        detailButton.setForeground(Color.WHITE);
        detailButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        detailButton.setPreferredSize(new Dimension(70, 50));
        detailButton.setMargin(new Insets(2, 5, 2, 5));
        detailButton.addActionListener(evt -> {
            int modelRow = customerTable.convertRowIndexToModel(row);
            String code = (String) tableModel.getValueAt(modelRow, 0);
            showCustomerDetail(modelRow, code);
            if (editor != null) editor.stopCellEditing();
        });

        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(detailButton);
        return panel;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}