package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.*;
import java.util.List;
import java.util.stream.Collectors;
import model.Invoice;
import model.Product;
import model.Customer;
import service.InvoiceService;
import service.InvoiceServiceImpl;
import dao.InvoiceDao;
import com.toedter.calendar.JCalendar;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OrderPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#,###.000 VND");
    private static final Logger logger = LogManager.getLogger(OrderPanel.class);

    private JTable invoiceTable;
    private DefaultTableModel tableModel;
    private JButton prevButton;
    private JButton nextButton;
    private JLabel pageLabel;
    private InvoiceService invoiceService;
    private JCalendar calendar;
    private Year selectedYear;
    private Month selectedMonth;
    private int currentPage = 1;
    private final int pageSize = 6;
    private int totalRecords = 0;

    public OrderPanel() {
        try {
            invoiceService = new InvoiceServiceImpl(new InvoiceDao());
        } catch (Exception e) {
            logger.error("Failed to initialize InvoiceService", e);
            JOptionPane.showMessageDialog(null, "Failed to initialize invoice service: " + e.getMessage(), 
                "Initialization Error", JOptionPane.ERROR_MESSAGE);
        }
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(240, 242, 245)); // Softer light gray background
        initializeUI();
        loadInvoices(null, null); // Load all invoices by default
    }

    private void initializeUI() {
        // Top panel for filtering
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(new Color(240, 242, 245));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Calendar for date selection
        calendar = new JCalendar();
        calendar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        calendar.setBackground(Color.WHITE);
        calendar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1, true), // Rounded border
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        calendar.setPreferredSize(new Dimension(240, 180)); // Slightly larger for clarity
        calendar.setDecorationBackgroundColor(new Color(240, 242, 245));
        calendar.setWeekdayForeground(new Color(33, 37, 41)); // Darker text for weekdays
        calendar.setSundayForeground(new Color(220, 53, 69)); // Vibrant red for Sundays
        calendar.setTodayButtonVisible(true); // Show today button
        calendar.setDecorationBordersVisible(false); // Cleaner look
        calendar.getDayChooser().setBackground(new Color(248, 249, 250)); // Subtle background for days

        // Add shadow effect to calendar panel
        JPanel calendarWrapper = new JPanel(new BorderLayout());
        calendarWrapper.setBackground(new Color(240, 242, 245));
        calendarWrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 2, new Color(0, 0, 0, 50)), // Subtle shadow
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        calendarWrapper.add(calendar, BorderLayout.CENTER);

        // Add property change listener for date selection
        calendar.getDayChooser().addPropertyChangeListener("day", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("day".equals(evt.getPropertyName())) {
                    LocalDate selectedDate = calendar.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    selectedYear = Year.of(selectedDate.getYear());
                    selectedMonth = Month.of(selectedDate.getMonthValue());
                    currentPage = 1;
                    loadInvoices(selectedYear, selectedMonth);
                }
            }
        });

        // Label for calendar
        JLabel calendarLabel = new JLabel("Select Year and Month:");
        calendarLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        calendarLabel.setForeground(new Color(33, 37, 41));
        calendarLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JPanel calendarPanel = new JPanel(new BorderLayout());
        calendarPanel.setBackground(new Color(240, 242, 245));
        calendarPanel.add(calendarLabel, BorderLayout.NORTH);
        calendarPanel.add(calendarWrapper, BorderLayout.CENTER);

        topPanel.add(calendarPanel, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        // Table setup
        String[] columnNames = {"Invoice ID", "Customer", "Total Amount", "Items", "Date", "Payment Method"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        invoiceTable = new JTable(tableModel);
        invoiceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        invoiceTable.setRowHeight(30);
        invoiceTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        invoiceTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        invoiceTable.getTableHeader().setBackground(new Color(52, 86, 139)); // Deeper blue for header
        invoiceTable.getTableHeader().setForeground(Color.WHITE);
        invoiceTable.setGridColor(new Color(220, 222, 224));
        invoiceTable.setShowGrid(true);
        invoiceTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        invoiceTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        invoiceTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        invoiceTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        invoiceTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        invoiceTable.getColumnModel().getColumn(5).setPreferredWidth(140);

        // Add double-click listener to show invoice details
        invoiceTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = invoiceTable.getSelectedRow();
                    if (row >= 0) {
                        long invoiceId = Long.parseLong(invoiceTable.getValueAt(row, 0).toString());
                        showInvoiceDetails(invoiceId);
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(invoiceTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(new Color(240, 242, 245));
        add(scrollPane, BorderLayout.CENTER);

        // Pagination controls
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        paginationPanel.setBackground(new Color(240, 242, 245));
        prevButton = new JButton("Previous");
        prevButton.setBackground(new Color(52, 86, 139));
        prevButton.setForeground(Color.WHITE);
        prevButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        prevButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        prevButton.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadInvoices(selectedYear, selectedMonth);
            }
        });

        nextButton = new JButton("Next");
        nextButton.setBackground(new Color(52, 86, 139));
        nextButton.setForeground(Color.WHITE);
        nextButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        nextButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        nextButton.addActionListener(e -> {
            if (currentPage * pageSize < totalRecords) {
                currentPage++;
                loadInvoices(selectedYear, selectedMonth);
            }
        });

        pageLabel = new JLabel("Page: 1");
        pageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pageLabel.setForeground(new Color(33, 37, 41));

        paginationPanel.add(prevButton);
        paginationPanel.add(Box.createHorizontalStrut(15));
        paginationPanel.add(pageLabel);
        paginationPanel.add(Box.createHorizontalStrut(15));
        paginationPanel.add(nextButton);
        add(paginationPanel, BorderLayout.SOUTH);
    }

    private void loadInvoices(Year year, Month month) {
        tableModel.setRowCount(0);
        try {
            List<Invoice> allInvoices = invoiceService.getAllInvoices(1, Integer.MAX_VALUE);
            if (allInvoices == null || allInvoices.isEmpty()) {
                logger.warn("No invoices found in the database");
                JOptionPane.showMessageDialog(this, 
                    "No invoices found in the database.\n- Check if the 'invoice' table has data.\n- Try creating a new invoice via the Menu page.", 
                    "No Data", JOptionPane.WARNING_MESSAGE);
                totalRecords = 0;
                updatePaginationControls();
                return;
            }

            List<Invoice> filteredInvoices = allInvoices;
            if (year != null && month != null) {
                filteredInvoices = allInvoices.stream()
                        .filter(invoice -> {
                            try {
                                if (invoice.getInvoiceDay() == null) {
                                    logger.warn("Invoice ID {} has null invoice date", invoice.getId());
                                    return false;
                                }
                                LocalDate invoiceDate = invoice.getInvoiceDay().toInstant()
                                    .atZone(ZoneId.systemDefault()).toLocalDate();
                                return invoiceDate.getYear() == year.getValue() && invoiceDate.getMonth() == month;
                            } catch (Exception e) {
                                logger.error("Error processing invoice ID {}: {}", invoice.getId(), e.getMessage());
                                return false;
                            }
                        })
                        .collect(Collectors.toList());
            }

            totalRecords = filteredInvoices.size();
            int startIndex = (currentPage - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, totalRecords);
            List<Invoice> paginatedInvoices = filteredInvoices.subList(startIndex, endIndex);

            for (Invoice invoice : paginatedInvoices) {
                String customerName = "N/A";
                try {
                    if (invoice.getCustomer() != null) {
                        Customer customer = invoice.getCustomer();
                        StringBuilder fullName = new StringBuilder();
                        String firstName = customer.getPersonFirstName();
                        String middleName = customer.getPersonMiddleName();
                        String lastName = customer.getPersonLastName();
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
                        customerName = fullName.length() > 0 ? fullName.toString() :
                            customer.getCustomerNumber() != null ? customer.getCustomerNumber() : "Unknown";
                    }
                } catch (Exception e) {
                    logger.warn("Failed to load customer for invoice ID {}: {}", invoice.getId(), e.getMessage());
                }

                int itemCount = 0;
                try {
                    if (invoice.getOrder() != null) {
                        itemCount = invoice.getOrder().getItems().size();
                    }
                } catch (Exception e) {
                    logger.warn("Failed to load items for invoice ID {}: {}", invoice.getId(), e.getMessage());
                }

                Object[] rowData = {
                    invoice.getId(),
                    customerName,
                    PRICE_FORMAT.format(invoice.getFinalPrice()),
                    itemCount,
                    invoice.getInvoiceDay() != null ? invoice.getInvoiceDay().toString() : "N/A",
                    invoice.getPaymentMethod() != null ? invoice.getPaymentMethod() : "Unknown"
                };
                tableModel.addRow(rowData);
            }

            logger.info("Loaded {} invoices (page {}/{}), filtered by year: {}, month: {}",
                totalRecords, currentPage, (int) Math.ceil((double) totalRecords / pageSize),
                year != null ? year.getValue() : "none", month != null ? month : "none");

            updatePaginationControls();
            invoiceTable.revalidate();
            invoiceTable.repaint();
        } catch (Exception e) {
            logger.error("Error loading invoices", e);
            JOptionPane.showMessageDialog(this, 
                "Error loading invoices: " + e.getMessage() + 
                "\nPlease check database connection and Hibernate configuration.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showInvoiceDetails(long invoiceId) {
        try {
            Invoice invoice = invoiceService.getInvoiceById(invoiceId);
            if (invoice == null) {
                logger.warn("Invoice ID {} not found", invoiceId);
                JOptionPane.showMessageDialog(this, "Invoice not found!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JDialog detailsDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                "Invoice Details", true);
            detailsDialog.setSize(500, 600);
            detailsDialog.setLayout(new BorderLayout());
            detailsDialog.setLocationRelativeTo(this);

            InvoicePanel invoicePanel = new InvoicePanel(invoice, invoice.getOrder() != null ?
                invoice.getOrder().getItems().stream()
                    .map(item -> {
                        Product p = item.getProduct();
                        p.setQuantity(item.getQuantity());
                        return p;
                    })
                    .collect(Collectors.toList()) : List.of());

            detailsDialog.add(invoicePanel, BorderLayout.CENTER);
            detailsDialog.setVisible(true);
        } catch (Exception e) {
            logger.error("Error loading invoice details for ID {}", invoiceId, e);
            JOptionPane.showMessageDialog(this, 
                "Error loading invoice details: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePaginationControls() {
        pageLabel.setText(String.format("Page %d of %d (%d invoices)", currentPage,
            (int) Math.ceil((double) totalRecords / pageSize), totalRecords));
        prevButton.setEnabled(currentPage > 1);
        nextButton.setEnabled(currentPage * pageSize < totalRecords);
    }
}