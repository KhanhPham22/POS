package ui;

import javax.swing.*;
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
import java.io.File;
import ui.Elements.SearchBar;

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
    private SearchBar searchBar;
    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#,###.000 VND");
    private static final String PRODUCT_IMG_DIR = "C:\\TTTN\\POS PROJECT\\img\\Product\\";

    public MenuPanel(CategoryService categoryService, ProductService productService, 
                     PersonService personService, PaymentService paymentService, boolean isEmployee) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.personService = personService;
        this.paymentService = paymentService;
        this.cartItems = new ArrayList<>();
        this.selectedCustomer = null;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        initializeUI(isEmployee);
        
        cartPanel = new CartPanel(cartItems);
    }

    private void initializeUI(boolean isEmployee) {
        // Top panel with Search bar and Cart button
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Search bar
        searchBar = new SearchBar(query -> filterProductsBySearch(query));
        searchBar.setPlaceholder("Search...");
        topPanel.add(searchBar, BorderLayout.CENTER);

        // Cart button with scaled image
        JButton cartButton = new JButton("Cart");
        cartButton.setBackground(new Color(70, 130, 180));
        cartButton.setForeground(Color.WHITE);
        try {
            ImageIcon cartIcon = new ImageIcon(new ImageIcon("C:\\TTTN\\POS PROJECT\\img\\cart_icon.png")
                .getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH));
            cartButton.setIcon(cartIcon);
            cartButton.setHorizontalTextPosition(SwingConstants.RIGHT);
            cartButton.setIconTextGap(5);
        } catch (Exception e) {
            System.err.println("Failed to load cart icon: " + e.getMessage());
        }
        cartButton.addActionListener(e -> showCart());
        topPanel.add(cartButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Center panel to hold category and product display
        JPanel centerPanel = new JPanel(new BorderLayout());

        // Category panel above product display
        categoryPanel = new CategoryPanel(categoryService, 
            category -> filterProductsByCategory(category), false);
        centerPanel.add(categoryPanel, BorderLayout.NORTH);

        // Product display panel (4 products per row)
        productDisplayPanel = new JPanel(new GridLayout(0, 4, 10, 10));
        productDisplayPanel.setBackground(Color.WHITE);
        productDisplayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.add(new JScrollPane(productDisplayPanel), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Initialize product detail panel (hidden initially)
        productDetailPanel = new JPanel(new BorderLayout());
        productDetailPanel.setVisible(false);
        add(productDetailPanel, BorderLayout.EAST);

        // Load products initially
        loadProducts(null);
    }

    private void filterProductsByCategory(Category category) {
        loadProducts(category);
    }

    private void filterProductsBySearch(String query) {
        List<Product> products = productService.getAllProducts(1, Integer.MAX_VALUE);
        if (query != null && !query.isEmpty()) {
            products = products.stream()
                .filter(p -> p.getName() != null && p.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        }
        loadProductsWithList(products);
    }

    private void loadProducts(Category category) {
        List<Product> products = productService.getAllProducts(1, Integer.MAX_VALUE);
        if (category != null) {
            products = products.stream()
                .filter(p -> p.getCategory() != null && p.getCategory().getId() == category.getId())
                .collect(Collectors.toList());
        }
        // Deduplicate by name to show only one representative product
        products = products.stream()
            .collect(Collectors.toMap(
                Product::getName,
                p -> p,
                (existing, replacement) -> existing
            ))
            .values()
            .stream()
            .collect(Collectors.toList());
        loadProductsWithList(products);
    }

    private void loadProductsWithList(List<Product> products) {
        productDisplayPanel.removeAll();
        if (products.isEmpty()) {
            productDisplayPanel.add(new JLabel("No products available"));
        } else {
            for (Product product : products) {
                JPanel productPanel = createProductPanel(product);
                productDisplayPanel.add(productPanel);
            }
        }
        productDisplayPanel.revalidate();
        productDisplayPanel.repaint();
    }

    private JPanel createProductPanel(Product product) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Product image
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loadProductImage(product, imageLabel, 150, 150);
        panel.add(imageLabel, BorderLayout.CENTER);

        // Product name
        JLabel nameLabel = new JLabel(product.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(nameLabel, BorderLayout.SOUTH);

        // Click to show details
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showProductDetails(product);
            }
        });

        return panel;
    }

    private void loadProductImage(Product product, JLabel label, int width, int height) {
        ImageIcon icon = null;
        if (product.getImagePath() != null && !product.getImagePath().isEmpty()) {
            File imageFile = new File(product.getImagePath());
            if (imageFile.exists()) {
                icon = new ImageIcon(product.getImagePath());
            } else {
                String fallbackPath = PRODUCT_IMG_DIR + product.getName() + "_" + (product.getSize() != null ? product.getSize() : "Default") + ".jpg";
                File fallbackFile = new File(fallbackPath);
                if (fallbackFile.exists()) {
                    icon = new ImageIcon(fallbackPath);
                }
            }
        } else {
            String fallbackPath = PRODUCT_IMG_DIR + product.getName() + "_" + (product.getSize() != null ? product.getSize() : "Default") + ".jpg";
            File fallbackFile = new File(fallbackPath);
            if (fallbackFile.exists()) {
                icon = new ImageIcon(fallbackPath);
            } else {
                File defaultFile = new File(PRODUCT_IMG_DIR + "default.png");
                String defaultPath = defaultFile.exists() ? PRODUCT_IMG_DIR + "default.png" : "C:\\TTTN\\POS PROJECT\\img\\default.png";
                icon = new ImageIcon(defaultPath);
            }
        }
        if (icon != null) {
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaledImage));
        } else {
            label.setIcon(new ImageIcon());
        }
    }

    private void showProductDetails(Product product) {
        productDetailPanel.removeAll();
        productDetailPanel.setVisible(true);

        JPanel detailContent = new JPanel(new GridLayout(0, 1, 5, 5));
        detailContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        detailContent.setBackground(Color.WHITE);

        // Product image
        JLabel imageLabel = new JLabel();
        loadProductImage(product, imageLabel, 150, 150);
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
        boolean isDesserts = product.getCategory() != null && product.getCategory().getName().equalsIgnoreCase("Desserts");
        String[] sizeOptions = isDesserts ? new String[]{"Default"} : new String[]{"S", "M", "L"};
        sizeComboBox = new JComboBox<>(sizeOptions);
        sizeComboBox.setEnabled(!isDesserts); // Disable size selection for desserts
        List<Product> sizeVariants = productService.getAllProducts(1, Integer.MAX_VALUE).stream()
            .filter(p -> p.getName() != null && p.getName().equals(product.getName()))
            .collect(Collectors.toList());
        String defaultSize = sizeVariants.stream()
            .filter(p -> p.getSize() != null)
            .findFirst()
            .map(p -> p.getSize())
            .orElse(isDesserts ? "Default" : "S");
        sizeComboBox.setSelectedItem(defaultSize);
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

        // Update price when size changes
        sizeComboBox.addActionListener(e -> updateProductDetails(product));
        quantityField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateProductDetails(product);
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

    private void updateProductDetails(Product product) {
        try {
            String selectedSize = (String) sizeComboBox.getSelectedItem();
            Product updatedProduct = productService.getProductByNameAndSize(product.getName(), selectedSize);
            if (updatedProduct == null) {
                updatedProduct = product; // Fallback to original product if size variant not found
            }
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity >= 1 && quantity <= updatedProduct.getQuantity()) {
                BigDecimal total = updatedProduct.getPrice().multiply(new BigDecimal(quantity));
                totalPriceLabel.setText("Total: " + formatPrice(total));
                // Update displayed price and stock
                for (Component comp : productDetailPanel.getComponents()) {
                    if (comp instanceof JPanel) {
                        for (Component subComp : ((JPanel) comp).getComponents()) {
                            if (subComp instanceof JLabel) {
                                JLabel label = (JLabel) subComp;
                                if (label.getText().startsWith("Price:")) {
                                    label.setText("Price: " + formatPrice(updatedProduct.getPrice()));
                                } else if (label.getText().startsWith("Quantity in Stock:")) {
                                    label.setText("Quantity in Stock: " + updatedProduct.getQuantity());
                                }
                            }
                        }
                    }
                }
            } else {
                quantityField.setText("1");
                totalPriceLabel.setText("Total: " + formatPrice(updatedProduct.getPrice()));
            }
        } catch (NumberFormatException ex) {
            quantityField.setText("1");
            totalPriceLabel.setText("Total: " + formatPrice(product.getPrice()));
        }
    }

    private String formatPrice(BigDecimal price) {
        return PRICE_FORMAT.format(price);
    }

    private void addToCart(Product product) {
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            String selectedSize = (String) sizeComboBox.getSelectedItem();
            Product cartProduct = productService.getProductByNameAndSize(product.getName(), selectedSize);
            if (cartProduct == null) {
                cartProduct = product; // Fallback to original product
            }
            if (quantity < 1 || quantity > cartProduct.getQuantity()) {
                JOptionPane.showMessageDialog(this, "Invalid quantity!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create a new product instance for the cart
            Product newCartProduct = new Product();
            newCartProduct.setId(cartProduct.getId());
            newCartProduct.setName(cartProduct.getName());
            newCartProduct.setPrice(cartProduct.getPrice());
            newCartProduct.setSize(selectedSize);
            newCartProduct.setQuantity(quantity);
            newCartProduct.setCategory(cartProduct.getCategory());
            newCartProduct.setImagePath(cartProduct.getImagePath());
            newCartProduct.setEan13(cartProduct.getEan13());

            cartItems.add(newCartProduct);
            JOptionPane.showMessageDialog(this, "Added " + cartProduct.getName() + " to cart!");
            cartPanel.updateCartDisplay();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showCart() {
        JFrame cartFrame = new JFrame("Cart");
        cartFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cartFrame.setSize(600, 400);

        cartPanel.updateCartDisplay();
        cartFrame.add(cartPanel);

        // Add Confirm Purchase button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton confirmButton = new JButton("Confirm Purchase");
        confirmButton.setBackground(new Color(50, 205, 50));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.addActionListener(e -> showCustomerSelection(cartPanel.getCartTotal()));
        buttonPanel.add(confirmButton);
        cartFrame.add(buttonPanel, BorderLayout.SOUTH);

        cartFrame.setLocationRelativeTo(null);
        cartFrame.setVisible(true);
    }

    private void showCustomerSelection(BigDecimal totalAmount) {
        JDialog customerDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            "Customer Information", true);
        customerDialog.setSize(400, 500);
        customerDialog.setLayout(new BorderLayout());

        JPanel selectionPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Option to add new customer or select existing
        JRadioButton newCustomerRadio = new JRadioButton("Add New Customer");
        JRadioButton existingCustomerRadio = new JRadioButton("Select Existing Customer");
        ButtonGroup customerGroup = new ButtonGroup();
        customerGroup.add(newCustomerRadio);
        customerGroup.add(existingCustomerRadio);
        newCustomerRadio.setSelected(true);

        // New customer form with Middle Name field
        JPanel newCustomerPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField firstNameField = new JTextField();
        JTextField middleNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField dobField = new JTextField();
        newCustomerPanel.add(new JLabel("First Name:"));
        newCustomerPanel.add(firstNameField);
        newCustomerPanel.add(new JLabel("Middle Name:"));
        newCustomerPanel.add(middleNameField);
        newCustomerPanel.add(new JLabel("Last Name:"));
        newCustomerPanel.add(lastNameField);
        newCustomerPanel.add(new JLabel("Phone:"));
        newCustomerPanel.add(phoneField);
        newCustomerPanel.add(new JLabel("Date of Birth (YYYY-MM-DD):"));
        newCustomerPanel.add(dobField);

        // Existing customer search
        JPanel existingCustomerPanel = new JPanel(new BorderLayout());
        JList<String> customerList = new JList<>();
        DefaultListModel<String> customerListModel = new DefaultListModel<>();
        customerList.setModel(customerListModel);
        customerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        try {
            List<Customer> customers = personService.getAllCustomers(1, Integer.MAX_VALUE);
            for (Customer c : customers) {
                customerListModel.addElement(getCustomerFullName(c));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading customers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        // Pass the SearchListener directly to the SearchBar constructor
        SearchBar customerSearchBar = new SearchBar(query -> {
            customerListModel.clear();
            try {
                List<Customer> customers = personService.getAllCustomers(1, Integer.MAX_VALUE);
                for (Customer c : customers) {
                    String fullName = getCustomerFullName(c);
                    if (fullName.toLowerCase().contains(query.toLowerCase())) {
                        customerListModel.addElement(fullName);
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error searching customers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        customerSearchBar.setPlaceholder("Search by name...");
        existingCustomerPanel.add(customerSearchBar, BorderLayout.NORTH);
        existingCustomerPanel.add(new JScrollPane(customerList), BorderLayout.CENTER);

        // Enable/disable panels based on selection
        newCustomerPanel.setEnabled(true);
        existingCustomerPanel.setEnabled(false);
        newCustomerRadio.addActionListener(e -> {
            newCustomerPanel.setEnabled(true);
            existingCustomerPanel.setEnabled(false);
        });
        existingCustomerRadio.addActionListener(e -> {
            newCustomerPanel.setEnabled(false);
            existingCustomerPanel.setEnabled(true);
        });

        selectionPanel.add(newCustomerRadio);
        selectionPanel.add(newCustomerPanel);
        selectionPanel.add(existingCustomerRadio);
        selectionPanel.add(existingCustomerPanel);

        // Proceed to payment button
        JButton proceedButton = new JButton("Proceed to Payment");
        proceedButton.setBackground(new Color(70, 130, 180));
        proceedButton.setForeground(Color.WHITE);
        proceedButton.addActionListener(e -> {
            if (newCustomerRadio.isSelected()) {
                try {
                    Customer customer = new Customer();
                    customer.setPersonFirstName(firstNameField.getText().trim());
                    customer.setPersonMiddleName(middleNameField.getText().trim());
                    customer.setPersonLastName(lastNameField.getText().trim());
                    customer.setPhone(phoneField.getText().trim());
                    customer.setDateOfBirth(dobField.getText().trim());
                    customer.generateCustomerNumber();
                    personService.createCustomer(customer);
                    selectedCustomer = customer;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error creating customer: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                try {
                    String selectedName = customerList.getSelectedValue();
                    if (selectedName == null) {
                        JOptionPane.showMessageDialog(this, "Please select a customer!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    List<Customer> customers = personService.getAllCustomers(1, Integer.MAX_VALUE);
                    selectedCustomer = customers.stream()
                        .filter(c -> getCustomerFullName(c).equals(selectedName))
                        .findFirst()
                        .orElse(null);
                    if (selectedCustomer == null) {
                        JOptionPane.showMessageDialog(this, "Customer not found!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error selecting customer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            customerDialog.dispose();
            proceedToPayment(totalAmount);
        });

        customerDialog.add(selectionPanel, BorderLayout.CENTER);
        customerDialog.add(proceedButton, BorderLayout.SOUTH);
        customerDialog.setLocationRelativeTo(null);
        customerDialog.setVisible(true);
    }

    private String getCustomerFullName(Customer customer) {
        return (customer.getPersonFirstName() != null ? customer.getPersonFirstName() : "") + " " +
               (customer.getPersonMiddleName() != null ? customer.getPersonMiddleName() + " " : "") +
               (customer.getPersonLastName() != null ? customer.getPersonLastName() : "");
    }

    private void proceedToPayment(BigDecimal totalAmount) {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create OrderDetail
        OrderDetail order = new OrderDetail();
        order.setTotalAmount(totalAmount.doubleValue());

        // Show PaymentPanel
        JDialog paymentDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            "Payment Options", true);
        paymentDialog.setSize(300, 200);
        PaymentPanel paymentPanel = new PaymentPanel(order, selectedCustomer);
        paymentDialog.add(paymentPanel);
        paymentDialog.setLocationRelativeTo(null);
        paymentDialog.setVisible(true);

        // Reset after payment
        paymentDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                resetAfterPayment();
            }
        });
    }

    private void resetAfterPayment() {
        cartItems.clear();
        selectedCustomer = null;
        loadProducts(null);
        productDetailPanel.setVisible(false);
        cartPanel.updateCartDisplay();
        JOptionPane.showMessageDialog(this, "Returned to Menu");
    }
}