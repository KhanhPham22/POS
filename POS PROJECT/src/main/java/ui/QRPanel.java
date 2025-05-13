package ui;

import javax.swing.*;
import model.Payment;
import model.OrderDetail;
import model.Customer;
import service.PaymentService;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class QRPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JButton momoButton;
    private JButton acbButton;
    private JLabel qrLabel;
    private JButton confirmPaymentButton;
    private double amount;
    private OrderDetail order;
    private Customer customer;
    private PaymentService paymentService;
    private String selectedBank;

    public QRPanel() {
        setLayout(new BorderLayout(10, 10));

        // Initialize components
        JPanel buttonPanel = new JPanel(new FlowLayout());
        momoButton = new JButton("Pay with Momo");
        acbButton = new JButton("Pay with ACB Bank");
        qrLabel = new JLabel();
        qrLabel.setHorizontalAlignment(SwingConstants.CENTER);
        confirmPaymentButton = new JButton("Confirm Payment");

        // Add buttons to panel
        buttonPanel.add(momoButton);
        buttonPanel.add(acbButton);

        // Add components to QRPanel
        add(buttonPanel, BorderLayout.NORTH);
        add(qrLabel, BorderLayout.CENTER);
        add(confirmPaymentButton, BorderLayout.SOUTH);

        // Action listeners
        momoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedBank = "Momo";
                displayQRCode("QR Momo.jpg");
            }
        });

        acbButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedBank = "ACB Bank";
                displayQRCode("QR ACB.jpg");
            }
        });

        confirmPaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processPayment();
            }
        });
    }

    public void setPaymentDetails(double amount, OrderDetail order, Customer customer, PaymentService paymentService) {
        this.amount = amount;
        this.order = order;
        this.customer = customer;
        this.paymentService = paymentService;
    }

    private void displayQRCode(String fileName) {
        String qrPath = "C:\\TTTN\\POS PROJECT\\img\\QR\\" + fileName;
        File qrFile = new File(qrPath);
        if (qrFile.exists()) {
            ImageIcon qrImage = new ImageIcon(qrPath);
            // Resize image to fit label
            Image scaledImage = qrImage.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            qrLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            qrLabel.setText("QR code not found: " + qrPath);
        }
    }

    private void processPayment() {
        if (selectedBank == null) {
            JOptionPane.showMessageDialog(this, "Please select a payment method (Momo or ACB Bank).");
            return;
        }

        // Create Payment object
        Payment payment = new Payment();
        payment.setPaymentMethod("QR - " + selectedBank);
        payment.setAmount(amount);
        payment.setStatus("PENDING"); // Initial status
        payment.setOrder(order);
        payment.setCustomer(customer);

        // Save payment to database
        boolean success = paymentService.createPayment(payment);
        if (success) {
            // Simulate QR payment confirmation (in a real system, this would involve API integration)
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Please scan the QR code with " + selectedBank + " app and confirm payment.", 
                "Confirm Payment", JOptionPane.OK_CANCEL_OPTION);
            if (confirm == JOptionPane.OK_OPTION) {
                payment.setStatus("COMPLETED");
                paymentService.updatePayment(payment);
                JOptionPane.showMessageDialog(this, "Payment completed successfully!");
                // Close QRPanel's frame
                SwingUtilities.getWindowAncestor(this).dispose();
            } else {
                payment.setStatus("CANCELLED");
                paymentService.updatePayment(payment);
                JOptionPane.showMessageDialog(this, "Payment cancelled.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to process payment.");
        }
    }
}