package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.List;
import model.Payment;
import model.OrderDetail;
import model.Customer;
import model.Employee;
import model.Product;
import model.Invoice;
import service.PaymentService;
import service.PersonService;
import service.InvoiceService;
import service.GiftVoucherService;

/**
 * PaymentPanel handles payment processing (QR or Cash).
 */
public class PaymentPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#,###.000 VND");

    private JComboBox<String> paymentMethodCombo;
    private JButton proceedButton;
    private PaymentService paymentService;
    private PersonService personService;
    private InvoiceService invoiceService;
    private GiftVoucherService giftVoucherService;
    private final Employee loggedInEmployee;
    private QRPanel qrPanel;
    private OrderDetail order;
    private Customer customer;
    private List<Product> cartItems;
    private Runnable onPaymentComplete;

    /**
     * Constructor initializes the payment panel.
     */
    public PaymentPanel(OrderDetail order, Customer customer, PaymentService paymentService, 
                       PersonService personService, InvoiceService invoiceService, 
                       GiftVoucherService giftVoucherService, List<Product> cartItems, 
                       Employee loggedInEmployee) {
        this.order = order;
        this.customer = customer;
        this.paymentService = paymentService;
        this.personService = personService;
        this.invoiceService = invoiceService;
        this.giftVoucherService = giftVoucherService;
        this.loggedInEmployee = loggedInEmployee;
        this.cartItems = cartItems;
        this.qrPanel = new QRPanel();

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        initializeUI();
    }

    /**
     * Sets the payment completion callback.
     */
    public void setOnPaymentCompleteListener(Runnable listener) {
        this.onPaymentComplete = listener;
    }

    /**
     * Initializes the UI components.
     */
    private void initializeUI() {
        JPanel mainPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel amountLabel = new JLabel("Total Amount:");
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        amountLabel.setForeground(new Color(50, 50, 50));

        JLabel amountField = new JLabel(PRICE_FORMAT.format(order.getTotalAmount()));
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        amountField.setBackground(new Color(245, 245, 245));
        amountField.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));

        JLabel methodLabel = new JLabel("Payment Method:");
        methodLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        methodLabel.setForeground(new Color(50, 50, 50));

        String[] methods = {"QR Code", "Cash"};
        paymentMethodCombo = new JComboBox<>(methods);
        paymentMethodCombo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        paymentMethodCombo.setBackground(Color.WHITE);
        paymentMethodCombo.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));

        proceedButton = new JButton("Proceed to Payment");
        proceedButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        proceedButton.setBackground(new Color(70, 130, 180));
        proceedButton.setForeground(Color.WHITE);
        proceedButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        proceedButton.addActionListener(e -> handleProceed());

        mainPanel.add(amountLabel);
        mainPanel.add(amountField);
        mainPanel.add(methodLabel);
        mainPanel.add(paymentMethodCombo);

        add(mainPanel, BorderLayout.CENTER);
        add(proceedButton, BorderLayout.SOUTH);

        JLabel titleLabel = new JLabel("Complete Your Payment", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
    }

    /**
     * Handles the proceed button action.
     */
    private void handleProceed() {
        String selectedMethod = (String) paymentMethodCombo.getSelectedItem();
        if (selectedMethod.equals("QR Code")) {
            showQRPanel();
        } else if (selectedMethod.equals("Cash")) {
            processCashPayment();
        }
    }

    /**
     * Shows the QR payment panel.
     */
    private void showQRPanel() {
        removeAll();
        revalidate();
        repaint();

        qrPanel.setPaymentDetails(order.getTotalAmount(), order, customer, paymentService);
        qrPanel.setOnPaymentCompleteListener(this::onQRPanelComplete);
        add(qrPanel, BorderLayout.CENTER);

        qrPanel.displayDynamicQRCode();
        revalidate();
        repaint();
    }

    /**
     * Processes a cash payment.
     */
    private void processCashPayment() {
        Payment payment = new Payment();
        payment.setPaymentMethod("Cash");
        double originalAmount = order.getTotalAmount();
        double discountedAmount = originalAmount;
        double discount = 0.0;
        if (giftVoucherService != null) {
            discountedAmount = new GiftVoucherPanel(giftVoucherService).applyDiscount(originalAmount);
            discount = originalAmount - discountedAmount;
        } else {
            JOptionPane.showMessageDialog(this, "Gift voucher service unavailable; proceeding without discount.",
                "Warning", JOptionPane.WARNING_MESSAGE);
        }
        payment.setAmount(discountedAmount);
        payment.setStatus("COMPLETED");
        payment.setOrder(order);
        payment.setCustomer(customer);
        payment.setPaymentDate(new java.util.Date());

        boolean paymentSuccess = paymentService.createPayment(payment);
        if (paymentSuccess) {
            updateCustomerPoints();
            Invoice invoice = createInvoice(payment, originalAmount, discount, discountedAmount);
            showPaymentConfirmation(discountedAmount, invoice);
            if (onPaymentComplete != null) {
                onPaymentComplete.run();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to process cash payment.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Processes a QR payment.
     */
    private void processQRPayment() {
        Payment payment = new Payment();
        payment.setPaymentMethod("QR - " + (qrPanel.getSelectedBank() != null ? qrPanel.getSelectedBank() : "Unknown"));
        double originalAmount = order.getTotalAmount();
        double discountedAmount = originalAmount;
        double discount = 0.0;
        if (giftVoucherService != null) {
            discountedAmount = new GiftVoucherPanel(giftVoucherService).applyDiscount(originalAmount);
            discount = originalAmount - discountedAmount;
        } else {
            JOptionPane.showMessageDialog(this, "Gift voucher service unavailable; proceeding without discount.",
                "Warning", JOptionPane.WARNING_MESSAGE);
        }
        payment.setAmount(discountedAmount);
        payment.setStatus("COMPLETED");
        payment.setOrder(order);
        payment.setCustomer(customer);
        payment.setPaymentDate(new java.util.Date());

        boolean paymentSuccess = paymentService.createPayment(payment);
        if (paymentSuccess) {
            updateCustomerPoints();
            Invoice invoice = createInvoice(payment, originalAmount, discount, discountedAmount);
            showPaymentConfirmation(discountedAmount, invoice);
            if (onPaymentComplete != null) {
                onPaymentComplete.run();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to process QR payment.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Creates an invoice.
     */
    private Invoice createInvoice(Payment payment, double originalAmount, double discount, double finalAmount) {
        Invoice invoice = new Invoice();
        invoice.setOrder(order);
        invoice.setCustomer(customer);
        invoice.setEmployee(loggedInEmployee);
        invoice.setPaymentMethod(payment.getPaymentMethod());
        invoice.setTotalPrice(originalAmount);
        invoice.setDiscount(discount);
        invoice.setFinalPrice(finalAmount);
        invoice.setInvoiceDay(new java.util.Date());
        invoice.setStatus("COMPLETED");
        boolean success = invoiceService.createInvoice(invoice);
        if (!success) {
            JOptionPane.showMessageDialog(this, "Failed to create invoice.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return invoice;
    }

    /**
     * Updates customer loyalty points.
     */
    private void updateCustomerPoints() {
        try {
            int itemCount = cartItems.stream().mapToInt(Product::getQuantity).sum();
            Double currentPoints = customer.getPoints();
            double pointsToAdd = currentPoints == null ? itemCount : currentPoints + itemCount;
            customer.setPoints(pointsToAdd);
            personService.updateCustomer(customer);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating customer points: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Shows payment confirmation and invoice option.
     */
    private void showPaymentConfirmation(double amountPaid, Invoice invoice) {
        JDialog confirmationDialog = new JDialog(SwingUtilities.getWindowAncestor(this), 
            "Payment Successful", Dialog.ModalityType.APPLICATION_MODAL);
        confirmationDialog.setSize(400, 300);
        confirmationDialog.setLayout(new BorderLayout(10, 10));
        confirmationDialog.getContentPane().setBackground(Color.WHITE);

        JPanel detailsPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        detailsPanel.add(createLabel("Payment Successful!", Font.BOLD, 18));
        detailsPanel.add(createLabel("Total Amount: " + PRICE_FORMAT.format(amountPaid), Font.PLAIN, 16));
        detailsPanel.add(createLabel("Payment Method: " + (invoice != null ? invoice.getPaymentMethod() : "Unknown"), Font.PLAIN, 16));
        detailsPanel.add(createLabel("Customer: " + getCustomerFullName(customer), Font.PLAIN, 16));
        detailsPanel.add(createLabel("Date: " + new java.util.Date(), Font.PLAIN, 16));

        confirmationDialog.add(detailsPanel, BorderLayout.CENTER);

        JButton showInvoiceButton = new JButton("View Invoice");
        showInvoiceButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        showInvoiceButton.setBackground(new Color(70, 130, 180));
        showInvoiceButton.setForeground(Color.WHITE);
        showInvoiceButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        showInvoiceButton.addActionListener(e -> {
            confirmationDialog.dispose();
            if (invoice != null) {
                JDialog invoiceDialog = new JDialog(SwingUtilities.getWindowAncestor(this), 
                    "Invoice Details", Dialog.ModalityType.MODELESS);
                invoiceDialog.setSize(400, 500);
                invoiceDialog.setLayout(new BorderLayout(10, 10));
                invoiceDialog.getContentPane().setBackground(Color.WHITE);
                invoiceDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

                InvoicePanel invoicePanel = new InvoicePanel(invoice, cartItems);
                invoiceDialog.add(invoicePanel, BorderLayout.CENTER);
                invoiceDialog.setLocationRelativeTo(null);
                invoiceDialog.setVisible(true);
            }
        });

        confirmationDialog.add(showInvoiceButton, BorderLayout.SOUTH);
        confirmationDialog.setLocationRelativeTo(null);
        confirmationDialog.setVisible(true);
    }

    /**
     * Creates a styled label.
     */
    private JLabel createLabel(String text, int style, int size) {
        JLabel label = new JLabel(text, SwingConstants.LEFT);
        label.setFont(new Font("Segoe UI", style, size));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    /**
     * Gets the customer's full name.
     */
    private String getCustomerFullName(Customer customer) {
        return (customer.getPersonFirstName() != null ? customer.getPersonFirstName() : "") + " " +
               (customer.getPersonMiddleName() != null ? customer.getPersonMiddleName() + " " : "") +
               (customer.getPersonLastName() != null ? customer.getPersonLastName() : "");
    }

    /**
     * Callback for QR payment completion.
     */
    public void onQRPanelComplete() {
        processQRPayment();
    }
}