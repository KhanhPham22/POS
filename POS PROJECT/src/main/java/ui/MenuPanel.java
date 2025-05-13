package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import model.Category;
import model.Product;
import model.Customer;
import model.OrderDetail;
import model.Payment;
import service.CategoryService;
import service.ProductService;
import service.PersonService;
import service.PaymentService;

public class MenuPanel extends JPanel {

   

    private final CategoryService categoryService;
    private final ProductService productService;
    private final PersonService personService;
    private final PaymentService paymentService;
    private final List<Product> cartItems;
    private Customer selectedCustomer;

    private CategoryPanel categoryPanel;
    private JPanel productDisplayPanel;
    private JPanel productDetailPanel;
    private CartPanel cartPanel;
    private JComboBox<String> sizeComboBox;
    private JTextField quantityField;
    private JLabel totalPriceLabel;

    public MenuPanel(CategoryService categoryService, ProductService productService, 
                     PersonService personService, PaymentService paymentService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.personService = personService;
        this.paymentService = paymentService;
        this.cartItems = new ArrayList<>();
        this.selectedCustomer = null;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        initializeUI();
        
        cartPanel = new CartPanel(cartItems);
    }

    private void initializeUI() {
        // Top panel with Add Customer button and Cart button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addCustomerButton = new JButton("Add Customer");
        addCustomerButton.setBackground(new Color(50, 205, 50));
        addCustomerButton.setForeground(Color.WHITE);
        addCustomerButton.addActionListener(e -> {
			try {
				showAddCustomerDialog();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

        JButton cartButton = new JButton("View Cart");
        cartButton.setBackground(new Color(70, 130, 180));
        cartButton.setForeground(Color.WHITE);
        cartButton.addActionListener(e -> showCart());

        topPanel.add(addCustomerButton);
        topPanel.add(cartButton);
        add(topPanel, BorderLayout.NORTH);

        // Category panel for filtering
        categoryPanel = new CategoryPanel(categoryService, 
            category -> filterProductsByCategory(category), false);
        add(categoryPanel, BorderLayout.WEST);

        // Product display panel
        productDisplayPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        productDisplayPanel.setBackground(Color.WHITE);
        JScrollPane productScrollPane = new JScrollPane(productDisplayPanel);
        add(productScrollPane, BorderLayout.CENTER);

        // Initialize product detail panel (hidden initially)
        productDetailPanel = new JPanel(new BorderLayout());
        productDetailPanel.setVisible(false);
        add(productDetailPanel, BorderLayout.EAST);

        // Load all products initially
        loadProducts(null);
    }

    private void filterProductsByCategory(Category category) {
        loadProducts(category);
    }

    private void loadProducts(Category category) {
        productDisplayPanel.removeAll();
        List<Product> products = productService.getAllProducts(1, Integer.MAX_VALUE);
        if (category != null) {
            products = products.stream()
                .filter(p -> p.getCategory() != null && p.getCategory().getId() == category.getId())
                .collect(Collectors.toList());
        }

        for (Product product : products) {
            JPanel productCard = createProductCard(product);
            productDisplayPanel.add(productCard);
        }

        productDisplayPanel.revalidate();
        productDisplayPanel.repaint();
    }

    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(200, 250));

        // Product image
        JLabel imageLabel = new JLabel();
        if (product.getImagePath() != null && !product.getImagePath().isEmpty()) {
            ImageIcon icon = new ImageIcon(new ImageIcon(product.getImagePath())
                .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
            imageLabel.setIcon(icon);
        }
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(imageLabel, BorderLayout.CENTER);

        // Product name and detail button
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        JLabel nameLabel = new JLabel(product.getName());
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JButton detailButton = new JButton("View Details");
        detailButton.setBackground(new Color(70, 130, 180));
        detailButton.setForeground(Color.WHITE);
        detailButton.addActionListener(e -> showProductDetails(product));

        infoPanel.add(nameLabel);
        infoPanel.add(detailButton);
        card.add(infoPanel, BorderLayout.SOUTH);

        return card;
    }

    private void showProductDetails(Product product) {
        productDetailPanel.removeAll();
        productDetailPanel.setVisible(true);

        JPanel detailContent = new JPanel(new GridLayout(0, 1, 5, 5));
        detailContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        detailContent.setBackground(Color.WHITE);

        // Product image
        JLabel imageLabel = new JLabel();
        if (product.getImagePath() != null && !product.getImagePath().isEmpty()) {
            ImageIcon icon = new ImageIcon(new ImageIcon(product.getImagePath())
                .getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH));
            imageLabel.setIcon(icon);
        }
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        detailContent.add(imageLabel);

        // Product info
        detailContent.add(new JLabel("Name: " + product.getName()));
        detailContent.add(new JLabel("Price: " + formatPrice(product.getPrice())));
        detailContent.add(new JLabel("Category: " + 
            (product.getCategory() != null ? product.getCategory().getName() : "N/A")));
        detailContent.add(new JLabel("Quantity in Stock: " + product.getQuantity()));

        // Size selection
        JLabel sizeLabel = new JLabel("Size:");
        sizeComboBox = new JComboBox<>(new String[]{"S", "M", "L", "Default"});
        sizeComboBox.setSelectedItem(product.getSize() != null ? product.getSize() : "Default");
        JPanel sizePanel = new JPanel(new FlowLayout());
        sizePanel.add(sizeLabel);
        sizePanel.add(sizeComboBox);
        detailContent.add(sizePanel);

        // Quantity selection
        JLabel quantityLabel = new JLabel("Quantity:");
        quantityField = new JTextField("1", 5);
        JButton minusButton = new JButton("-");
        JButton plusButton = new JButton("+");
        minusButton.addActionListener(e -> adjustQuantity(-1, product.getQuantity()));
        plusButton.addActionListener(e -> adjustQuantity(1, product.getQuantity()));
        JPanel quantityPanel = new JPanel(new FlowLayout());
        quantityPanel.add(quantityLabel);
        quantityPanel.add(minusButton);
        quantityPanel.add(quantityField);
        quantityPanel.add(plusButton);
        detailContent.add(quantityPanel);

        // Total price
        totalPriceLabel = new JLabel("Total: " + 
            formatPrice(product.getPrice().multiply(new BigDecimal(quantityField.getText()))));
        detailContent.add(totalPriceLabel);

        // Add to Cart button
        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.setBackground(new Color(50, 205, 50));
        addToCartButton.setForeground(Color.WHITE);
        addToCartButton.addActionListener(e -> addToCart(product));
        detailContent.add(addToCartButton);

        productDetailPanel.add(detailContent, BorderLayout.CENTER);
        productDetailPanel.revalidate();
        productDetailPanel.repaint();

        // Update total price when size or quantity changes
        sizeComboBox.addActionListener(e -> updateTotalPrice(product));
        quantityField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateTotalPrice(product);
            }
        });
    }

    private void adjustQuantity(int change, int maxQuantity) {
        try {
            int current = Integer.parseInt(quantityField.getText());
            int newQuantity = current + change;
            if (newQuantity >= 1 && newQuantity <= maxQuantity) {
                quantityField.setText(String.valueOf(newQuantity));
            }
        } catch (NumberFormatException ex) {
            quantityField.setText("1");
        }
    }

    private void updateTotalPrice(Product product) {
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity >= 1 && quantity <= product.getQuantity()) {
                BigDecimal total = product.getPrice().multiply(new BigDecimal(quantity));
                totalPriceLabel.setText("Total: " + formatPrice(total));
            } else {
                quantityField.setText("1");
                totalPriceLabel.setText("Total: " + formatPrice(product.getPrice()));
            }
        } catch (NumberFormatException ex) {
            quantityField.setText("1");
            totalPriceLabel.setText("Total: " + formatPrice(product.getPrice()));
        }
    }

    private String formatPrice(BigDecimal price) {
        DecimalFormat df = new DecimalFormat("#,###.000 VND");
        return df.format(price);
    }

    private void addToCart(Product product) {
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            String selectedSize = (String) sizeComboBox.getSelectedItem();
            if (quantity < 1 || quantity > product.getQuantity()) {
                JOptionPane.showMessageDialog(this, "Invalid quantity!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create a new product instance for the cart to avoid modifying the original
            Product cartProduct = new Product();
            cartProduct.setId(product.getId());
            cartProduct.setName(product.getName());
            cartProduct.setPrice(product.getPrice());
            cartProduct.setSize(selectedSize);
            cartProduct.setQuantity(quantity);
            cartProduct.setCategory(product.getCategory());
            cartProduct.setImagePath(product.getImagePath());
            cartProduct.setEan13(product.getEan13());

            cartItems.add(cartProduct);
            JOptionPane.showMessageDialog(this, "Added " + product.getName() + " to cart!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getCustomerFullName(Customer customer) {
        StringBuilder fullName = new StringBuilder();
        if (customer.getPersonFirstName() != null) {
            fullName.append(customer.getPersonFirstName());
        }
        if (customer.getPersonMiddleName() != null) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(customer.getPersonMiddleName());
        }
        if (customer.getPersonLastName() != null) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(customer.getPersonLastName());
        }
        return fullName.length() > 0 ? fullName.toString() : "Unknown";
    }
    
    private void showAddCustomerDialog() throws Exception {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            "Add New Customer", true);
        dialog.setSize(400, 450);
        CustomerPanel customerPanel = new CustomerPanel(personService);
        dialog.add(customerPanel);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        List<Customer> customers = personService.getAllCustomers(1, Integer.MAX_VALUE);
        if (!customers.isEmpty()) {
            selectedCustomer = customers.get(customers.size() - 1);
            JOptionPane.showMessageDialog(this, "Customer added: " + 
                getCustomerFullName(selectedCustomer)); // Use new method
        }
    }

    private void showCart() {
        JFrame cartFrame = new JFrame("Cart");
        cartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cartFrame.setSize(600, 400);

        cartPanel.updateCartDisplay(); // Refresh cart display
        cartFrame.add(cartPanel);
        
        // Add Proceed to Payment button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton checkoutButton = new JButton("Proceed to Payment");
        checkoutButton.setBackground(new Color(50, 205, 50));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.addActionListener(e -> proceedToPayment(cartPanel.getCartTotal()));
        buttonPanel.add(checkoutButton);
        cartFrame.add(buttonPanel, BorderLayout.SOUTH);

        cartFrame.setLocationRelativeTo(null);
        cartFrame.setVisible(true);
    }

    private void updateCartPanel() {
        cartPanel.removeAll();
        cartPanel.setLayout(new BorderLayout());

        // Cart items table
        String[] columns = {"Name", "Size", "Quantity", "Price", "Total"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable cartTable = new JTable(tableModel);
        cartTable.setRowHeight(30);

        // Calculate cart total
        final BigDecimal cartTotal = calculateCartTotal(tableModel);

        JScrollPane scrollPane = new JScrollPane(cartTable);
        cartPanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with total and payment button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JLabel totalLabel = new JLabel("Total: " + formatPrice(cartTotal));
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        JButton checkoutButton = new JButton("Proceed to Payment");
        checkoutButton.setBackground(new Color(50, 205, 50));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.addActionListener(e -> proceedToPayment(cartTotal));

        bottomPanel.add(totalLabel, BorderLayout.WEST);
        bottomPanel.add(checkoutButton, BorderLayout.EAST);
        cartPanel.add(bottomPanel, BorderLayout.SOUTH);

        cartPanel.revalidate();
        cartPanel.repaint();
    }

    private BigDecimal calculateCartTotal(DefaultTableModel tableModel) {
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
        return cartTotal;
    }

    private void proceedToPayment(BigDecimal totalAmount) {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create OrderDetail
        OrderDetail order = new OrderDetail();
        order.setTotalAmount(totalAmount.doubleValue());
        // Note: OrderDetail should be saved to the database via a service, 
        // but for simplicity, we assume it's created here

        JDialog paymentDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            "Payment Options", true);
        paymentDialog.setSize(300, 200);
        paymentDialog.setLayout(new FlowLayout());

        JButton cashButton = new JButton("Pay with Cash");
        cashButton.setBackground(new Color(70, 130, 180));
        cashButton.setForeground(Color.WHITE);
        JButton qrButton = new JButton("Pay with QR");
        qrButton.setBackground(new Color(70, 130, 180));
        qrButton.setForeground(Color.WHITE);

        cashButton.addActionListener(e -> processCashPayment(order));
        qrButton.addActionListener(e -> showQRPayment(order));

        paymentDialog.add(cashButton);
        paymentDialog.add(qrButton);
        paymentDialog.setLocationRelativeTo(null);
        paymentDialog.setVisible(true);
    }

    private void processCashPayment(OrderDetail order) {
        Payment payment = new Payment();
        payment.setPaymentMethod("Cash");
        payment.setAmount(order.getTotalAmount());
        payment.setStatus("COMPLETED");
        payment.setOrder(order);
        payment.setCustomer(selectedCustomer);

        if (paymentService.createPayment(payment)) {
            JOptionPane.showMessageDialog(this, "Payment completed successfully!");
            resetAfterPayment();
        } else {
            JOptionPane.showMessageDialog(this, "Payment failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showQRPayment(OrderDetail order) {
        JFrame qrFrame = new JFrame("QR Payment");
        qrFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        qrFrame.setSize(400, 500);
        QRPanel qrPanel = new QRPanel();
        qrPanel.setPaymentDetails(order.getTotalAmount(), order, selectedCustomer, paymentService);
        qrFrame.add(qrPanel);
        qrFrame.setLocationRelativeTo(null);
        qrFrame.setVisible(true);

        // Listen for QR frame disposal to reset if payment is successful
        qrFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // Check payment status via paymentService if needed
                resetAfterPayment();
            }
        });
    }
    
    

    private void resetAfterPayment() {
        cartItems.clear();
        selectedCustomer = null;
        loadProducts(null);
        productDetailPanel.setVisible(false);
        updateCartPanel();
        JOptionPane.showMessageDialog(this, "Returned to Menu");
    }
}