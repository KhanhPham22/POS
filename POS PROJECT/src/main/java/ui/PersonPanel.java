package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import model.Employee;
import model.Owner;
import model.Person;
import service.HashService;
import service.PersonService;
import service.PersonServiceImpl;
import ui.Elements.SearchBar;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PersonPanel extends JPanel {

    private JComboBox<String> personTypeComboBox;
    private JTable personTable;
    private DefaultTableModel tableModel;
    private JButton addButton, editButton, deleteButton;
    private JLabel pageInfoLabel;
    private JButton prevButton, nextButton;
    private SearchBar searchBar;

    private int currentPage = 1;
    private int pageSize = 10;
    private int totalRecords = 0;
    private String currentSearchQuery = "";
    
    private final PersonService personService;
    private final HashService hashService;
    
    public PersonPanel(PersonService personService, HashService hashService) {
        this.personService = personService;
        this.hashService = hashService;
        initializeUI();
        loadPersonData();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Top panel with person type selection, search bar, and add button
        JPanel topPanel = new JPanel(new BorderLayout());
        
        // Person type selection
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        personTypeComboBox = new JComboBox<>(new String[]{"All Employees", "Management", "Sales Staff", "Owners"});
        personTypeComboBox.addActionListener(e -> {
            currentSearchQuery = "";
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
        searchBar.setPlaceholder("Search by name or username...");
        
        // Add button
        addButton = new JButton("+ Add Person");
        addButton.addActionListener(e -> showAddPersonDialog());
        
        topPanel.add(typePanel, BorderLayout.WEST);
        topPanel.add(searchBar, BorderLayout.CENTER);
        topPanel.add(addButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        
        // Table setup
        String[] columnNames = {"ID", "Full Name", "Username", "Type", "Contact", "Status", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0);
        
        personTable = new JTable(tableModel);
        personTable.setRowHeight(30);
        personTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        personTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        personTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        personTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        personTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        personTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        personTable.getColumnModel().getColumn(6).setPreferredWidth(150);
        
        JScrollPane scrollPane = new JScrollPane(personTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Bottom panel with pagination and action buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        // Pagination controls
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        prevButton = new JButton("Previous");
        prevButton.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadPersonData();
            }
        });
        
        nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            if (currentPage * pageSize < totalRecords) {
                currentPage++;
                loadPersonData();
            }
        });
        
        pageInfoLabel = new JLabel();
        
        paginationPanel.add(prevButton);
        paginationPanel.add(pageInfoLabel);
        paginationPanel.add(nextButton);
        bottomPanel.add(paginationPanel, BorderLayout.CENTER);
        
        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        editButton = new JButton("Edit");
        editButton.addActionListener(e -> editSelectedPerson());
        
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteSelectedPerson());
        
        actionPanel.add(editButton);
        actionPanel.add(deleteButton);
        bottomPanel.add(actionPanel, BorderLayout.EAST);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadPersonData() {
        try {
            tableModel.setRowCount(0);
            String selectedType = (String) personTypeComboBox.getSelectedItem();
            
            if (selectedType.equals("All Employees") || selectedType.equals("Management") || selectedType.equals("Sales Staff")) {
                List<Employee> employees = loadEmployees(selectedType);
                for (Employee emp : employees) {
                    if (matchesSearchQuery(emp)) {
                        tableModel.addRow(new Object[]{
                            emp.getEmployeeNumber(),
                            getFullName(emp),
                            emp.getLoginUsername(),
                            emp.getEmployeeType(),
                            emp.getPhone(),
                            emp.isEnabledFlag() ? "Active" : "Inactive",
                            "Actions"
                        });
                    }
                }
            } else if (selectedType.equals("Owners")) {
                List<Owner> owners = personService.getAllOwner(currentPage, pageSize);
                totalRecords = personService.getAllOwner(1, Integer.MAX_VALUE).size();
                
                for (Owner owner : owners) {
                    if (matchesSearchQuery(owner)) {
                        tableModel.addRow(new Object[]{
                            owner.getOwnerNumber(),
                            getFullName(owner),
                            owner.getLoginUsername(),
                            "Owner",
                            owner.getPhone(),
                            owner.isEnabledFlag() ? "Active" : "Inactive",
                            "Actions"
                        });
                    }
                }
            }
            
            updatePaginationInfo();
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
            String employeeType = selectedType.equals("Management") ? "Quản lý" : "Nhân viên bán hàng";
            employees = personService.getAllEmployees(currentPage, pageSize)
                    .stream()
                    .filter(e -> e.getEmployeeType() != null && e.getEmployeeType().equals(employeeType))
                    .toList();
            totalRecords = (int) personService.getAllEmployees(1, Integer.MAX_VALUE)
                    .stream()
                    .filter(e -> e.getEmployeeType() != null && e.getEmployeeType().equals(employeeType))
                    .count();
        }
        return employees;
    }
    
    private boolean matchesSearchQuery(Person person) {
        if (currentSearchQuery.isEmpty()) return true;
        String query = currentSearchQuery.toLowerCase();
        return getFullName(person).toLowerCase().contains(query) ||
               (person instanceof Employee ? ((Employee) person).getLoginUsername().toLowerCase().contains(query) : 
                ((Owner) person).getLoginUsername().toLowerCase().contains(query));
    }
    
    private String getFullName(Person person) {
        return (person.getPersonFirstName() != null ? person.getPersonFirstName() : "") + " " +
               (person.getPersonMiddleName() != null ? person.getPersonMiddleName() : "") + " " +
               (person.getPersonLastName() != null ? person.getPersonLastName() : "");
    }
    
    private void updatePaginationInfo() {
        int start = (currentPage - 1) * pageSize + 1;
        int end = Math.min(currentPage * pageSize, totalRecords);
        pageInfoLabel.setText(String.format("Showing %d-%d of %d", start, end, totalRecords));
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
        JDialog dialog = createDialog("Add New Employee", 400, 450);
        JTextField firstNameField = new JTextField();
        JTextField middleNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Quản lý", "Nhân viên bán hàng"});
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Active", "Inactive"});
        
        addDialogFields(dialog, new String[]{"First Name:", "Middle Name:", "Last Name:", "Phone:", "Email:", "Username:", "Password:", "Type:", "Status:"},
                new JComponent[]{firstNameField, middleNameField, lastNameField, phoneField, emailField, usernameField, passwordField, typeCombo, statusCombo});
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                String password = new String(passwordField.getPassword());
                if (password.isEmpty()) {
                    showError("Password cannot be empty");
                    return;
                }
                Employee emp = new Employee();
                emp.setPersonFirstName(firstNameField.getText());
                emp.setPersonMiddleName(middleNameField.getText());
                emp.setPersonLastName(lastNameField.getText());
                emp.setPhone(phoneField.getText());
                emp.setEmail(emailField.getText());
                emp.setLoginUsername(usernameField.getText());
                emp.setLoginPassword(hashService.hash(password));
                emp.setEmployeeType((String)typeCombo.getSelectedItem());
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
        JDialog dialog = createDialog("Add New Owner", 400, 450);
        JTextField firstNameField = new JTextField();
        JTextField middleNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Active", "Inactive"});
        
        addDialogFields(dialog, new String[]{"First Name:", "Middle Name:", "Last Name:", "Phone:", "Email:", "Username:", "Password:", "Status:"},
                new JComponent[]{firstNameField, middleNameField, lastNameField, phoneField, emailField, usernameField, passwordField, statusCombo});
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            try {
                String password = new String(passwordField.getPassword());
                if (password.isEmpty()) {
                    showError("Password cannot be empty");
                    return;
                }
                Owner owner = new Owner();
                owner.setPersonFirstName(firstNameField.getText());
                owner.setPersonMiddleName(middleNameField.getText());
                owner.setPersonLastName(lastNameField.getText());
                owner.setPhone(phoneField.getText());
                owner.setEmail(emailField.getText());
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
        JDialog dialog = new JDialog((Frame)SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setSize(width, height);
        dialog.setLayout(new GridLayout(0, 2, 10, 10));
        dialog.setLocationRelativeTo(this);
        return dialog;
    }
    
    private void addDialogFields(JDialog dialog, String[] labels, JComponent[] components) {
        for (int i = 0; i < labels.length; i++) {
            dialog.add(new JLabel(labels[i]));
            dialog.add(components[i]);
        }
    }
    
    private void addDialogButtons(JDialog dialog, JButton saveButton) {
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);
        
        dialog.add(new JLabel());
        dialog.add(buttonPanel);
        
        dialog.pack();
        dialog.setVisible(true);
    }
    
    private void editSelectedPerson() {
        int selectedRow = personTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Please select a person to edit");
            return;
        }
        
        String personType = (String) tableModel.getValueAt(selectedRow, 3);
        String code = (String) tableModel.getValueAt(selectedRow, 0);
        if (personType.equals("Owner")) {
            editOwner(selectedRow, code);
        } else {
            editEmployee(selectedRow, code);
        }
    }
    
    private void editEmployee(int row, String code) {
        try {
            Employee emp = personService.getAllEmployees(1, Integer.MAX_VALUE)
                    .stream()
                    .filter(e -> e.getEmployeeNumber().equals(code))
                    .findFirst()
                    .orElseThrow(() -> new Exception("Employee not found"));
            
            JDialog dialog = createDialog("Edit Employee", 400, 450);
            JTextField firstNameField = new JTextField(emp.getPersonFirstName() != null ? emp.getPersonFirstName() : "");
            JTextField middleNameField = new JTextField(emp.getPersonMiddleName() != null ? emp.getPersonMiddleName() : "");
            JTextField lastNameField = new JTextField(emp.getPersonLastName() != null ? emp.getPersonLastName() : "");
            JTextField phoneField = new JTextField(emp.getPhone() != null ? emp.getPhone() : "");
            JTextField emailField = new JTextField(emp.getEmail() != null ? emp.getEmail() : "");
            JTextField usernameField = new JTextField(emp.getLoginUsername() != null ? emp.getLoginUsername() : "");
            JPasswordField passwordField = new JPasswordField();
            JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Quản lý", "Nhân viên bán hàng"});
            typeCombo.setSelectedItem(emp.getEmployeeType());
            JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Active", "Inactive"});
            statusCombo.setSelectedItem(emp.isEnabledFlag() ? "Active" : "Inactive");
            
            addDialogFields(dialog, new String[]{"First Name:", "Middle Name:", "Last Name:", "Phone:", "Email:", "Username:", "Password (leave blank to keep unchanged):", "Type:", "Status:"},
                    new JComponent[]{firstNameField, middleNameField, lastNameField, phoneField, emailField, usernameField, passwordField, typeCombo, statusCombo});
            
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(e -> {
                try {
                    emp.setPersonFirstName(firstNameField.getText());
                    emp.setPersonMiddleName(middleNameField.getText());
                    emp.setPersonLastName(lastNameField.getText());
                    emp.setPhone(phoneField.getText());
                    emp.setEmail(emailField.getText());
                    emp.setLoginUsername(usernameField.getText());
                    String password = new String(passwordField.getPassword());
                    if (!password.isEmpty()) {
                        emp.setLoginPassword(hashService.hash(password));
                    }
                    emp.setEmployeeType((String)typeCombo.getSelectedItem());
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
            Owner owner = personService.getAllOwner(1, Integer.MAX_VALUE)
                    .stream()
                    .filter(o -> o.getOwnerNumber().equals(code))
                    .findFirst()
                    .orElseThrow(() -> new Exception("Owner not found"));
            
            JDialog dialog = createDialog("Edit Owner", 400, 450);
            JTextField firstNameField = new JTextField(owner.getPersonFirstName() != null ? owner.getPersonFirstName() : "");
            JTextField middleNameField = new JTextField(owner.getPersonMiddleName() != null ? owner.getPersonMiddleName() : "");
            JTextField lastNameField = new JTextField(owner.getPersonLastName() != null ? owner.getPersonLastName() : "");
            JTextField phoneField = new JTextField(owner.getPhone() != null ? owner.getPhone() : "");
            JTextField emailField = new JTextField(owner.getEmail() != null ? owner.getEmail() : "");
            JTextField usernameField = new JTextField(owner.getLoginUsername() != null ? owner.getLoginUsername() : "");
            JPasswordField passwordField = new JPasswordField();
            JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Active", "Inactive"});
            statusCombo.setSelectedItem(owner.isEnabledFlag() ? "Active" : "Inactive");
            
            addDialogFields(dialog, new String[]{"First Name:", "Middle Name:", "Last Name:", "Phone:", "Email:", "Username:", "Password (leave blank to keep unchanged):", "Status:"},
                    new JComponent[]{firstNameField, middleNameField, lastNameField, phoneField, emailField, usernameField, passwordField, statusCombo});
            
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(e -> {
                try {
                    owner.setPersonFirstName(firstNameField.getText());
                    owner.setPersonMiddleName(middleNameField.getText());
                    owner.setPersonLastName(lastNameField.getText());
                    owner.setPhone(phoneField.getText());
                    owner.setEmail(emailField.getText());
                    owner.setLoginUsername(usernameField.getText());
                    String password = new String(passwordField.getPassword());
                    if (!password.isEmpty()) {
                        owner.setLoginPassword(hashService.hash(password));
                    }
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
    
    private void deleteSelectedPerson() {
        int selectedRow = personTable.getSelectedRow();
        if (selectedRow == -1) {
            showWarning("Please select a person to delete");
            return;
        }
        
        String personType = (String) tableModel.getValueAt(selectedRow, 3);
        String code = (String) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete " + name + " (" + code + ")?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (personType.equals("Owner")) {
                    Owner owner = personService.getAllOwner(1, Integer.MAX_VALUE)
                            .stream()
                            .filter(o -> o.getOwnerNumber().equals(code))
                            .findFirst()
                            .orElseThrow(() -> new Exception("Owner not found"));
                    personService.deleteOwner(owner.getPersonId());
                } else {
                    Employee emp = personService.getAllEmployees(1, Integer.MAX_VALUE)
                            .stream()
                            .filter(e -> e.getEmployeeNumber().equals(code))
                            .findFirst()
                            .orElseThrow(() -> new Exception("Employee not found"));
                    personService.deleteEmployee(emp.getPersonId());
                }
                
                loadPersonData();
            } catch (Exception e) {
                showError("Error deleting person: " + e.getMessage());
            }
        }
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }
}