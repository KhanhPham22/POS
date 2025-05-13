package ui;

import javax.swing.*;

import model.Item;
import model.Supplier;
import service.ItemService;
import service.ItemServiceImpl;
import service.SupplierService;
import service.SupplierServiceImpl;

import java.awt.*;

/**
 * ItemPanel is a JDialog used for creating or editing an Item that belongs to a specific Supplier.
 */
public class ItemPanel extends JDialog {
    // Input fields for item details
    private JTextField txtId, txtName, txtType, txtUnit, txtDescription, txtQuantity;
    private JButton btnSave, btnCancel; // Buttons for saving or canceling the operation
    private Item item; // The item being created or edited
    private Supplier supplier; // The supplier associated with the item
    private ItemService itemService; // Service for item-related operations (create/update)
    private SupplierPanel supplierPanel; // Reference to parent panel to refresh UI after changes

    /**
     * Constructor to initialize the dialog for adding or editing an item.
     * 
     * @param item The item to edit, or null if creating a new one
     * @param supplier The supplier this item belongs to
     * @param itemService The service to handle item logic
     * @param supplierPanel The parent panel to refresh after saving
     */
    public ItemPanel(Item item, Supplier supplier, ItemService itemService, SupplierPanel supplierPanel) {
        super((Frame) null, item == null ? "Add New Item" : "Edit Item", true);
        this.supplier = supplier;
        this.itemService = itemService;
        this.supplierPanel = supplierPanel;
        this.item = (item == null) ? new Item() : item;
        initUI();
        setLocationRelativeTo(null);
        setSize(400, 350);
    }

    // Initialize the user interface
    private void initUI() {
        setLayout(new BorderLayout());

        // Main container
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form panel with all fields
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 5, 5));

        txtId = new JTextField();
        txtName = new JTextField();
        txtType = new JTextField();
        txtUnit = new JTextField();
        txtDescription = new JTextField();
        txtQuantity = new JTextField();

        formPanel.add(new JLabel("Item ID:"));
        formPanel.add(txtId);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("Type:"));
        formPanel.add(txtType);
        formPanel.add(new JLabel("Unit:"));
        formPanel.add(txtUnit);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(txtDescription);
        formPanel.add(new JLabel("Quantity:"));
        formPanel.add(txtQuantity);

        mainPanel.add(formPanel);

        // Panel for action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnCancel = new JButton("Cancel");
        btnCancel.setBackground(new Color(231, 76, 60)); // Red background
        btnCancel.addActionListener(e -> {
            dispose(); // Close dialog
            supplierPanel.setVisible(true); // Show the parent panel again
        });

        btnSave = new JButton("Save");
        btnSave.setBackground(new Color(46, 204, 113)); // Green background
        btnSave.addActionListener(e -> saveItem()); // Trigger save logic

        buttonPanel.add(btnCancel);
        buttonPanel.add(btnSave);

        // Add components to dialog
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Save or update item based on input fields
    private void saveItem() {
        try {
            int quantity = Integer.parseInt(txtQuantity.getText().trim());
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Quantity must be greater than 0!");
                return;
            }

            if (item.getId() == 0) {
                // If it's a new item (id = 0), parse the entered ID
                long id = Long.parseLong(txtId.getText().trim());
                if (id <= 0) {
                    JOptionPane.showMessageDialog(this, "ID must be a positive number!");
                    return;
                }
                item.setId(id);
            }

            // Set item properties from input fields
            item.setName(txtName.getText().trim());
            item.setType(txtType.getText().trim());
            item.setUnit(txtUnit.getText().trim());
            item.setDescription(txtDescription.getText().trim());
            item.setQuantity(quantity);
            item.setSupplier(supplier);

            // Save or update the item using the service
            boolean success;
            if (itemService.getItem(item.getId()) != null) {
                success = itemService.updateItem(item); // Update existing item
            } else {
                success = itemService.createItem(item); // Create new item
            }

            if (success) {
                JOptionPane.showMessageDialog(this, "Item saved successfully!");
                supplierPanel.refreshItems(); // Refresh item list in parent panel
                dispose(); // Close dialog
                supplierPanel.setVisible(true); // Show parent panel
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save item!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers!");
        }
    }

    /**
     * Load item details into input fields for editing.
     * 
     * @param currentItem The item to load
     */
    public void setItem(Item currentItem) {
        if (currentItem != null) {
            this.item = currentItem;
            txtId.setText(String.valueOf(item.getId()));
            txtName.setText(item.getName() != null ? item.getName() : "");
            txtType.setText(item.getType() != null ? item.getType() : "");
            txtUnit.setText(item.getUnit() != null ? item.getUnit() : "");
            txtDescription.setText(item.getDescription() != null ? item.getDescription() : "");
            txtQuantity.setText(String.valueOf(item.getQuantity()));
            txtId.setEditable(false); // ID should not be editable when editing
        }
    }
}
