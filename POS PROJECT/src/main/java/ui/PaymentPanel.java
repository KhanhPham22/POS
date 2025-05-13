package ui;

import javax.swing.*;
import model.Payment;
import model.OrderDetail;
import model.Customer;
import service.PaymentService;
import service.PaymentServiceImpl;
import dao.PaymentDao;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PaymentPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JTextField amountField;
    private JComboBox<String> paymentMethodCombo;
    private JButton proceedButton;
    private PaymentService paymentService;
    private QRPanel qrPanel;
    private OrderDetail order; // Assuming order is passed to this panel
    private Customer customer; // Assuming customer is passed to this panel

    public PaymentPanel(OrderDetail order, Customer customer) {
        this.order = order;
        this.customer = customer;
        this.paymentService = new PaymentServiceImpl(new PaymentDao());
        this.qrPanel = new QRPanel();

        setLayout(new GridLayout(4, 2, 10, 10));

        // Initialize UI components
        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JTextField(10);
        amountField.setText(String.valueOf(order.getTotalAmount())); // Pre-fill with order total
        amountField.setEditable(false); // Amount is fixed based on order

        JLabel methodLabel = new JLabel("Payment Method:");
        String[] methods = {"QR Code"};
        paymentMethodCombo = new JComboBox<>(methods);

        proceedButton = new JButton("Proceed to Payment");

        // Add components to panel
        add(amountLabel);
        add(amountField);
        add(methodLabel);
        add(paymentMethodCombo);
        add(new JLabel()); // Empty space
        add(proceedButton);

        // Action listener for proceed button
        proceedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (paymentMethodCombo.getSelectedItem().equals("QR Code")) {
                    // Show QRPanel for QR code selection
                    showQRPanel();
                }
            }
        });
    }

    private void showQRPanel() {
        // Create a new frame to display QRPanel
        JFrame qrFrame = new JFrame("QR Payment");
        qrFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        qrFrame.setSize(400, 500);
        qrFrame.add(qrPanel);
        qrFrame.setVisible(true);

        // Pass payment details to QRPanel
        double amount = Double.parseDouble(amountField.getText());
        qrPanel.setPaymentDetails(amount, order, customer, paymentService);
    }
}