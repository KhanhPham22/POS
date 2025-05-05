package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import model.Category;
import model.Product;
import service.CategoryService;
import service.ProductService;
import service.ProductServiceImpl;
import dao.ProductDao;

/**
 * A reusable panel that displays product details
 * Can be embedded in other containers like JFrame, JDialog or other panels
 */
public class ProductDetailPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    
    // UI Components
    private JLabel productImageLabel;
    private JLabel productNameLabel;
    private JLabel productCodeLabel;
    private JLabel categoryLabel;
    private JLabel statusLabel;
    private JPanel sizeSelectionPanel;
    private JRadioButton sizeSmall;
    private JRadioButton sizeMedium;
    private JRadioButton sizeLarge;
    private ButtonGroup sizeGroup;
    private JLabel priceLabel;
    private JLabel discountLabel;
    private JLabel totalPriceLabel;
    private JButton backButton;
    private JButton addToCartButton;
    private JSpinner quantitySpinner;
    
    // Services
    private ProductService productService;
    private CategoryService categoryService;
    
    // Current product
    private Product currentProduct;
    
    // Vietnamese currency formatter
    private NumberFormat currencyFormatter;
    
    // Callback interfaces for actions
    private ProductActionListener productActionListener;
    
    // Reference to parent container for card layout navigation
    private JPanel parentPanel;
    private CardLayout cardLayout;

    /**
     * Interface for handling product actions
     */
    public interface ProductActionListener {
        void onAddToCart(Product product, String size, int quantity);
        void onBack();
        void onCategoryView(long categoryId);
    }
    
    /**
     * Constructor with ProductService dependency
     * @param productService The service to fetch product data
     */
    public ProductDetailPanel(ProductService productService) {
        this(productService, null, null, null);
    }
    
    /**
     * Constructor with all dependencies for integration with CategoryPanel
     * @param productService The service to fetch product data
     * @param categoryService The service to fetch category data
     * @param parentPanel The parent panel for card layout navigation
     * @param cardLayout The card layout for navigation
     */
    public ProductDetailPanel(ProductService productService, CategoryService categoryService, 
                             JPanel parentPanel, CardLayout cardLayout) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.parentPanel = parentPanel;
        this.cardLayout = cardLayout;
        
        // Initialize currency formatter for VND
        currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        currencyFormatter.setCurrency(java.util.Currency.getInstance("VND"));
        
        // Setup UI
        initializeUI();
    }
    
    /**
     * Set a listener for product actions
     * @param listener The listener to handle actions
     */
    public void setProductActionListener(ProductActionListener listener) {
        this.productActionListener = listener;
    }
    
    /**
     * Load and display a product by ID
     * @param productId The ID of the product to display
     * @return true if product loaded successfully, false otherwise
     */
    public boolean loadProduct(long productId) {
        try {
            currentProduct = productService.getProductById(productId);
            if (currentProduct != null) {
                updateUI();
                return true;
            }
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading product: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Update UI with current product data
     */
//    private void updateUI() {
//        if (currentProduct == null) return;
//        
//        // Update product name
//        productNameLabel.setText(currentProduct.getName());
//        
//        // Update product code (ID)
//        productCodeLabel.setText("Mã sản phẩm: " + currentProduct.getId());
//        
//        // Update category with clickable link if categoryService is available
//        if (currentProduct.getCategory() != null) {
//            if (categoryService != null) {
//                // Create a clickable category label that navigates to category view
//                String categoryName = currentProduct.getCategory().getName();
//                categoryLabel.setText("<html><u>" + categoryName + "</u></html>");
//                categoryLabel.setForeground(new Color(0, 0, 255)); // Blue for hyperlinks
//                categoryLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
//                
//                // Add mouse listener for category click
//                if (categoryLabel.getMouseListeners().length == 0) {
//                    categoryLabel.addMouseListener(new java.awt.event.MouseAdapter() {
//                        @Override
//                        public void mouseClicked(java.awt.event.MouseEvent evt) {
//                            if (productActionListener != null) {
//                                productActionListener.onCategoryView(currentProduct.getCategory().getId());
//                            }
//                        }
//                    });
//                }
//            } else {
//                // Regular non-clickable label if no categoryService
//                categoryLabel.setText("Danh mục: " + currentProduct.getCategory().getName());
//            }
//        } else {
//            categoryLabel.setText("Danh mục: N/A");
//        }
//        
//        // Update status and quantity
//        String statusText = currentProduct.getStatus() == 1 ? "Còn hàng" : "Hết hàng";
//        statusLabel.setText("Tình trạng: " + statusText + 
//            (currentProduct.getQuantity() != null ? " (" + currentProduct.getQuantity() + ")" : ""));
//        
//        // Update size selection - select based on product's current size if available
//        String productSize = currentProduct.getSize();
//        if (productSize != null) {
//            if (productSize.equalsIgnoreCase("S")) {
//                sizeSmall.setSelected(true);
//            } else if (productSize.equalsIgnoreCase("L")) {
//                sizeLarge.setSelected(true);
//            } else {
//                sizeMedium.setSelected(true);
//            }
//        }
//        
//        // Update price information
//        BigDecimal price = currentProduct.getPrice();
//        BigDecimal discount = currentProduct.getDiscount();
//        BigDecimal finalPrice = price;
//        
//        if (price != null) {
//            priceLabel.setText("Giá: " + currencyFormatter.format(price));
//            
//            if (discount != null && discount.compareTo(BigDecimal.ZERO) > 0) {
//                // Calculate percentage
//                BigDecimal percentage = discount.multiply(new BigDecimal(100)).divide(price, 0, BigDecimal.ROUND_HALF_UP);
//                discountLabel.setText("Giảm giá: " + currencyFormatter.format(discount) + 
//                    " (" + percentage + "%)");
//                discountLabel.setVisible(true);
//                
//                // Calculate final price
//                finalPrice = price.subtract(discount);
//            } else {
//                discountLabel.setVisible(false);
//            }
//            
//            totalPriceLabel.setText("Thành tiền: " + currencyFormatter.format(finalPrice));
//        }
//        
//        // Update image if available
//        if (currentProduct.getImagePath() != null && !currentProduct.getImagePath().isEmpty()) {
//            try {
//                ImageIcon icon = new ImageIcon(currentProduct.getImagePath());
//                Image image = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
//                productImageLabel.setIcon(new ImageIcon(image));
//                productImageLabel.setText(""); // Clear the placeholder text
//            } catch (Exception e) {
//                productImageLabel.setIcon(null);
//                productImageLabel.setText("Hình ảnh không có sẵn");
//            }
//        } else {
//            productImageLabel.setIcon(null);
//            productImageLabel.setText("Hình ảnh không có sẵn");
//        }
//        
//        // Set maximum quantity in spinner
//        SpinnerNumberModel model = (SpinnerNumberModel) quantitySpinner.getModel();
//        if (currentProduct.getQuantity() != null) {
//            model.setMaximum(currentProduct.getQuantity());
//        }
//        
//        // Enable/disable add to cart button based on status and quantity
//        boolean canAddToCart = currentProduct.getStatus() == 1 && 
//                              (currentProduct.getQuantity() == null || currentProduct.getQuantity() > 0);
//        addToCartButton.setEnabled(canAddToCart);
//    }
//    
    /**
     * Initialize the UI components
     */
    private void initializeUI() {
        // Set layout
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        
        // Create main panel with product content
        JPanel mainPanel = new JPanel(new BorderLayout(20, 0));
        mainPanel.setBackground(Color.WHITE);
        
        // Left panel for product image
        JPanel imagePanel = createImagePanel();
        mainPanel.add(imagePanel, BorderLayout.WEST);
        
        // Right panel for product details
        JPanel detailsPanel = createDetailsPanel();
        mainPanel.add(detailsPanel, BorderLayout.CENTER);
        
        // Add main panel to the center
        add(mainPanel, BorderLayout.CENTER);
        
        // Bottom panel for buttons
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Create the panel for displaying product image
     */
    private JPanel createImagePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        panel.setPreferredSize(new Dimension(300, 300));
        
        productImageLabel = new JLabel("Hình ảnh sản phẩm");
        productImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        productImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        productImageLabel.setForeground(Color.LIGHT_GRAY);
        
        panel.add(productImageLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Create the panel for displaying product details
     */
    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // Product name
        productNameLabel = new JLabel("");
        productNameLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        productNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(productNameLabel);
        panel.add(Box.createVerticalStrut(15));
        
        // Product code
        productCodeLabel = new JLabel("");
        productCodeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(productCodeLabel);
        panel.add(Box.createVerticalStrut(10));
        
        // Category
        categoryLabel = new JLabel("");
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(categoryLabel);
        panel.add(Box.createVerticalStrut(10));
        
        // Status
        statusLabel = new JLabel("");
        statusLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(statusLabel);
        panel.add(Box.createVerticalStrut(20));
        
        // Size selection
        JLabel sizeLabel = new JLabel("Chọn kích cỡ:");
        sizeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(sizeLabel);
        panel.add(Box.createVerticalStrut(10));
        
        sizeSelectionPanel = new JPanel();
        sizeSelectionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        sizeSelectionPanel.setBackground(Color.WHITE);
        sizeSelectionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        sizeGroup = new ButtonGroup();
        
        sizeSmall = createSizeButton("S");
        sizeMedium = createSizeButton("M");
        sizeLarge = createSizeButton("L");
        
        // Select medium by default
        sizeMedium.setSelected(true);
        
        sizeSelectionPanel.add(sizeSmall);
        sizeSelectionPanel.add(sizeMedium);
        sizeSelectionPanel.add(sizeLarge);
        
        panel.add(sizeSelectionPanel);
        panel.add(Box.createVerticalStrut(20));
        
        // Price information
        JPanel pricePanel = new JPanel();
        pricePanel.setLayout(new GridLayout(3, 1, 0, 10));
        pricePanel.setBackground(Color.WHITE);
        pricePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        pricePanel.setMaximumSize(new Dimension(400, 100));
        
        priceLabel = new JLabel("");
        discountLabel = new JLabel("");
        discountLabel.setForeground(new Color(231, 76, 60));
        totalPriceLabel = new JLabel("");
        totalPriceLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalPriceLabel.setForeground(new Color(63, 81, 181));
        
        pricePanel.add(priceLabel);
        pricePanel.add(discountLabel);
        pricePanel.add(totalPriceLabel);
        
        panel.add(pricePanel);
        
        return panel;
    }
    
    /**
     * Create the panel with action buttons
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        // Quantity panel
        JPanel quantityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        quantityPanel.setBackground(Color.WHITE);
        
        JLabel quantityLabel = new JLabel("Số lượng:");
        quantityPanel.add(quantityLabel);
        
        // Quantity spinner
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 999, 1);
        quantitySpinner = new JSpinner(spinnerModel);
        JComponent editor = quantitySpinner.getEditor();
        JFormattedTextField textField = ((JSpinner.DefaultEditor) editor).getTextField();
        textField.setColumns(2);
        
        // Custom buttons for spinner
        JButton decrementButton = new JButton("-");
        decrementButton.setPreferredSize(new Dimension(40, 25));
        decrementButton.addActionListener(e -> {
            int value = (int) quantitySpinner.getValue();
            if (value > 1) {
                quantitySpinner.setValue(value - 1);
            }
        });
        
        JButton incrementButton = new JButton("+");
        incrementButton.setPreferredSize(new Dimension(40, 25));
        incrementButton.addActionListener(e -> {
            int value = (int) quantitySpinner.getValue();
            int max = ((BigDecimal) ((SpinnerNumberModel) quantitySpinner.getModel()).getMaximum()).intValue();
            if (value < max) {
                quantitySpinner.setValue(value + 1);
            }
        });
        
        quantityPanel.add(decrementButton);
        quantityPanel.add(quantitySpinner);
        quantityPanel.add(incrementButton);
        
        panel.add(quantityPanel, BorderLayout.WEST);
        
        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(Color.WHITE);
        
        backButton = new JButton("Quay lại");
        backButton.setPreferredSize(new Dimension(120, 40));
        backButton.setBackground(new Color(231, 76, 60));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            if (parentPanel != null && cardLayout != null) {
                // If we're using card layout, show the previous panel
                cardLayout.show(parentPanel, "categoryPanel");
            } else if (productActionListener != null) {
                // Otherwise use the listener callback
                productActionListener.onBack();
            }
        });
        
        addToCartButton = new JButton("Thêm vào giỏ");
        addToCartButton.setPreferredSize(new Dimension(150, 40));
        addToCartButton.setBackground(new Color(46, 204, 113));
        addToCartButton.setForeground(Color.WHITE);
        addToCartButton.setFocusPainted(false);
        addToCartButton.addActionListener(e -> {
            if (currentProduct != null && productActionListener != null) {
                String selectedSize;
                
                if (sizeSmall.isSelected()) {
                    selectedSize = "S";
                } else if (sizeLarge.isSelected()) {
                    selectedSize = "L";
                } else {
                    selectedSize = "M";
                }
                
                int quantity = (int) quantitySpinner.getValue();
                
                productActionListener.onAddToCart(currentProduct, selectedSize, quantity);
            }
        });
        
        buttonsPanel.add(backButton);
        buttonsPanel.add(addToCartButton);
        
        panel.add(buttonsPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Create a radio button for size selection
     * @param size The size label (S, M, L)
     * @return The configured radio button
     */
    private JRadioButton createSizeButton(String size) {
        JRadioButton button = new JRadioButton(size);
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setPreferredSize(new Dimension(50, 40));
        sizeGroup.add(button);
        
        return button;
    }
    
    /**
     * Load products by category
     * @param categoryId The category ID to load products for
     * @return List of products in the category
     */
    public List loadProductsByCategory(long categoryId) {
        try {
            // This would require a method in ProductService to get products by category
            // You'd need to add this to your ProductService interface and implementation
            // For now, we'll just return null as a placeholder
            return null; // Replace with actual implementation
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading products for category: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    /**
     * Example of how to use this panel with CategoryPanel in a card layout
     */
    public static void main(String[] args) {
        // Just a demo to show how to use this panel with CategoryPanel
        JFrame frame = new JFrame("Product System Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create DAOs and Services
        ProductDao productDao = new ProductDao();
        ProductService productService = new ProductServiceImpl(productDao);
        // You would need to create CategoryDao and CategoryService similar to ProductDao/Service
        CategoryService categoryService = null; // Replace with actual implementation
        
        // Set up card layout for navigation
        JPanel mainPanel = new JPanel();
        CardLayout cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        
        // Create ProductDetailPanel
        ProductDetailPanel productPanel = new ProductDetailPanel(
            productService, categoryService, mainPanel, cardLayout);
        
        // Create CategoryPanel with listener to navigate to product details
        CategoryPanel categoryPanel = new CategoryPanel(categoryService, 
            new CategoryPanel.OnCategoryClickListener() {
                @Override
                public void onCategorySelected(Category category) {
                    // Typically you would show a list of products in the category
                    // But for demo purposes, let's just show product detail panel
                    productPanel.loadProduct(1); // Load first product (placeholder)
                    cardLayout.show(mainPanel, "productPanel");
                }
            }, false);
        
        // Add panels to card layout
        mainPanel.add(categoryPanel, "categoryPanel");
        mainPanel.add(productPanel, "productPanel");
        
        // Start with category panel
        cardLayout.show(mainPanel, "categoryPanel");
        
        // Set up the frame
        frame.getContentPane().add(mainPanel);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    /**
     * Simpler example showing how to use just the ProductDetailPanel
     */
    public static void singlePanelDemo() {
        JFrame frame = new JFrame("Product Detail Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create DAO and Service
        ProductDao productDao = new ProductDao();
        ProductService productService = new ProductServiceImpl(productDao);
        
        // Create panel
        ProductDetailPanel panel = new ProductDetailPanel(productService);
        
        // Add action listener
        panel.setProductActionListener(new ProductDetailPanel.ProductActionListener() {
            @Override
            public void onAddToCart(Product product, String size, int quantity) {
                JOptionPane.showMessageDialog(frame, 
                    "Added " + quantity + " " + product.getName() + " (Size: " + size + ") to cart", 
                    "Cart Updated", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
            @Override
            public void onBack() {
                JOptionPane.showMessageDialog(frame, 
                    "Back button clicked", 
                    "Navigation", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
            @Override
            public void onCategoryView(long categoryId) {
                JOptionPane.showMessageDialog(frame,
                    "Viewing category with ID: " + categoryId,
                    "Category View",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        // Load product with ID 1 (just for demo)
        boolean loaded = panel.loadProduct(1);
        if (!loaded) {
            JOptionPane.showMessageDialog(frame, 
                "Failed to load product. Make sure database is connected.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        frame.getContentPane().add(panel);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}