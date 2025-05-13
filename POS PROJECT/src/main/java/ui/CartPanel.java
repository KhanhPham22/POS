package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import model.Product;

public class CartPanel extends JPanel {
    private final List<Product> cartItems; 
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#,###.000 VND");

    public CartPanel(List<Product> cartItems) {
        this.cartItems = cartItems;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        initializeUI();
        updateCartDisplay();
    }

    private void initializeUI() {
        // Cart items table
        String[] columns = {"Name", "Size", "Quantity", "Price", "Total"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable cartTable = new JTable(tableModel);
        cartTable.setRowHeight(30);
        cartTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cartTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        cartTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(cartTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with total
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        totalLabel = new JLabel("Total: 0 VND");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        bottomPanel.add(totalLabel, BorderLayout.WEST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void updateCartDisplay() {
        // Clear existing rows
        tableModel.setRowCount(0);

        // Calculate and display cart items
        BigDecimal cartTotal = BigDecimal.ZERO;
        for (Product item : cartItems) {
            BigDecimal total = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
            tableModel.addRow(new Object[]{
                item.getName(),
                item.getSize(),
                item.getQuantity(),
                formatPrice(item.getPrice()),
                formatPrice(total)
            });
            cartTotal = cartTotal.add(total);
        }

        // Update total label
        totalLabel.setText("Total: " + formatPrice(cartTotal));

        revalidate();
        repaint();
    }

    private String formatPrice(BigDecimal price) {
        return PRICE_FORMAT.format(price);
    }

    public BigDecimal getCartTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (Product item : cartItems) {
            total = total.add(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
        }
        return total;
    }
}