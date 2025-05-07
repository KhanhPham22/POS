package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import model.Employee;
import model.Owner;
import model.Person;
import service.AuthenticationService;
import service.HashService;
import service.PersonService;
import service.PersonServiceImpl;
import ui.Elements.SearchBar;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.List;
import java.util.regex.Pattern;

public class PersonPanel extends JPanel {

    private JComboBox<String> personTypeComboBox;
    private JTable personTable;
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
    private final HashService hashService;
    private final AuthenticationService authService;

    public PersonPanel(PersonService personService, HashService hashService, AuthenticationService authService) {
        this.personService = personService;
        this.hashService = hashService;
        this.authService = authService;
        initializeUI();
        loadPersonData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());

        // Top panel with person type selection, search bar, and add button
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Person type selection
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        personTypeComboBox = new JComboBox<>(new String[] { "All Employees", "Full-time", "Part-time", "Owners" });
        personTypeComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        personTypeComboBox.setBackground(Color.WHITE);
        personTypeComboBox.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
        personTypeComboBox.setPreferredSize(new Dimension(200, 40));
        personTypeComboBox.addActionListener(e -> {
            currentSearchQuery = "";
            currentPage = 1;
            loadPersonData();
        });
        typePanel.add(new JLabel("Person Type:"));
        typePanel.add(personTypeComboBox);

        // Search bar
        searchBar = new SearchBar(query -> {
            currentSearchQuery = query;
            currentPage = 1;
            loadPersonData();
        });
        searchBar.setPlaceholder("Search by name or username");
        searchBar.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchBar.setBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        searchBar.setBackground(new Color(245, 245, 245));
        searchBar.setPreferredSize(new Dimension(400, 40));

        // Add button
        addButton = new JButton("+ Thêm nhân viên");
        addButton.setBackground(new Color(50, 205, 50));
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        addButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addButton.addActionListener(e -> showAddPersonDialog());

        topPanel.add(typePanel, BorderLayout.WEST);
        topPanel.add(searchBar, BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Table setup with new columns
        String[] columnNames = { "ID", "Full Name", "Username", "Type", "Gender", "Date of Birth", "Email", "Contact",
                "Address", "City", "State", "Status", "Actions" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 12; // Allow editing only for the Actions column
            }
        };

        personTable = new JTable(tableModel);
        personTable.setFillsViewportHeight(true); // Ensure table fills the viewport height
        personTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Set custom renderer and editor for the Actions column
        personTable.getColumnModel().getColumn(12).setCellRenderer(new ActionCellRenderer());
        personTable.getColumnModel().getColumn(12).setCellEditor(new ActionCellEditor());

        // Custom renderer for status column
        personTable.getColumnModel().getColumn(11).setCellRenderer(new StatusCellRenderer());

