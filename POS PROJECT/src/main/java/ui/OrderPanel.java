package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import model.OrderDetail;
import model.OrderItem;
import model.Product;
import model.Invoice;
import model.Customer;
import service.InvoiceService;
import service.OrderService;
import service.PaymentService;
import com.toedter.calendar.JCalendar;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ui.Elements.SearchBar;

public class OrderPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#,###.000 VND");
    private static final Logger logger = LogManager.getLogger(OrderPanel.class);

    private JTable orderTable;
    private DefaultTableModel tableModel;
    private JButton prevButton;
    private JButton nextButton;
    private JLabel pageLabel;
    private InvoiceService invoiceService;
    private OrderService orderService;
    private final PaymentService paymentService;
    private JCalendar calendar;
    private SearchBar searchBar;
    private JComboBox<String> paymentMethodComboBox;
    private Year selectedYear = null; // Default null to not filter by year
    private Month selectedMonth = null; // Default null to not filter by month
    private boolean isTodayFilter = false; // Flag for Today button
    private String currentSearchQuery = "";
    private String selectedPaymentMethod = "All"; // Default to "All"
    private int currentPage = 1;
    private final int pageSize = 10;
    private int totalRecords = 0;

    public OrderPanel(OrderService orderService, InvoiceService invoiceService, PaymentService paymentService) {
        this.orderService = orderService;
        this.invoiceService = invoiceService;
        this.paymentService = paymentService;
        setLayout(new BorderLayout());
        initializeUI();
        loadOrders(null, null, false, "", "All"); // Load all orders on initialization
    }

    private void initializeUI() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        topPanel.setBackground(new Color(240, 242, 245));

        JPanel calendarPanel = new JPanel(new BorderLayout());
        calendarPanel.setBackground(new Color(240, 242, 245));

        calendar = new JCalendar();
        calendar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        calendar.setBackground(Color.WHITE);
        calendar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        calendar.setPreferredSize(new Dimension(240, 180));
        calendar.setDecorationBackgroundColor(new Color(240, 242, 245));
        calendar.setWeekdayForeground(new Color(33, 37, 41));
        calendar.setSundayForeground(new Color(220, 53, 69));
        calendar.setTodayButtonVisible(true);
        calendar.setDecorationBordersVisible(false);
        calendar.getDayChooser().setBackground(new Color(248, 249, 250));

        JPanel calendarWrapper = new JPanel(new BorderLayout());
        calendarWrapper.setBackground(new Color(240, 242, 245));
        calendarWrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(0, 0, 0, 50)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        calendarWrapper.add(calendar, BorderLayout.CENTER);

        JLabel calendarLabel = new JLabel("Select Year and Month:");
        calendarLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        calendarLabel.setForeground(new Color(33, 37, 41));
        calendarLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        calendarPanel.add(calendarLabel, BorderLayout.NORTH);
        calendarPanel.add(calendarWrapper, BorderLayout.CENTER);

        // Handle today button click
        calendar.addPropertyChangeListener("calendar", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("calendar".equals(evt.getPropertyName())) {
                    Date selectedDate = calendar.getDate();
                    Date today = new Date();
                    if (selectedDate.equals(today)) {
                        LocalDate localDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        selectedYear = Year.of(localDate.getYear());
                        selectedMonth = Month.of(localDate.getMonthValue());
                        isTodayFilter = true; // Enable Today filter
                        currentPage = 1;
                        loadOrders(selectedYear, selectedMonth, isTodayFilter, currentSearchQuery, selectedPaymentMethod);
                    }
                }
            }
        });

        // Handle day selection
        calendar.getDayChooser().addPropertyChangeListener("day", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("day".equals(evt.getPropertyName())) {
                    LocalDate selectedDate = calendar.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    selectedYear = Year.of(selectedDate.getYear());
                    selectedMonth = Month.of(selectedDate.getMonthValue());
                    isTodayFilter = selectedDate.equals(LocalDate.now()); // Set Today filter if current day is selected
                    currentPage = 1;
                    loadOrders(selectedYear, selectedMonth, isTodayFilter, currentSearchQuery, selectedPaymentMethod);
                }
            }
        });

        // Center panel for search bar and payment method combo box
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        centerPanel.setBackground(new Color(240, 242, 245));

        // Modified search bar with reset on empty
        searchBar = new SearchBar(query -> {
            currentSearchQuery = query;
            currentPage = 1;
            if (query.isEmpty()) {
                // Reset filters when search is cleared
                selectedYear = null;
                selectedMonth = null;
                isTodayFilter = false;
                calendar.setDate(new Date());
            }
            loadOrders(selectedYear, selectedMonth, isTodayFilter, currentSearchQuery, selectedPaymentMethod);
        });
        searchBar.setPlaceholder("Search by customer name or order ID");
        searchBar.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        searchBar.setBorder(
                BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        searchBar.setBackground(new Color(245, 245, 245));
        searchBar.setPreferredSize(new Dimension(400, 40));
        centerPanel.add(searchBar);

        // Payment method combo box
        String[] paymentMethods = {"All", "Cash", "QR"};
        paymentMethodComboBox = new JComboBox<>(paymentMethods);
        paymentMethodComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        paymentMethodComboBox.setBackground(Color.WHITE);
        paymentMethodComboBox.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        paymentMethodComboBox.setPreferredSize(new Dimension(120, 40));
        paymentMethodComboBox.setSelectedItem("All");
        paymentMethodComboBox.addActionListener(e -> {
            selectedPaymentMethod = (String) paymentMethodComboBox.getSelectedItem();
            currentPage = 1;
            loadOrders(selectedYear, selectedMonth, isTodayFilter, currentSearchQuery, selectedPaymentMethod);
        });
        centerPanel.add(paymentMethodComboBox);

        topPanel.add(calendarPanel, BorderLayout.WEST);
        topPanel.add(centerPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"Order ID", "Customer", "Total Amount", "Items", "Quantity", "Date", "Payment Method"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(tableModel);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        orderTable.setRowHeight(45);
        orderTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        orderTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        orderTable.getTableHeader().setBackground(new Color(52, 86, 139));
        orderTable.getTableHeader().setForeground(Color.WHITE);
        orderTable.setGridColor(new Color(220, 222, 224));
        orderTable.setShowGrid(true);
        orderTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        orderTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        orderTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        orderTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        orderTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        orderTable.getColumnModel().getColumn(5).setPreferredWidth(120);
        orderTable.getColumnModel().getColumn(6).setPreferredWidth(140);

        orderTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = orderTable.getSelectedRow();
                    if (row >= 0) {
                        long orderId = Long.parseLong(orderTable.getValueAt(row, 0).toString());
                        showOrderDetails(orderId);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(new Color(240, 242, 245));
        add(scrollPane, BorderLayout.CENTER);

        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        paginationPanel.setBackground(new Color(240, 242, 245));
        prevButton = new JButton("Previous");
        prevButton.setBackground(new Color(70, 130, 180));
        prevButton.setForeground(Color.WHITE);
        prevButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        prevButton.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadOrders(selectedYear, selectedMonth, isTodayFilter, currentSearchQuery, selectedPaymentMethod);
            }
        });

        nextButton = new JButton("Next");
        nextButton.setBackground(new Color(70, 130, 180));
        nextButton.setForeground(Color.WHITE);
        nextButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nextButton.addActionListener(e -> {
            if (currentPage * pageSize < totalRecords) {
                currentPage++;
                loadOrders(selectedYear, selectedMonth, isTodayFilter, currentSearchQuery, selectedPaymentMethod);
            }
        });

        pageLabel = new JLabel("Page: 1 of 1 (0 orders)");
        pageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pageLabel.setForeground(new Color(33, 37, 41));

        paginationPanel.add(prevButton);
        paginationPanel.add(Box.createHorizontalStrut(15));
        pageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pageLabel.setPreferredSize(new Dimension(200, 30));
        paginationPanel.add(pageLabel);
        paginationPanel.add(Box.createHorizontalStrut(15));
        paginationPanel.add(nextButton);
        add(paginationPanel, BorderLayout.SOUTH);
    }

    private void loadOrders(Year year, Month month, boolean isTodayFilter, String searchQuery, String paymentMethod) {
        tableModel.setRowCount(0);
        try {
            List<OrderDetail> allOrders = orderService.getAllOrders(1, Integer.MAX_VALUE);
            if (allOrders == null || allOrders.isEmpty()) {
                logger.warn("No orders found in the database");
                JOptionPane.showMessageDialog(this,
                    "No orders found in the database.\n- Check if the 'order_detail' table has data.\n- Try creating a new order via the Menu page.",
                    "No Data", JOptionPane.WARNING_MESSAGE);
                totalRecords = 0;
                updatePaginationControls();
                return;
            }

            List<OrderDetail> filteredOrders = allOrders;

            // Apply date filter only when year and month are not null
            if (year != null && month != null) {
                filteredOrders = filteredOrders.stream()
                        .filter(order -> {
                            try {
                                if (order.getOrderDate() == null) {
                                    logger.warn("Order ID {} has null order date", order.getId());
                                    return false;
                                }
                                LocalDate orderDate = order.getOrderDate().toInstant()
                                        .atZone(ZoneId.systemDefault()).toLocalDate();
                                if (isTodayFilter) {
                                    return orderDate.equals(LocalDate.now());
                                }
                                return orderDate.getYear() == year.getValue() &&
                                       orderDate.getMonth() == month;
                            } catch (Exception e) {
                                logger.error("Error processing order ID {}: {}", order.getId(), e.getMessage());
                                return false;
                            }
                        })
                        .collect(Collectors.toList());
            }

            // Apply search if present
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                String query = searchQuery.toLowerCase().trim();
                filteredOrders = filteredOrders.stream()
                        .filter(order -> {
                            if (String.valueOf(order.getId()).contains(query)) {
                                return true;
                            }
                            String customerName = getCustomerName(order);
                            return customerName.toLowerCase().contains(query);
                        })
                        .collect(Collectors.toList());
            }

            // Apply payment method filter if not "All"
            if (paymentMethod != null && !paymentMethod.equals("All")) {
                filteredOrders = filteredOrders.stream()
                        .filter(order -> {
                            Invoice invoice = invoiceService.getAllInvoices(1, Integer.MAX_VALUE)
                                    .stream()
                                    .filter(inv -> inv.getOrder() != null && inv.getOrder().getId() == order.getId())
                                    .findFirst()
                                    .orElse(null);
                            String orderPaymentMethod = invoice != null && invoice.getPaymentMethod() != null
                                    ? invoice.getPaymentMethod()
                                    : "Unknown";
                            if (paymentMethod.equals("QR")) {
                                return orderPaymentMethod.toLowerCase().startsWith("qr");
                            }
                            return orderPaymentMethod.equalsIgnoreCase(paymentMethod);
                        })
                        .collect(Collectors.toList());
            }

            totalRecords = filteredOrders.size();

            int startIndex = (currentPage - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalRecords);
            List<OrderDetail> paginatedOrders = filteredOrders.subList(
                    Math.min(startIndex, totalRecords),
                    Math.min(endIndex, totalRecords));

            for (OrderDetail order : paginatedOrders) {
                Invoice invoice = invoiceService.getAllInvoices(1, Integer.MAX_VALUE)
                        .stream()
                        .filter(inv -> inv.getOrder() != null && inv.getOrder().getId() == order.getId())
                        .findFirst()
                        .orElse(null);

                String customerName = getCustomerName(order);
                int itemCount = orderService.getItemCountForOrder(order.getId());
                int totalQuantity = order.getItems().stream()
                        .mapToInt(OrderItem::getQuantity)
                        .sum();
                double finalPrice = invoice != null ? invoice.getFinalPrice() : order.getTotalAmount();
                String orderPaymentMethod = invoice != null && invoice.getPaymentMethod() != null ? invoice.getPaymentMethod() : "Unknown";

                Object[] rowData = {
                    order.getId(),
                    customerName,
                    PRICE_FORMAT.format(finalPrice),
                    itemCount,
                    totalQuantity,
                    order.getOrderDate() != null ? order.getOrderDate().toString() : "N/A",
                    orderPaymentMethod
                };
                tableModel.addRow(rowData);
            }

            logger.info("Loaded {} orders (page {}/{}), filtered by year: {}, month: {}, today: {}, search: {}, payment: {}",
                totalRecords, currentPage, (int) Math.ceil((double) totalRecords / pageSize),
                year != null ? year.getValue() : "none", month != null ? month : "none", isTodayFilter, searchQuery, paymentMethod);

            updatePaginationControls();
            orderTable.revalidate();
            orderTable.repaint();
        } catch (Exception e) {
            logger.error("Error loading orders", e);
            JOptionPane.showMessageDialog(this,
                "Error loading orders: " + e.getMessage() +
                "\nPlease check database connection and service configuration.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getCustomerName(OrderDetail order) {
        String customerName = "N/A";
        try {
            Customer customer = order.getCustomer();
            if (customer != null) {
                StringBuilder fullName = new StringBuilder();
                String firstName = customer.getPersonFirstName();
                String middleName = customer.getPersonMiddleName();
                String lastName = customer.getPersonLastName();
                if (firstName != null && !firstName.trim().isEmpty()) {
                    fullName.append(firstName);
                }
                if (middleName != null && !middleName.trim().isEmpty()) {
                    if (fullName.length() > 0) fullName.append(" ");
                    fullName.append(middleName);
                }
                if (lastName != null && !lastName.trim().isEmpty()) {
                    if (fullName.length() > 0) fullName.append(" ");
                    fullName.append(lastName);
                }
                if (fullName.length() > 0) {
                    customerName = fullName.toString();
                } else if (customer.getCustomerNumber() != null && !customer.getCustomerNumber().trim().isEmpty()) {
                    customerName = customer.getCustomerNumber();
                } else {
                    customerName = "Unknown Customer (ID: " + customer.getPersonId() + ")";
                }
                logger.debug("Customer name for order ID {}: {}", order.getId(), customerName);
            } else {
                logger.warn("No customer associated with order ID {}", order.getId());
            }
        } catch (Exception e) {
            logger.warn("Failed to load customer for order ID {}: {}", order.getId(), e.getMessage());
        }
        return customerName;
    }

    private void showOrderDetails(long orderId) {
        try {
            OrderDetail order = orderService.getOrderById(orderId);
            if (order == null) {
                logger.warn("Order ID {} not found", orderId);
                JOptionPane.showMessageDialog(this, "Order not found!",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Invoice invoice = invoiceService.getAllInvoices(1, Integer.MAX_VALUE)
                    .stream()
                    .filter(inv -> inv.getOrder() != null && inv.getOrder().getId() == orderId)
                    .findFirst()
                    .orElse(null);

            if (invoice == null) {
                logger.warn("No invoice found for order ID {}", orderId);
                JOptionPane.showMessageDialog(this, "No invoice found for this order!",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JDialog detailsDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Order Details", true);
            detailsDialog.setSize(500, 600);
            detailsDialog.setLayout(new BorderLayout());
            detailsDialog.setLocationRelativeTo(this);

            InvoicePanel invoicePanel = new InvoicePanel(invoice, order.getItems() != null ?
                order.getItems().stream()
                    .map(item -> {
                        Product p = item.getProduct();
                        p.setQuantity(item.getQuantity());
                        return p;
                    })
                    .collect(Collectors.toList()) : List.of());

            detailsDialog.add(invoicePanel, BorderLayout.CENTER);
            detailsDialog.setVisible(true);
        } catch (Exception e) {
            logger.error("Error loading order details for ID {}", orderId, e);
            JOptionPane.showMessageDialog(this,
                "Error loading order details: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePaginationControls() {
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        pageLabel.setText(String.format("Page %d of %d (%d orders)", currentPage,
            totalPages, totalRecords));
        prevButton.setEnabled(currentPage > 1);
        nextButton.setEnabled(currentPage * pageSize < totalRecords);
    }
}