package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import model.Product;

public class CartPanel extends JPanel {
    private final List<Product> cartItems; 
    private DefaultTableModel tableModel;
    private JLabel totalLabel;
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#,###.000 VND");
    private Map<Product, JLabel> quantityLabels; // Map to store quantity labels for each product

    public CartPanel(List<Product> cartItems) {
        this.cartItems = cartItems;
        this.quantityLabels = new HashMap<>(); // Initialize the map
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        initializeUI();
        updateCartDisplay();
    }

    private void initializeUI() {
        // Cart items table with custom renderer for buttons
        String[] columns = {"Name", "Size", "Quantity", "Price", "Total", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Actions column is at index 5
            }
        };
        JTable cartTable = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 5 ? Object.class : super.getColumnClass(column);
            }
        };
        cartTable.setRowHeight(40);
        cartTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cartTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        cartTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        cartTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        cartTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));

        // Adjust column widths for better balance
        cartTable.getColumnModel().getColumn(0).setPreferredWidth(100); // Name
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(50);  // Size
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(50);  // Quantity
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(80);  // Price
        cartTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Total
        cartTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // Actions

        JScrollPane scrollPane = new JScrollPane(cartTable);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with total and clear button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        totalLabel = new JLabel("Total: 0 VND");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        bottomPanel.add(totalLabel, BorderLayout.WEST);

        JButton clearCartButton = new JButton("Clear Cart");
        clearCartButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        clearCartButton.setBackground(new Color(255, 99, 71));
        clearCartButton.setForeground(Color.WHITE);
        clearCartButton.setPreferredSize(new Dimension(100, 30));
        clearCartButton.addActionListener(e -> {
            cartItems.clear();
            quantityLabels.clear(); // Clear the labels map
            updateCartDisplay();
        });
        bottomPanel.add(clearCartButton, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void updateCartDisplay() {
        tableModel.setRowCount(0);
        BigDecimal cartTotal = BigDecimal.ZERO;
        for (Product item : cartItems) {
            BigDecimal total = item.getPrice().multiply(new BigDecimal(item.getQuantity()));
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 0));
            
            // Reuse or create a new JLabel for quantity
            JLabel quantityLabel = quantityLabels.computeIfAbsent(item, k -> new JLabel(String.valueOf(item.getQuantity())));
            quantityLabel.setText(String.valueOf(item.getQuantity())); // Ensure the label is up-to-date
            
            JButton minusButton = new JButton("-");
            styleButton(minusButton);
            minusButton.addActionListener(e -> adjustQuantity(item, -1));
            JButton plusButton = new JButton("+");
            styleButton(plusButton);
            plusButton.addActionListener(e -> adjustQuantity(item, 1));
            buttonPanel.add(minusButton);
            buttonPanel.add(quantityLabel);
            buttonPanel.add(plusButton);

            tableModel.addRow(new Object[]{
                item.getName(),
                item.getSize(),
                item.getQuantity(),
                formatPrice(item.getPrice()),
                formatPrice(total),
                buttonPanel
            });
            cartTotal = cartTotal.add(total);
        }

        totalLabel.setText("Total: " + formatPrice(cartTotal));
        revalidate();
        repaint();
    }

    private void adjustQuantity(Product item, int change) {
        int newQuantity = item.getQuantity() + change;
        if (newQuantity >= 1 && newQuantity <= 100) { // Assuming max quantity is 100
            item.setQuantity(newQuantity);
            // Update the JLabel in the Actions column
            JLabel quantityLabel = quantityLabels.get(item);
            if (quantityLabel != null) {
                quantityLabel.setText(String.valueOf(newQuantity));
            }
            // Update the Quantity column in the table model
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(item.getName()) && tableModel.getValueAt(i, 1).equals(item.getSize())) {
                    tableModel.setValueAt(newQuantity, i, 2); // Update the Quantity column
                    BigDecimal total = item.getPrice().multiply(new BigDecimal(newQuantity));
                    tableModel.setValueAt(formatPrice(total), i, 4); // Update the Total column
                    break;
                }
            }
            // Update the total label
            totalLabel.setText("Total: " + formatPrice(getCartTotal()));
            revalidate();
            repaint();
        }
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(30, 25));
        button.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
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

// Custom renderer for button panel
class ButtonRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return (Component) value;
    }
}

// Custom editor for button panel
class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private JPanel buttonPanel;

    public ButtonEditor(JCheckBox checkBox) {
        buttonPanel = new JPanel();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return (Component) value;
    }

    @Override
    public Object getCellEditorValue() {
        return buttonPanel;
    }
}