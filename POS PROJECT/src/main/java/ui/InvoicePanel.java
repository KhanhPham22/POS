package ui;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import model.Invoice;
import model.Product;

/**
 * InvoicePanel displays invoice details.
 */
public class InvoicePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#,###.000 VND");

    /**
     * Constructor initializes the invoice panel.
     */
    public InvoicePanel(Invoice invoice, List<Product> cartItems) {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        initializeUI(invoice, cartItems);
    }

    /**
     * Initializes the UI components.
     */
    private void initializeUI(Invoice invoice, List<Product> cartItems) {
        JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Invoice Details", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        add(titleLabel, BorderLayout.NORTH);

        detailsPanel.add(createLabel("Invoice ID: " + invoice.getId(), Font.PLAIN, 16));
        detailsPanel.add(createLabel("Customer: " + getCustomerFullName(invoice.getCustomer()), Font.PLAIN, 16));
        detailsPanel.add(createLabel("Payment Method: " + invoice.getPaymentMethod(), Font.PLAIN, 16));
        detailsPanel.add(createLabel("Total Price: " + PRICE_FORMAT.format(invoice.getTotalPrice()), Font.PLAIN, 16));
        detailsPanel.add(createLabel("Discount: " + PRICE_FORMAT.format(invoice.getDiscount()), Font.PLAIN, 16));
        detailsPanel.add(createLabel("Final Price: " + PRICE_FORMAT.format(invoice.getFinalPrice()), Font.PLAIN, 16));
        detailsPanel.add(createLabel("Invoice Date: " + invoice.getInvoiceDay(), Font.PLAIN, 16));
        detailsPanel.add(createLabel("Items Purchased:", Font.BOLD, 16));
        for (Product item : cartItems) {
            detailsPanel.add(createLabel("- " + item.getName() + " (" + item.getSize() + ") x" + item.getQuantity() + 
                " - " + PRICE_FORMAT.format(item.getPrice().multiply(new java.math.BigDecimal(item.getQuantity()))), 
                Font.PLAIN, 14));
        }

        add(detailsPanel, BorderLayout.CENTER);
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
    private String getCustomerFullName(model.Customer customer) {
        return (customer.getPersonFirstName() != null ? customer.getPersonFirstName() : "") + " " +
               (customer.getPersonMiddleName() != null ? customer.getPersonMiddleName() + " " : "") +
               (customer.getPersonLastName() != null ? customer.getPersonLastName() : "");
    }
}