package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import model.Category;
import model.Product;
import model.Customer;
import model.OrderDetail;
import model.OrderItem;
import service.CategoryService;
import service.ProductService;
import service.PersonService;
import service.PaymentService;
import service.OrderService;
import java.io.File;
import ui.Elements.SearchBar;

public class MenuPanel extends JPanel {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final PersonService personService;
    private final PaymentService paymentService;
    private final OrderService orderService;
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
    private JPanel mainContentPanel;
    private JPanel centerPanel;

    public MenuPanel(CategoryService categoryService, ProductService productService, 
                     PersonService personService, PaymentService paymentService, 
                     OrderService orderService, boolean isEmployee) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.personService = personService;
        this.paymentService = paymentService;
        this.orderService = orderService;
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

        // Cart button
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

        // Main content panel
        mainContentPanel = new JPanel(new BorderLayout());
        add(mainContentPanel, BorderLayout.CENTER);

        // Center panel for product display
        centerPanel = new JPanel(new BorderLayout());

        // Category panel
        categoryPanel = new CategoryPanel(categoryService, 
            category -> filterProductsByCategory(category), false);
        centerPanel.add(categoryPanel, BorderLayout.NORTH);

        // Product display panel
        productDisplayPanel = new JPanel(new GridLayout(0, 4, 10, 10));
        productDisplayPanel.setBackground(Color.WHITE);
        productDisplayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.add(new JScrollPane(productDisplayPanel), BorderLayout.CENTER);

        mainContentPanel.add(centerPanel, BorderLayout.CENTER);

        // Product detail panel
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

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loadProductImage(product, imageLabel, 150, 150);
        panel.add(imageLabel, BorderLayout.CENTER);

        JLabel nameLabel = new JLabel(product.getName(), SwingConstants.CENTER);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(nameLabel, BorderLayout.SOUTH);

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

        JLabel imageLabel = new JLabel();
        loadProductImage(product, imageLabel, 150, 150);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        detailContent.add(imageLabel);

        detailContent.add(new JLabel("Name: " + product.getName()));
        detailContent.add(new JLabel("Price: " + formatPrice(product.getPrice())));
        detailContent.add(new JLabel("Category: " + 
            (product.getCategory() != null ? product.getCategory().getName() : "N/A")));
        detailContent.add(new JLabel("Quantity in Stock: " + product.getQuantity()));

        JLabel sizeLabel = new JLabel("Size:");
        boolean isDesserts = product.getCategory() != null && product.getCategory().getName().equalsIgnoreCase("Desserts");
        String[] sizeOptions = isDesserts ? new String[]{"Default"} : new String[]{"S", "M", "L"};
        sizeComboBox = new JComboBox<>(sizeOptions);
        sizeComboBox.setEnabled(!isDesserts);
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

        totalPriceLabel = new JLabel("Total: " + 
            formatPrice(product.getPrice().multiply(new BigDecimal(quantityField.getText()))));
        detailContent.add(totalPriceLabel);

        JButton addToCartButton = new JButton("Add to Cart");
        addToCartButton.setBackground(new Color(50, 205, 50));
        addToCartButton.setForeground(Color.WHITE);
        addToCartButton.addActionListener(e -> addToCart(product));
        detailContent.add(addToCartButton);

