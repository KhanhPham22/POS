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

/**
 * QRPanel handles QR code payment.
 */
public class QRPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private JLabel qrLabel;
    private JLabel countdownLabel;
    private Timer countdownTimer;
    private Runnable onPaymentComplete;
    private int countdownSeconds = 30;
    private double amount;
    private OrderDetail order;
    private Customer customer;
    private PaymentService paymentService;

    /**
     * Constructor initializes the QR panel.
     */
    public QRPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Momo Payment", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        qrLabel = new JLabel();
        qrLabel.setHorizontalAlignment(SwingConstants.CENTER);

        countdownLabel = new JLabel("Time remaining: 30s");
        countdownLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        countdownLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(titleLabel, BorderLayout.NORTH);
        add(qrLabel, BorderLayout.CENTER);
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(Color.WHITE);
        southPanel.add(countdownLabel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets payment details.
     */
    public void setPaymentDetails(double amount, OrderDetail order, Customer customer, PaymentService paymentService) {
        this.amount = amount;
        this.order = order;
        this.customer = customer;
        this.paymentService = paymentService;
        displayDynamicQRCode();
        startCountdown();
    }
    
    /**
     * Sets the payment completion callback.
     */
    public void setOnPaymentCompleteListener(Runnable listener) {
        this.onPaymentComplete = listener;
    }

    /**
     * Displays the QR code.
     */
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

    /**
     * Starts the countdown timer.
     */
    private void startCountdown() {
        if (countdownTimer != null && countdownTimer.isRunning()) {
            countdownTimer.stop();
        }
        countdownSeconds = 30;
        countdownLabel.setText("Time remaining: " + countdownSeconds + "s");
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

    /**
     * Processes automatic payment after countdown.
     */
    private void processAutoPayment() {
        if (order == null) {
            JOptionPane.showMessageDialog(this, "Order details not set.", 
                "Error", JOptionPane.ERROR_MESSAGE);
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
            if (onPaymentComplete != null) {
                onPaymentComplete.run();
            }
            SwingUtilities.getWindowAncestor(this).dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to process payment.", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
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
     * Gets the selected bank.
     */
    public String getSelectedBank() {
        return "Momo";
    }
}