        // Make header bold
        personTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));

        personTable.setRowHeight(60);
        // Adjusted column widths to accommodate new columns
        personTable.getColumnModel().getColumn(0).setPreferredWidth(80); // ID
        personTable.getColumnModel().getColumn(1).setPreferredWidth(140); // Full Name
        personTable.getColumnModel().getColumn(2).setPreferredWidth(120); // Username
        personTable.getColumnModel().getColumn(3).setPreferredWidth(80); // Type
        personTable.getColumnModel().getColumn(4).setPreferredWidth(80); // Gender
        personTable.getColumnModel().getColumn(5).setPreferredWidth(120); // Date of Birth
        personTable.getColumnModel().getColumn(6).setPreferredWidth(150); // Email
        personTable.getColumnModel().getColumn(7).setPreferredWidth(100); // Contact
        personTable.getColumnModel().getColumn(8).setPreferredWidth(100); // Address
        personTable.getColumnModel().getColumn(9).setPreferredWidth(100); // City
        personTable.getColumnModel().getColumn(10).setPreferredWidth(100); // State
        personTable.getColumnModel().getColumn(11).setPreferredWidth(100); // Status
        personTable.getColumnModel().getColumn(12).setPreferredWidth(150); // Actions

        JScrollPane scrollPane = new JScrollPane(personTable);
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
                loadPersonData();
            }
        });

        nextButton = new JButton("Next");
        nextButton.setBackground(new Color(70, 130, 180));
        nextButton.setForeground(Color.WHITE);
        nextButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nextButton.addActionListener(e -> {
            if (currentPage * pageSize < totalRecords) {
                currentPage++;
                loadPersonData();
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

    // Custom renderer for status column
    private class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            JLabel label = (JLabel) c;

            if (value.equals("Active")) {
                label.setBackground(new Color(144, 238, 144)); // Light green
                label.setForeground(Color.BLACK);
                label.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 1)); // Darker green border
            } else {
                label.setBackground(new Color(216, 191, 216)); // Light purple
                label.setForeground(Color.BLACK);
                label.setBorder(BorderFactory.createLineBorder(new Color(128, 0, 128), 1)); // Darker purple border
            }

            label.setHorizontalAlignment(SwingConstants.CENTER);
            return c;
        }
    }

    // Custom renderer for the Actions column
    private class ActionCellRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            if (row >= table.getModel().getRowCount() || table.getModel().getValueAt(row, column) == null) {
                return new JPanel(); // Return empty panel if row or cell is invalid
            }
            return createActionPanel(row, null);
        }
    }

    // Custom editor for the Actions column
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
                panel.removeAll(); // Clear all components from the panel
                panel = null; // Clear the panel reference
            }
            return super.stopCellEditing();
        }

        @Override
        public void cancelCellEditing() {
            if (panel != null) {
                panel.removeAll(); // Clear all components from the panel
                panel = null; // Clear the panel reference
            }
            super.cancelCellEditing();
        }
    }

    private JPanel createActionPanel(int row, ActionCellEditor editor) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        panel.setBackground(Color.WHITE);

        JButton editButton = new JButton("Edit");
        editButton.setBackground(new Color(255, 165, 0));
        editButton.setForeground(Color.WHITE);
        editButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        editButton.setPreferredSize(new Dimension(60, 30));
        editButton.setMargin(new Insets(2, 5, 2, 5));
        editButton.addActionListener(evt -> {
            int modelRow = personTable.convertRowIndexToModel(row);
            String personType = (String) tableModel.getValueAt(modelRow, 3);
            String code = (String) tableModel.getValueAt(modelRow, 0);
            if (personType.equals("Owner")) {
                editOwner(modelRow, code);
            } else {
                editEmployee(modelRow, code);
            }
            if (editor != null) editor.stopCellEditing();
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(new Color(255, 0, 0));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        deleteButton.setPreferredSize(new Dimension(60, 30));
        deleteButton.setMargin(new Insets(2, 5, 2, 5));
        deleteButton.addActionListener(evtDel -> {
            int modelRow = personTable.convertRowIndexToModel(row);
            if (modelRow >= tableModel.getRowCount()) return;
            String personType = (String) tableModel.getValueAt(modelRow, 3);
            String code = (String) tableModel.getValueAt(modelRow, 0);
            String name = (String) tableModel.getValueAt(modelRow, 1);

            int confirm = JOptionPane.showConfirmDialog(PersonPanel.this,
                    "Are you sure you want to delete " + name + " (" + code + ")?", "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    if (personType.equals("Owner")) {
                        Owner owner = personService.getAllOwner(1, Integer.MAX_VALUE).stream()
                                .filter(o -> o.getOwnerNumber().equals(code))
                                .findFirst()
                                .orElseThrow(() -> new Exception("Owner not found"));
                        personService.deleteOwner(owner.getPersonId());
                    } else {
                        Employee emp = personService.getAllEmployees(1, Integer.MAX_VALUE).stream()
                                .filter(empFilter -> empFilter.getEmployeeNumber().equals(code))
                                .findFirst()
                                .orElseThrow(() -> new Exception("Employee not found"));
                        personService.deleteEmployee(emp.getPersonId());
                    }
                    SwingUtilities.invokeLater(() -> {
                        loadPersonData();
                        personTable.revalidate();
                        personTable.repaint();
                        if (editor != null) editor.stopCellEditing();
                    });
                } catch (Exception ex) {
                    showError("Error deleting person: " + ex.getMessage());
                }
            }
            if (editor != null) editor.stopCellEditing();
        });

        panel.add(editButton);
        panel.add(deleteButton);
        return panel;
    }
    

    private void loadPersonData() {
        if (tableModel == null) {
            System.err.println("TableModel is null, cannot load data");
            return;
        }
        try {
            tableModel.setRowCount(0); // Clear old data
            String selectedType = (String) personTypeComboBox.getSelectedItem();

            if (selectedType.equals("All Employees") || selectedType.equals("Full-time")
                    || selectedType.equals("Part-time")) {
                List<Employee> employees = loadEmployees(selectedType);
                for (Employee emp : employees) {
                    if (matchesSearchQuery(emp)) {
                        tableModel.addRow(new Object[] { emp.getEmployeeNumber(), getFullName(emp),
                                emp.getLoginUsername(), emp.getEmployeeType(), emp.getPersonGender(),
                                emp.getDateOfBirth(), emp.getEmail(), emp.getPhone(), emp.getAddress(), emp.getCity(),
                                emp.getState(), emp.isEnabledFlag() ? "Active" : "Inactive", "" });
                    }
                }
            } else if (selectedType.equals("Owners")) {
                List<Owner> owners = personService.getAllOwner(currentPage, pageSize);
                totalRecords = personService.getAllOwner(1, Integer.MAX_VALUE).size();

                for (Owner owner : owners) {
                    if (matchesSearchQuery(owner)) {
                        tableModel.addRow(new Object[] { owner.getOwnerNumber(), getFullName(owner),
                                owner.getLoginUsername(), "Owner", owner.getPersonGender(), owner.getDateOfBirth(),
                                owner.getEmail(), owner.getPhone(), owner.getAddress(), owner.getCity(),
                                owner.getState(), owner.isEnabledFlag() ? "Active" : "Inactive", "" });
                    }
                }
            }

            updatePaginationInfo();
            // Ensure the table updates visually after loading data
            personTable.revalidate();
            personTable.repaint();
        } catch (Exception e) {
            showError("Error loading person data: " + e.getMessage());
        }
    }

    private List<Employee> loadEmployees(String selectedType) throws Exception {
        List<Employee> employees;
        if (selectedType.equals("All Employees")) {
            employees = personService.getAllEmployees(currentPage, pageSize);
            totalRecords = personService.getAllEmployees(1, Integer.MAX_VALUE).size();
        } else {
            String employeeType = selectedType;
            employees = personService.getAllEmployees(currentPage, pageSize).stream()
                    .filter(e -> e.getEmployeeType() != null && e.getEmployeeType().equals(employeeType)).toList();
            totalRecords = (int) personService.getAllEmployees(1, Integer.MAX_VALUE).stream()
                    .filter(e -> e.getEmployeeType() != null && e.getEmployeeType().equals(employeeType)).count();
        }
        return employees;
    }

    private boolean matchesSearchQuery(Person person) {
        if (currentSearchQuery.isEmpty())
            return true;
        String query = currentSearchQuery.toLowerCase();
        return getFullName(person).toLowerCase().contains(query)
                || (person instanceof Employee ? ((Employee) person).getLoginUsername().toLowerCase().contains(query)
                        : ((Owner) person).getLoginUsername().toLowerCase().contains(query));
    }

    private String getFullName(Person person) {
        return (person.getPersonFirstName() != null ? person.getPersonFirstName() : "") + " "
                + (person.getPersonMiddleName() != null ? person.getPersonMiddleName() : "") + " "
                + (person.getPersonLastName() != null ? person.getPersonLastName() : "");
    }

    private void updatePaginationInfo() {
        int start = (currentPage - 1) * pageSize + 1;
        int end = Math.min(currentPage * pageSize, totalRecords);
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        pageInfoLabel.setText(String.format("Page %d of %d (%d employees)", currentPage, totalPages, totalRecords));

        prevButton.setEnabled(currentPage > 1);
        nextButton.setEnabled(currentPage * pageSize < totalRecords);
    }

    private void showAddPersonDialog() {
        String selectedType = (String) personTypeComboBox.getSelectedItem();
        if (selectedType.equals("Owners")) {
            showAddOwnerDialog();
        } else {
            showAddEmployeeDialog();
        }
    }

    private void showAddEmployeeDialog() {
        JDialog dialog = createDialog("Add New Employee", 700, 650);
        // create field
        JTextField firstNameField = new JTextField();
        JTextField middleNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JComboBox<String> genderCombo = new JComboBox<>(new String[] { "Male", "Female", "Other" });
        JTextField dobField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField stateField = new JTextField();
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> typeCombo = new JComboBox<>(new String[] { "Full-time", "Part-time" });
        JComboBox<String> statusCombo = new JComboBox<>(new String[] { "Active", "Inactive" });

        // Apply consistent styling to all comboboxes
        for (JComboBox<?> combo : new JComboBox<?>[] { genderCombo, typeCombo, statusCombo }) {
            combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            combo.setBackground(Color.WHITE);
            combo.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        }

        addDialogFields(dialog,
                new String[] { "First Name:", "Middle Name:", "Last Name:", "Gender:", "Date of Birth:", "Phone:",
                        "Email:", "Address:", "City:", "State:", "Username:", "Password:", "Type:", "Status:" },
                new JComponent[] { firstNameField, middleNameField, lastNameField, genderCombo, dobField, phoneField,
                        emailField, addressField, cityField, stateField, usernameField, passwordField, typeCombo,
                        statusCombo });

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                String email = emailField.getText().trim();
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                String dob = dobField.getText().trim();

                // Validate date of birth format (YYYY-MM-DD)
                if (!dob.isEmpty() && !Pattern.matches("\\d{4}-\\d{2}-\\d{2}", dob)) {
                    showError("Date of Birth must be in YYYY-MM-DD format (e.g., 2003-02-22)");
                    return;
                }

                // Check for duplicate email
                if (!email.isEmpty() && personService.existsByEmail(email)) {
                    showError("Email '" + email + "' is already in use. Please use a different email.");
                    return;
                }

                // Check for duplicate username
                if (!username.isEmpty() && personService.existsByUsername(username)) {
                    showError("Username '" + username + "' is already in use. Please use a different username.");
                    return;
                }

                // Validate password
                if (password.isEmpty()) {
                    showError("Password cannot be empty");
                    return;
                }
                if (!authService.isStrongPassword(password)) {
                    showError(
                            "Password must be at least 6 characters long and include uppercase, lowercase, numbers, and special characters");
                    return;
                }
                Employee emp = new Employee();
                emp.setPersonFirstName(firstNameField.getText());
                emp.setPersonMiddleName(middleNameField.getText());
                emp.setPersonLastName(lastNameField.getText());
                emp.setPersonGender((String) genderCombo.getSelectedItem());
                emp.setDateOfBirth(dobField.getText());
                emp.setPhone(phoneField.getText());
                emp.setEmail(emailField.getText());
                emp.setAddress(addressField.getText());
                emp.setCity(cityField.getText());
                emp.setState(stateField.getText());
                emp.setLoginUsername(usernameField.getText());
                emp.setLoginPassword(hashService.hash(password));
                emp.setEmployeeType((String) typeCombo.getSelectedItem());
                emp.setEnabledFlag(statusCombo.getSelectedItem().equals("Active"));
                emp.generateEmployeeNumber();

                personService.createEmployee(emp);
                loadPersonData();
                dialog.dispose();
            } catch (Exception ex) {
                showError("Error saving employee: " + ex.getMessage());
            }
        });

        addDialogButtons(dialog, saveButton);
    }

    private void showAddOwnerDialog() {
        JDialog dialog = createDialog("Add New Owner", 700, 650);
        // Create fields
        JTextField firstNameField = new JTextField();
        JTextField middleNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JComboBox<String> genderCombo = new JComboBox<>(new String[] { "Male", "Female", "Other" });
        JTextField dobField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField stateField = new JTextField();
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> statusCombo = new JComboBox<>(new String[] { "Active", "Inactive" });

        // Apply consistent styling to all comboboxes
        for (JComboBox<?> combo : new JComboBox<?>[] { genderCombo, statusCombo }) {
            combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            combo.setBackground(Color.WHITE);
            combo.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        }

        addDialogFields(dialog,
                new String[] { "First Name:", "Middle Name:", "Last Name:", "Gender:", "Date of Birth:", "Phone:",
                        "Email:", "Address:", "City:", "State:", "Username:", "Password:", "Status:" },
                new JComponent[] { firstNameField, middleNameField, lastNameField, genderCombo, dobField, phoneField,
                        emailField, addressField, cityField, stateField, usernameField, passwordField, statusCombo });

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                String email = emailField.getText().trim();
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                String dob = dobField.getText().trim();

                // Validate date of birth format (YYYY-MM-DD)
                if (!dob.isEmpty() && !Pattern.matches("\\d{4}-\\d{2}-\\d{2}", dob)) {
                    showError("Date of Birth must be in YYYY-MM-DD format (e.g., 2003-02-22)");
                    return;
                }

                // Check for duplicate email
                if (!email.isEmpty() && personService.existsByEmail(email)) {
                    showError("Email '" + email + "' is already in use. Please use a different email.");
                    return;
                }

                // Check for duplicate username
                if (!username.isEmpty() && personService.existsByUsername(username)) {
                    showError("Username '" + username + "' is already in use. Please use a different username.");
                    return;
                }

                // Validate password
                if (password.isEmpty()) {
                    showError("Password cannot be empty");
                    return;
                }
                if (!authService.isStrongPassword(password)) {
                    showError(
                            "Password must be at least 6 characters long and include uppercase, lowercase, numbers, and special characters");
                    return;
                }
                Owner owner = new Owner();
                owner.setPersonFirstName(firstNameField.getText());
                owner.setPersonMiddleName(middleNameField.getText());
                owner.setPersonLastName(lastNameField.getText());
                owner.setPersonGender((String) genderCombo.getSelectedItem());
                owner.setDateOfBirth(dobField.getText());
                owner.setPhone(phoneField.getText());
                owner.setEmail(emailField.getText());
                owner.setAddress(addressField.getText());
                owner.setCity(cityField.getText());
                owner.setState(stateField.getText());
                owner.setLoginUsername(usernameField.getText());
                owner.setLoginPassword(hashService.hash(password));
                owner.setEnabledFlag(statusCombo.getSelectedItem().equals("Active"));
                owner.generateOwnerId();

                personService.createOwner(owner);
                loadPersonData();
                dialog.dispose();
            } catch (Exception ex) {
                showError("Error saving owner: " + ex.getMessage());
            }
        });

        addDialogButtons(dialog, saveButton);
    }

    private JDialog createDialog(String title, int width, int height) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setSize(width, height);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);

        // Main content panel with GridLayout for form fields
        JPanel contentPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        contentPanel.setBackground(Color.WHITE);

        // Button panel at bottom
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
            if (components[i] instanceof JTextField || components[i] instanceof JPasswordField) {
                components[i].setPreferredSize(new Dimension(200, 30));
                components[i].setBorder(
                        BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)),
                                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
            }
            if (components[i] instanceof JComboBox) {
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

    private void editEmployee(int row, String code) {
        try {
            Employee emp = personService.getAllEmployees(1, Integer.MAX_VALUE).stream()
                    .filter(e -> e.getEmployeeNumber().equals(code)).findFirst()
                    .orElseThrow(() -> new Exception("Employee not found"));

            JDialog dialog = createDialog("Edit Employee", 400, 550); // Reduced height as password field is removed
            dialog.setLocationRelativeTo(null); // Center the dialog on the screen
            JTextField firstNameField = new JTextField(
                    emp.getPersonFirstName() != null ? emp.getPersonFirstName() : "");
            JTextField middleNameField = new JTextField(
                    emp.getPersonMiddleName() != null ? emp.getPersonMiddleName() : "");
            JTextField lastNameField = new JTextField(emp.getPersonLastName() != null ? emp.getPersonLastName() : "");
            JComboBox<String> genderCombo = new JComboBox<>(new String[] { "Male", "Female", "Other" });
            genderCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            genderCombo.setSelectedItem(emp.getPersonGender() != null ? emp.getPersonGender() : "Other");
            JTextField dobField = new JTextField(emp.getDateOfBirth() != null ? emp.getDateOfBirth() : "");
            JTextField phoneField = new JTextField(emp.getPhone() != null ? emp.getPhone() : "");
            JTextField emailField = new JTextField(emp.getEmail() != null ? emp.getEmail() : "");
            JTextField addressField = new JTextField(emp.getAddress() != null ? emp.getAddress() : "");
            JTextField cityField = new JTextField(emp.getCity() != null ? emp.getCity() : "");
            JTextField stateField = new JTextField(emp.getState() != null ? emp.getState() : "");
            JTextField usernameField = new JTextField(emp.getLoginUsername() != null ? emp.getLoginUsername() : "");
            JComboBox<String> typeCombo = new JComboBox<>(new String[] { "Full-time", "Part-time" });
            typeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            typeCombo.setSelectedItem(emp.getEmployeeType());
            JComboBox<String> statusCombo = new JComboBox<>(new String[] { "Active", "Inactive" });
            statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            statusCombo.setSelectedItem(emp.isEnabledFlag() ? "Active" : "Inactive");

            addDialogFields(dialog,
                    new String[] { "First Name:", "Middle Name:", "Last Name:", "Gender:", "Date of Birth:", "Phone:",
                            "Email:", "Address:", "City:", "State:", "Username:", "Type:", "Status:" },
                    new JComponent[] { firstNameField, middleNameField, lastNameField, genderCombo, dobField,
                            phoneField, emailField, addressField, cityField, stateField, usernameField, typeCombo,
                            statusCombo });

            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(e -> {
                try {
                    emp.setPersonFirstName(firstNameField.getText());
                    emp.setPersonMiddleName(middleNameField.getText());
                    emp.setPersonLastName(lastNameField.getText());
                    emp.setPersonGender((String) genderCombo.getSelectedItem());
                    emp.setDateOfBirth(dobField.getText());
                    emp.setPhone(phoneField.getText());
                    emp.setEmail(emailField.getText());
                    emp.setAddress(addressField.getText());
                    emp.setCity(cityField.getText());
                    emp.setState(stateField.getText());
                    emp.setLoginUsername(usernameField.getText());
                    emp.setEmployeeType((String) typeCombo.getSelectedItem());
                    emp.setEnabledFlag(statusCombo.getSelectedItem().equals("Active"));

                    personService.updateEmployee(emp);
                    loadPersonData();
                    dialog.dispose();
                } catch (Exception ex) {
                    showError("Error updating employee: " + ex.getMessage());
                }
            });

            addDialogButtons(dialog, saveButton);
        } catch (Exception e) {
            showError("Error loading employee data: " + e.getMessage());
        }
    }

    private void editOwner(int row, String code) {
        try {
            Owner owner = personService.getAllOwner(1, Integer.MAX_VALUE).stream()
                    .filter(o -> o.getOwnerNumber().equals(code)).findFirst()
                    .orElseThrow(() -> new Exception("Owner not found"));

            JDialog dialog = createDialog("Edit Owner", 400, 550); // Reduced height as password field is removed
            dialog.setLocationRelativeTo(null); // Center the dialog on the screen
            JTextField firstNameField = new JTextField(
                    owner.getPersonFirstName() != null ? owner.getPersonFirstName() : "");
            JTextField middleNameField = new JTextField(
                    owner.getPersonMiddleName() != null ? owner.getPersonMiddleName() : "");
            JTextField lastNameField = new JTextField(
                    owner.getPersonLastName() != null ? owner.getPersonLastName() : "");
            JComboBox<String> genderCombo = new JComboBox<>(new String[] { "Male", "Female", "Other" });
            genderCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            genderCombo.setSelectedItem(owner.getPersonGender() != null ? owner.getPersonGender() : "Other");
            JTextField dobField = new JTextField(owner.getDateOfBirth () != null ? owner.getDateOfBirth() : "");
            JTextField phoneField = new JTextField(owner.getPhone() != null ? owner.getPhone() : "");
            JTextField emailField = new JTextField(owner.getEmail() != null ? owner.getEmail() : "");
            JTextField addressField = new JTextField(owner.getAddress() != null ? owner.getAddress() : "");
            JTextField cityField = new JTextField(owner.getCity() != null ? owner.getCity() : "");
            JTextField stateField = new JTextField(owner.getState() != null ? owner.getState() : "");
            JTextField usernameField = new JTextField(owner.getLoginUsername() != null ? owner.getLoginUsername() : "");
            JComboBox<String> statusCombo = new JComboBox<>(new String[] { "Active", "Inactive" });
            statusCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            statusCombo.setSelectedItem(owner.isEnabledFlag() ? "Active" : "Inactive");

            addDialogFields(dialog,
                    new String[] { "First Name:", "Middle Name:", "Last Name:", "Gender:", "Date of Birth:", "Phone:",
                            "Email:", "Address:", "City:", "State:", "Username:", "Status:" },
                    new JComponent[] { firstNameField, middleNameField, lastNameField, genderCombo, dobField,
                            phoneField, emailField, addressField, cityField, stateField, usernameField, statusCombo });

            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(e -> {
                try {
                    owner.setPersonFirstName(firstNameField.getText());
                    owner.setPersonMiddleName(middleNameField.getText());
                    owner.setPersonLastName(lastNameField.getText());
                    owner.setPersonGender((String) genderCombo.getSelectedItem());
                    owner.setDateOfBirth(dobField.getText());
                    owner.setPhone(phoneField.getText());
                    owner.setEmail(emailField.getText());
                    owner.setAddress(addressField.getText());
                    owner.setCity(cityField.getText());
                    owner.setState(stateField.getText());
                    owner.setLoginUsername(usernameField.getText());
                    owner.setEnabledFlag(statusCombo.getSelectedItem().equals("Active"));

                    personService.updateOwner(owner);
                    loadPersonData();
                    dialog.dispose();
                } catch (Exception ex) {
                    showError("Error updating owner: " + ex.getMessage());
                }
            });

            addDialogButtons(dialog, saveButton);
        } catch (Exception e) {
            showError("Error loading owner data: " + e.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}