        productDetailPanel.add(detailContent, BorderLayout.CENTER);
        productDetailPanel.revalidate();
        productDetailPanel.repaint();

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
                updatedProduct = product;
            }
            int quantity = Integer.parseInt(quantityField.getText());
            if (quantity >= 1 && quantity <= updatedProduct.getQuantity()) {
                BigDecimal total = updatedProduct.getPrice().multiply(new BigDecimal(quantity));
                totalPriceLabel.setText("Total: " + formatPrice(total));
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
                JOptionPane.showMessageDialog(this, "Product not found for selected size!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (cartProduct.getId() <= 0) {
                JOptionPane.showMessageDialog(this, "Invalid product ID for " + cartProduct.getName() + "!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            System.out.println("Adding to cart: Product ID=" + cartProduct.getId() + ", Name=" + cartProduct.getName() + ", Size=" + selectedSize);
            
            if (quantity < 1 || quantity > cartProduct.getQuantity()) {
                JOptionPane.showMessageDialog(this, "Invalid quantity!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            cartProduct.setQuantity(quantity);
            cartItems.add(cartProduct);
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

        JPanel selectionPanel = new JPanel();
        selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.Y_AXIS));
        selectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JRadioButton newCustomerRadio = new JRadioButton("Add New Customer");
        JRadioButton existingCustomerRadio = new JRadioButton("Select Existing Customer");
        ButtonGroup customerGroup = new ButtonGroup();
        customerGroup.add(newCustomerRadio);
        customerGroup.add(existingCustomerRadio);
        newCustomerRadio.setSelected(true);

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

        JPanel existingCustomerPanel = new JPanel(new BorderLayout(5, 5));
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
        setEnabledRecursive(newCustomerPanel, true);
        existingCustomerPanel.setEnabled(false);
        setEnabledRecursive(existingCustomerPanel, false);

        newCustomerRadio.addActionListener(e -> {
            newCustomerPanel.setEnabled(true);
            setEnabledRecursive(newCustomerPanel, true);
            existingCustomerPanel.setEnabled(false);
            setEnabledRecursive(existingCustomerPanel, false);
        });
        existingCustomerRadio.addActionListener(e -> {
            newCustomerPanel.setEnabled(false);
            setEnabledRecursive(newCustomerPanel, false);
            existingCustomerPanel.setEnabled(true);
            setEnabledRecursive(existingCustomerPanel, true);
        });

        // Add components with minimal spacing
        selectionPanel.add(newCustomerRadio);
        selectionPanel.add(Box.createVerticalStrut(5)); // Small gap
        selectionPanel.add(newCustomerPanel);
        selectionPanel.add(Box.createVerticalStrut(10)); // Slightly larger gap
        selectionPanel.add(existingCustomerRadio);
        selectionPanel.add(Box.createVerticalStrut(5)); // Small gap
        selectionPanel.add(existingCustomerPanel);

        JButton proceedButton = new JButton("Proceed to Payment");
        proceedButton.setBackground(new Color(70, 130, 180));
        proceedButton.setForeground(Color.WHITE);
        proceedButton.addActionListener(e -> {
            if (newCustomerRadio.isSelected()) {
                String firstName = firstNameField.getText().trim();
                String middleName = middleNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                if (firstName.isEmpty() && middleName.isEmpty() && lastName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "At least one name field (First, Middle, or Last) must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                try {
                    Customer customer = new Customer();
                    customer.setPersonFirstName(firstName);
                    customer.setPersonMiddleName(middleName);
                    customer.setPersonLastName(lastName);
                    customer.setPhone(phoneField.getText().trim());
                    customer.setDateOfBirth(dobField.getText().trim());
                    customer.generateCustomerNumber();
                    personService.createCustomer(customer);
                    selectedCustomer = customer;
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error creating customer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        boolean allOrdersSuccessful = true;
        StringBuilder errorMessages = new StringBuilder();

        OrderDetail order = new OrderDetail();
        order.setTotalAmount(totalAmount.doubleValue());
        order.setCustomer(selectedCustomer);
        order.setOrderDate(new java.util.Date());

        System.out.println("Creating order for customer: " + (selectedCustomer != null ? selectedCustomer.getPersonFirstName() : "null") +
                          ", Total Amount: " + totalAmount);

        for (Product product : cartItems) {
            try {
                System.out.println("Processing cart item: Name=" + product.getName() + ", ID=" + product.getId() +
                                  ", Quantity=" + product.getQuantity() + ", Size=" + product.getSize());
                Product fullProduct = productService.getProductById(product.getId());
                if (fullProduct == null || product.getId() <= 0) {
                    errorMessages.append("Invalid product: ").append(product.getName())
                        .append(" (ID: ").append(product.getId()).append(")\n");
                    System.err.println("Invalid product in cart: Name=" + product.getName() + ", ID=" + product.getId());
                    allOrdersSuccessful = false;
                    continue;
                }
                if (product.getQuantity() == null || product.getQuantity() <= 0) {
                    errorMessages.append("Invalid quantity for product: ").append(product.getName())
                        .append(" (Quantity: ").append(product.getQuantity()).append(")\n");
                    System.err.println("Invalid quantity for product: Name=" + product.getName() + ", Quantity=" + product.getQuantity());
                    allOrdersSuccessful = false;
                    continue;
                }
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(fullProduct);
                orderItem.setQuantity(product.getQuantity());
                orderItem.setUnitPrice(fullProduct.getPrice());
                orderItem.setTotalPrice(fullProduct.getPrice().multiply(new BigDecimal(product.getQuantity())));
                orderItem.setDiscount(BigDecimal.ZERO);
                orderItem.setOrder(order);
                order.getItems().add(orderItem);
                System.out.println("Added OrderItem: Product ID=" + fullProduct.getId() + ", Quantity=" + product.getQuantity());
            } catch (Exception ex) {
                errorMessages.append("Error with ").append(product.getName()).append(": ").append(ex.getMessage()).append("\n");
                System.err.println("Exception processing product: Name=" + product.getName() + ", ID=" + product.getId() +
                                  ", Error=" + ex.getMessage());
                allOrdersSuccessful = false;
            }
        }

        if (order.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No valid items to process in order!", "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("No valid items in order, aborting.");
            return;
        }

        System.out.println("Attempting to save order with " + order.getItems().size() + " items.");
        boolean success = orderService.createOrder(order);
        if (!success) {
            errorMessages.append("Failed to create order\n");
            System.err.println("Failed to create order: " + errorMessages.toString());
            allOrdersSuccessful = false;
        } else {
            System.out.println("Order created successfully.");
            // Show PaymentPanel
            JDialog paymentDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Payment", true);
            paymentDialog.setSize(600, 500);
            paymentDialog.setLayout(new BorderLayout());
            paymentDialog.setLocationRelativeTo(null);

            PaymentPanel paymentPanel = new PaymentPanel(order, selectedCustomer, paymentService, personService, cartItems);
            paymentPanel.setOnPaymentCompleteListener(() -> {
                resetAfterPayment();
                paymentDialog.dispose();
            });
            paymentDialog.add(paymentPanel, BorderLayout.CENTER);
            paymentDialog.setVisible(true);
        }

        if (!allOrdersSuccessful) {
            JOptionPane.showMessageDialog(this, 
                "Some orders failed:\n" + errorMessages.toString(), 
                "Partial Success", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void setEnabledRecursive(Container container, boolean enabled) {
        for (Component component : container.getComponents()) {
            component.setEnabled(enabled);
            if (component instanceof Container) {
                setEnabledRecursive((Container) component, enabled);
            }
        }
    }

    private void resetAfterPayment() {
        cartItems.clear();
        selectedCustomer = null;
        mainContentPanel.removeAll();
        mainContentPanel.add(centerPanel, BorderLayout.CENTER);
        loadProducts(null);
        productDetailPanel.setVisible(false);
        cartPanel.updateCartDisplay();
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
        JOptionPane.showMessageDialog(this, "Returned to Menu");
    }
}