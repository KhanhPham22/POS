package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import model.OrderDetail;
import service.OrderService;
import service.OrderServiceImpl;
import dao.OrderDetailDao;
import model.Customer; // Add this import for Customer type

public class OrderPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTable orderTable;
    private DefaultTableModel tableModel;
    private JButton prevButton;
    private JButton nextButton;
    private JLabel pageLabel;
    private OrderService orderService;
    private int currentPage = 1;
    private final int pageSize = 10; // Number of orders per page

    public OrderPanel() {
        // Initialize OrderService
        orderService = new OrderServiceImpl(new OrderDetailDao());

        // Set layout
        setLayout(new BorderLayout(10, 10));

        // Initialize table
        String[] columnNames = {"Order ID", "Customer", "Product", "Quantity", "Total Amount", "Payment Method", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        orderTable = new JTable(tableModel);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        orderTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(orderTable);

        // Initialize pagination controls
        JPanel paginationPanel = new JPanel(new FlowLayout());
        prevButton = new JButton("Previous");
        nextButton = new JButton("Next");
        pageLabel = new JLabel("Page: 1");

        // Add pagination controls to panel
        paginationPanel.add(prevButton);
        paginationPanel.add(pageLabel);
        paginationPanel.add(nextButton);

        // Add components to OrderPanel
        add(scrollPane, BorderLayout.CENTER);
        add(paginationPanel, BorderLayout.SOUTH);

        // Load initial data
        loadOrders(currentPage, pageSize);

        // Action listeners for pagination
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage > 1) {
                    currentPage--;
                    loadOrders(currentPage, pageSize);
                    updatePaginationControls();
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentPage++;
                loadOrders(currentPage, pageSize);
                updatePaginationControls();
            }
        });
    }

    private void loadOrders(int pageNumber, int pageSize) {
        // Clear existing table data
        tableModel.setRowCount(0);

        // Fetch orders from OrderService
        List<OrderDetail> orders = orderService.getAllOrders(pageNumber, pageSize);
        if (orders != null && !orders.isEmpty()) {
            // Populate table with order data
            for (OrderDetail order : orders) {
                // Construct customer full name
                String customerName = "N/A";
                if (order.getCustomer() != null) {
                    Customer customer = order.getCustomer();
                    StringBuilder fullName = new StringBuilder();
                    String firstName = customer.getCustomerFirstName();
                    String middleName = customer.getPersonMiddleName();
                    String lastName = customer.getPersonLastName();
                     // Use method from Person
                    if (firstName != null && !firstName.isEmpty()) {
                        fullName.append(firstName);
                    }
                    if (middleName != null && !middleName.isEmpty()) {
                        if (fullName.length() > 0) fullName.append(" ");
                        fullName.append(middleName);
                    }
                    if (lastName != null && !lastName.isEmpty()) {
                        if (fullName.length() > 0) fullName.append(" ");
                        fullName.append(lastName);
                    }
                    customerName = fullName.length() > 0 ? fullName.toString() : customer.getCustomerNumber() != null ? customer.getCustomerNumber() : "Unknown";
                }

                Object[] rowData = {
                    order.getId(),
                    customerName,
                    order.getProduct() != null ? order.getProduct().getName() : "N/A",
                    order.getQuantity(),
                    order.getTotalAmount(),
                    order.getPaymentMethod(),
                    order.getStatus()
                };
                tableModel.addRow(rowData);
            }
        } else {
            // Disable "Next" button if no data is returned
            nextButton.setEnabled(false);
            if (pageNumber > 1) {
                currentPage--; // Revert to previous page if no data
                loadOrders(currentPage, pageSize);
            }
        }
    }

    private void updatePaginationControls() {
        // Update page label
        pageLabel.setText("Page: " + currentPage);

        // Enable/disable buttons
        prevButton.setEnabled(currentPage > 1);
        nextButton.setEnabled(true); // Assume more pages exist until proven otherwise
    }
}