package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.List;
import model.Payment;
import model.OrderDetail;
import model.Customer;
import model.Product;
import service.PaymentService;
import service.PersonService;

public class PaymentPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#,###.000 VND");

    private JTextField amountField;
    private JTextField amountPaidField;
    private JLabel changeLabel;
    private JComboBox<String> paymentMethodCombo;
    private JButton proceedButton;
    private PaymentService paymentService;
    private PersonService personService;
    private QRPanel qrPanel;
    private OrderDetail order;
    private Customer customer;
    private List<Product> cartItems;
    private JPanel mainPanel;
    private Timer qrTimer;
    private Runnable onPaymentComplete;

    public PaymentPanel(OrderDetail order, Customer customer, PaymentService paymentService, PersonService personService, List<Product> cartItems) {
        this.order = order;
        this.customer = customer;
        this.paymentService = paymentService;
        this.personService = personService;
        this.cartItems = cartItems;
        this.qrPanel = new QRPanel();

        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        initializeUI();
    }

    public void setOnPaymentCompleteListener(Runnable listener) {
        this.onPaymentComplete = listener;
    }

    private void initializeUI() {
        mainPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel amountLabel = new JLabel("Total Amount:");
        amountLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        amountLabel.setForeground(new Color(50, 50, 50));

        amountField = new JTextField(PRICE_FORMAT.format(order.getTotalAmount()));
        amountField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        amountField.setEditable(false);
        amountField.setBackground(new Color(245, 245, 245));
        amountField.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));

        JLabel amountPaidLabel = new JLabel("Amount Paid:");
        amountPaidLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        amountPaidLabel.setForeground(new Color(50, 50, 50));

        amountPaidField = new JTextField();
        amountPaidField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        amountPaidField.setBackground(new Color(245, 245, 245));
        amountPaidField.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));

        JLabel changeTitleLabel = new JLabel("Change:");
        changeTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        changeTitleLabel.setForeground(new Color(50, 50, 50));

        changeLabel = new JLabel("0.00 VND");
        changeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        changeLabel.setForeground(new Color(50, 50, 50));

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
        proceedButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                proceedButton.setBackground(new Color(100, 160, 210));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                proceedButton.setBackground(new Color(70, 130, 180));
            }
        });
        proceedButton.addActionListener(e -> handleProceed());

        mainPanel.add(amountLabel);
        mainPanel.add(amountField);
        mainPanel.add(amountPaidLabel);
        mainPanel.add(amountPaidField);
        mainPanel.add(changeTitleLabel);
        mainPanel.add(changeLabel);
        mainPanel.add(methodLabel);
        mainPanel.add(paymentMethodCombo);
        mainPanel.add(new JLabel());
        mainPanel.add(proceedButton);

        add(mainPanel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("Complete Your Payment", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        amountPaidField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateChange();
            }
        });
    }

    private void updateChange() {
        try {
            double totalAmount = order.getTotalAmount();
            double amountPaid = amountPaidField.getText().isEmpty() ? 0 : Double.parseDouble(amountPaidField.getText().replace(",", "").replace(" VND", ""));
            double change = amountPaid - totalAmount;
            changeLabel.setText(PRICE_FORMAT.format(change));
        } catch (NumberFormatException ex) {
            changeLabel.setText("Invalid input");
        }
    }

    private void handleProceed() {
        String selectedMethod = (String) paymentMethodCombo.getSelectedItem();
        if (selectedMethod.equals("QR Code")) {
            amountPaidField.setText(PRICE_FORMAT.format(order.getTotalAmount()));
            amountPaidField.setEditable(false);
            updateChange();
            showQRPanel();
        } else if (selectedMethod.equals("Cash")) {
            amountPaidField.setEditable(true);
            amountPaidField.setText("");
            changeLabel.setText("0.00 VND");
            showCashPayment();
        }
    }

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

    private void showCashPayment() {
        removeAll();
        revalidate();
        repaint();

        JPanel cashPanel = new JPanel(new BorderLayout(10, 10));
        cashPanel.setBackground(Color.WHITE);
        cashPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBackground(Color.WHITE);

        inputPanel.add(new JLabel("Total Amount:"));
        inputPanel.add(new JLabel(PRICE_FORMAT.format(order.getTotalAmount())));
        inputPanel.add(new JLabel("Amount Paid:"));
        inputPanel.add(amountPaidField);
        inputPanel.add(new JLabel("Change:"));
        inputPanel.add(changeLabel);

        JButton confirmButton = new JButton("Confirm Cash Payment");
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        confirmButton.setBackground(new Color(50, 205, 50));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        confirmButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                confirmButton.setBackground(new Color(80, 235, 80));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                confirmButton.setBackground(new Color(50, 205, 50));
            }
        });
        confirmButton.addActionListener(e -> processCashPayment());

        cashPanel.add(inputPanel, BorderLayout.CENTER);
        cashPanel.add(confirmButton, BorderLayout.SOUTH);

        add(cashPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void processCashPayment() {
        try {
            double amountPaid = Double.parseDouble(amountPaidField.getText().replace(",", "").replace(" VND", ""));
            double totalAmount = order.getTotalAmount();
            if (amountPaid < totalAmount) {
                JOptionPane.showMessageDialog(this, "Amount paid is less than total amount!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Payment payment = new Payment();
            payment.setPaymentMethod("Cash");
            payment.setAmount(totalAmount);
            payment.setStatus("COMPLETED");
            payment.setOrder(order);
            payment.setCustomer(customer);
            payment.setPaymentDate(new java.util.Date());

            boolean success = paymentService.createPayment(payment);
            if (success) {
                updateCustomerPoints();
                showPaymentConfirmation(amountPaid);
                if (onPaymentComplete != null) {
                    onPaymentComplete.run();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to process cash payment.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount paid!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processQRPayment() {
        Payment payment = new Payment();
        payment.setPaymentMethod("QR - " + (qrPanel.getSelectedBank() != null ? qrPanel.getSelectedBank() : "Unknown"));
        payment.setAmount(order.getTotalAmount());
        payment.setStatus("COMPLETED");
        payment.setOrder(order);
        payment.setCustomer(customer);
        payment.setPaymentDate(new java.util.Date());

        boolean success = paymentService.createPayment(payment);
        if (success) {
            updateCustomerPoints();
            showPaymentConfirmation(order.getTotalAmount());
            if (onPaymentComplete != null) {
                onPaymentComplete.run();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to process QR payment.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCustomerPoints() {
        try {
            int itemCount = cartItems.stream().mapToInt(Product::getQuantity).sum();
            Double currentPoints = customer.getPoints();
            double pointsToAdd = currentPoints == null ? itemCount : currentPoints + itemCount;
            customer.setPoints(pointsToAdd);
            personService.updateCustomer(customer);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating customer points: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showPaymentConfirmation(double amountPaid) {
        JDialog confirmationDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Payment Confirmation", true);
        confirmationDialog.setSize(400, 400);
        confirmationDialog.setLayout(new BorderLayout(10, 10));
        confirmationDialog.getContentPane().setBackground(Color.WHITE);

        JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        detailsPanel.add(createLabel("Payment Confirmation", Font.BOLD, 20, true));
        detailsPanel.add(createLabel("Customer: " + getCustomerFullName(customer), Font.PLAIN, 16, false));
        detailsPanel.add(createLabel("Payment Method: " + paymentMethodCombo.getSelectedItem(), Font.PLAIN, 16, false));
        detailsPanel.add(createLabel("Total Amount: " + PRICE_FORMAT.format(order.getTotalAmount()), Font.PLAIN, 16, false));
        detailsPanel.add(createLabel("Amount Paid: " + PRICE_FORMAT.format(amountPaid), Font.PLAIN, 16, false));
        detailsPanel.add(createLabel("Change: " + changeLabel.getText(), Font.PLAIN, 16, false));
        detailsPanel.add(createLabel("Items Purchased:", Font.BOLD, 16, false));
        for (Product item : cartItems) {
            detailsPanel.add(createLabel("- " + item.getName() + " (" + item.getSize() + ") x" + item.getQuantity(), Font.PLAIN, 14, false));
        }
        detailsPanel.add(createLabel("Points Earned: " + cartItems.stream().mapToInt(Product::getQuantity).sum(), Font.PLAIN, 16, false));
        Double points = customer.getPoints();
        detailsPanel.add(createLabel("Total Points: " + (points != null ? points : 0.0), Font.PLAIN, 16, false));

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        closeButton.setBackground(new Color(70, 130, 180));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        closeButton.addActionListener(e -> confirmationDialog.dispose());

        confirmationDialog.add(detailsPanel, BorderLayout.CENTER);
        confirmationDialog.add(closeButton, BorderLayout.SOUTH);
        confirmationDialog.setLocationRelativeTo(null);
        confirmationDialog.setVisible(true);
    }

    private JLabel createLabel(String text, int style, int size, boolean center) {
        JLabel label = new JLabel(text, center ? SwingConstants.CENTER : SwingConstants.LEFT);
        label.setFont(new Font("Segoe UI", style, size));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    private String getCustomerFullName(Customer customer) {
        return (customer.getPersonFirstName() != null ? customer.getPersonFirstName() : "") + " " +
               (customer.getPersonMiddleName() != null ? customer.getPersonMiddleName() + " " : "") +
               (customer.getPersonLastName() != null ? customer.getPersonLastName() : "");
    }

    public void onQRPanelComplete() {
        removeAll();
        add(mainPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
        processQRPayment();
    }
}