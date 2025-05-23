package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import model.Payment;
import model.OrderDetail;
import model.Customer;
import service.PaymentService;

public class QRPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JLabel qrLabel;
    private JButton confirmPaymentButton;
    private JLabel countdownLabel;
    private Timer countdownTimer;
    private Runnable onPaymentComplete;
    private int countdownSeconds = 30;
    private double amount;
    private OrderDetail order;
    private Customer customer;
    private PaymentService paymentService;

    public QRPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Initialize components
        JLabel titleLabel = new JLabel("Momo Payment", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Bold title
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        qrLabel = new JLabel();
        qrLabel.setHorizontalAlignment(SwingConstants.CENTER);
        confirmPaymentButton = createStyledButton("Confirm Payment");
        countdownLabel = new JLabel("Time remaining: 30s");
        countdownLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        countdownLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Add components to QRPanel
        add(titleLabel, BorderLayout.NORTH);
        add(qrLabel, BorderLayout.CENTER);
        add(countdownLabel, BorderLayout.SOUTH);
        add(confirmPaymentButton, BorderLayout.SOUTH);

        // Action listener for confirm button
        confirmPaymentButton.addActionListener(e -> processPayment());
    }

    public void setPaymentDetails(double amount, OrderDetail order, Customer customer, PaymentService paymentService) {
        this.amount = amount;
        this.order = order;
        this.customer = customer;
        this.paymentService = paymentService;
        displayDynamicQRCode(); // Generate QR code only after order is set
        startCountdown(); // Start countdown only after QR is generated
    }
    
    public void setOnPaymentCompleteListener(Runnable listener) {
        this.onPaymentComplete = listener;
    }

    public void displayDynamicQRCode() {
        if (order == null) {
            qrLabel.setText("Order details not set.");
            return;
        }
        try {
            String qrContent = String.format(
                "{\"bank\":\"Momo\",\"amount\":%.2f,\"orderId\":%d,\"customer\":\"%s\"}",
                amount, order.getId(), getCustomerFullName(customer)
            );

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 400, 400);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            qrLabel.setIcon(new ImageIcon(qrImage));
        } catch (WriterException e) {
            qrLabel.setText("Failed to generate QR code: " + e.getMessage());
        }
    }

    private void startCountdown() {
        if (countdownTimer != null && countdownTimer.isRunning()) {
            countdownTimer.stop();
        }
        countdownTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countdownSeconds--;
                countdownLabel.setText("Time remaining: " + countdownSeconds + "s");
                if (countdownSeconds <= 0) {
                    countdownTimer.stop();
                    processAutoPayment();
                }
            }
        });
        countdownTimer.start();
    }

    private void processAutoPayment() {
        if (order == null) {
            JOptionPane.showMessageDialog(this, "Order details not set.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Payment payment = new Payment();
        payment.setPaymentMethod("QR - Momo");
        payment.setAmount(amount);
        payment.setStatus("COMPLETED");
        payment.setOrder(order);
        payment.setCustomer(customer);

        boolean success = paymentService.createPayment(payment);
        if (success) {
            JOptionPane.showMessageDialog(this, "Payment completed automatically after 30 seconds!");
            SwingUtilities.getWindowAncestor(this).dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to process payment.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void processPayment() {
        if (order == null) {
            JOptionPane.showMessageDialog(this, "Order details not set.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Payment payment = new Payment();
        payment.setPaymentMethod("QR - Momo");
        payment.setAmount(amount);
        payment.setStatus("PENDING");
        payment.setOrder(order);
        payment.setCustomer(customer);

        boolean success = paymentService.createPayment(payment);
        if (success) {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Please scan the QR code with Momo app and confirm payment.", 
                "Confirm Payment", JOptionPane.OK_CANCEL_OPTION);
            if (confirm == JOptionPane.OK_OPTION) {
                payment.setStatus("COMPLETED");
                paymentService.updatePayment(payment);
                JOptionPane.showMessageDialog(this, "Payment completed successfully!");
                SwingUtilities.getWindowAncestor(this).dispose();
            } else {
                payment.setStatus("CANCELLED");
                paymentService.updatePayment(payment);
                JOptionPane.showMessageDialog(this, "Payment cancelled.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Failed to process payment.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getCustomerFullName(Customer customer) {
        return (customer.getPersonFirstName() != null ? customer.getPersonFirstName() : "") + " " +
               (customer.getPersonMiddleName() != null ? customer.getPersonMiddleName() + " " : "") +
               (customer.getPersonLastName() != null ? customer.getPersonLastName() : "");
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 160, 210));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
        return button;
    }
    
    public String getSelectedBank() {
        return "Momo"; // Hardcoded to Momo since button is removed
    }
}