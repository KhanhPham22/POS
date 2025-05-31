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
import model.Employee;
import model.OrderDetail;
import model.OrderItem;
import service.CategoryService;
import service.GiftVoucherService;
import service.ProductService;
import service.PersonService;
import service.PaymentService;
import service.OrderService;
import service.InvoiceService;
import java.io.File;
import ui.Elements.SearchBar;

/**
 * MenuPanel displays the product menu, allowing users to browse products, add
 * items to a cart, and proceed to payment.
 */
public class MenuePanel extends JPanel {
	private final CategoryService categoryService;
	private final ProductService productService;
	private final PersonService personService;
	private final PaymentService paymentService;
	private final OrderService orderService;
	private final GiftVoucherService giftVoucherService;
	private final InvoiceService invoiceService;
	private final Employee loggedInEmployee; // New field
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

	/**
	 * Constructor initializes the panel with required services and UI.
	 */
	public MenuePanel(CategoryService categoryService, ProductService productService, PersonService personService,
			PaymentService paymentService, OrderService orderService, InvoiceService invoiceService,GiftVoucherService giftVoucherService,
			Employee loggedInEmployee) {
		this.categoryService = categoryService;
		this.productService = productService;
		this.personService = personService;
		this.paymentService = paymentService;
		this.orderService = orderService;
		this.invoiceService = invoiceService;
		this.giftVoucherService = giftVoucherService;
		this.cartItems = new ArrayList<>();
		this.selectedCustomer = null;
		this.loggedInEmployee = loggedInEmployee; // Properly initialized

		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		initializeUI();

		cartPanel = new CartPanel(cartItems);
		
	}

	/**
	 * Initializes the UI components.
	 */
	private void initializeUI() {
		JPanel topPanel = new JPanel(new BorderLayout(10, 0));
		topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

		searchBar = new SearchBar(query -> filterProductsBySearch(query));
		searchBar.setPlaceholder("Search...");
		topPanel.add(searchBar, BorderLayout.CENTER);

		JButton cartButton = new JButton("Cart");
		cartButton.setBackground(new Color(70, 130, 180));
		cartButton.setForeground(Color.WHITE);
		try {
			ImageIcon cartIcon = new ImageIcon(new ImageIcon("C:\\TTTN\\POS PROJECT\\img\\cart_icon.png").getImage()
					.getScaledInstance(24, 24, Image.SCALE_SMOOTH));
			cartButton.setIcon(cartIcon);
			cartButton.setHorizontalTextPosition(SwingConstants.RIGHT);
			cartButton.setIconTextGap(5);
		} catch (Exception e) {
			System.err.println("Failed to load cart icon: " + e.getMessage());
		}
		cartButton.addActionListener(e -> showCart());
		topPanel.add(cartButton, BorderLayout.EAST);
		add(topPanel, BorderLayout.NORTH);

		mainContentPanel = new JPanel(new BorderLayout());
		add(mainContentPanel, BorderLayout.CENTER);

		centerPanel = new JPanel(new BorderLayout());
		categoryPanel = new CategoryPanel(categoryService, category -> filterProductsByCategory(category), false);
		centerPanel.add(categoryPanel, BorderLayout.NORTH);

		productDisplayPanel = new JPanel(new GridLayout(0, 4, 10, 10));
		productDisplayPanel.setBackground(Color.WHITE);
		productDisplayPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		centerPanel.add(new JScrollPane(productDisplayPanel), BorderLayout.CENTER);

		mainContentPanel.add(centerPanel, BorderLayout.CENTER);

		productDetailPanel = new JPanel(new BorderLayout());
		productDetailPanel.setVisible(false);
		add(productDetailPanel, BorderLayout.EAST);

		loadProducts(null);
	}

	/**
	 * Filters products by category.
	 */
	private void filterProductsByCategory(Category category) {
		loadProducts(category);
	}

	/**
	 * Filters products by search query.
	 */
	private void filterProductsBySearch(String query) {
		List<Product> products = productService.getAllProducts(1, Integer.MAX_VALUE);
		if (query != null && !query.isEmpty()) {
			products = products.stream()
					.filter(p -> p.getName() != null && p.getName().toLowerCase().contains(query.toLowerCase()))
					.collect(Collectors.toList());
		}
		loadProductsWithList(products);
	}

	/**
	 * Loads products, optionally filtered by category.
	 */
	private void loadProducts(Category category) {
		List<Product> products = productService.getAllProducts(1, Integer.MAX_VALUE);
		if (category != null) {
			products = products.stream()
					.filter(p -> p.getCategory() != null && p.getCategory().getId() == category.getId())
					.collect(Collectors.toList());
		}
		products = products.stream()
				.collect(Collectors.toMap(Product::getName, p -> p, (existing, replacement) -> existing)).values()
				.stream().collect(Collectors.toList());
		loadProductsWithList(products);
	}

	/**
	 * Displays a list of products.
	 */
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

	/**
	 * Creates a panel for a product.
	 */
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

	/**
	 * Loads a product image with fallbacks.
	 */
	private void loadProductImage(Product product, JLabel label, int width, int height) {
		ImageIcon icon = null;
		if (product.getImagePath() != null && !product.getImagePath().isEmpty()) {
			File imageFile = new File(product.getImagePath());
			if (imageFile.exists()) {
				icon = new ImageIcon(product.getImagePath());
			} else {
				String fallbackPath = PRODUCT_IMG_DIR + product.getName() + "_"
						+ (product.getSize() != null ? product.getSize() : "Default") + ".jpg";
				File fallbackFile = new File(fallbackPath);
				if (fallbackFile.exists()) {
					icon = new ImageIcon(fallbackPath);
				}
			}
		} else {
			String fallbackPath = PRODUCT_IMG_DIR + product.getName() + "_"
					+ (product.getSize() != null ? product.getSize() : "Default") + ".jpg";
			File fallbackFile = new File(fallbackPath);
			if (fallbackFile.exists()) {
				icon = new ImageIcon(fallbackPath);
			} else {
				File defaultFile = new File(PRODUCT_IMG_DIR + "default.png");
				String defaultPath = defaultFile.exists() ? PRODUCT_IMG_DIR + "default.png"
						: "C:\\TTTN\\POS PROJECT\\img\\default.png";
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

	/**
	 * Shows product details.
	 */
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
		detailContent.add(
				new JLabel("Category: " + (product.getCategory() != null ? product.getCategory().getName() : "N/A")));
		detailContent.add(new JLabel("Quantity in Stock: " + product.getQuantity()));

		JLabel sizeLabel = new JLabel("Size:");
		boolean isDesserts = product.getCategory() != null
				&& product.getCategory().getName().equalsIgnoreCase("Desserts");
		String[] sizeOptions = isDesserts ? new String[] { "Default" } : new String[] { "S", "M", "L" };
		sizeComboBox = new JComboBox<>(sizeOptions);
		sizeComboBox.setEnabled(!isDesserts);
		List<Product> sizeVariants = productService.getAllProducts(1, Integer.MAX_VALUE).stream()
				.filter(p -> p.getName() != null && p.getName().equals(product.getName())).collect(Collectors.toList());
		String defaultSize = sizeVariants.stream().filter(p -> p.getSize() != null).findFirst().map(p -> p.getSize())
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

		totalPriceLabel = new JLabel(
				"Total: " + formatPrice(product.getPrice().multiply(new BigDecimal(quantityField.getText()))));
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

	/**
	 * Adjusts the quantity field.
	 */
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

	/**
	 * Updates product details based on size and quantity.
	 */
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

	/**
	 * Formats a price for display.
	 */
	private String formatPrice(BigDecimal price) {
		return PRICE_FORMAT.format(price);
	}

	/**
	 * Adds a product to the cart.
	 */
	private void addToCart(Product product) {
		try {
			int quantity = Integer.parseInt(quantityField.getText());
			String selectedSize = (String) sizeComboBox.getSelectedItem();
			Product cartProduct = productService.getProductByNameAndSize(product.getName(), selectedSize);

			if (cartProduct == null) {
				JOptionPane.showMessageDialog(this, "Product not found for selected size!", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (cartProduct.getId() <= 0) {
				JOptionPane.showMessageDialog(this, "Invalid product ID for " + cartProduct.getName() + "!", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

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

	/**
	 * Shows the cart in a new frame.
	 */
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

	/**
	 * Shows a dialog for selecting or adding a customer with address and
	 * description fields.
	 */
	private void showCustomerSelection(BigDecimal totalAmount) {
		JDialog customerDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Customer Information",
				true);
		customerDialog.setSize(400, 600);
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
		JTextField addressField = new JTextField();
		JTextField descriptionField = new JTextField();
		JComboBox<String> genderField = new JComboBox<>(new String[] { "Male", "Female", "Other" }); // Added gender
																										// combo box
		genderField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		genderField.setBackground(Color.WHITE);
		genderField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));

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
		newCustomerPanel.add(new JLabel("Gender:"));
		newCustomerPanel.add(genderField); // Add gender field to panel
		newCustomerPanel.add(new JLabel("Address:"));
		newCustomerPanel.add(addressField);
		newCustomerPanel.add(new JLabel("Description:"));
		newCustomerPanel.add(descriptionField);

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
			JOptionPane.showMessageDialog(this, "Error loading customers: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
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
				JOptionPane.showMessageDialog(this, "Error searching customers: " + e.getMessage(), "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		customerSearchBar.setPlaceholder("Search by name...");
		existingCustomerPanel.add(customerSearchBar, BorderLayout.NORTH);
		existingCustomerPanel.add(new JScrollPane(customerList), BorderLayout.CENTER);

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

		selectionPanel.add(newCustomerRadio);
		selectionPanel.add(Box.createVerticalStrut(5));
		selectionPanel.add(newCustomerPanel);
		selectionPanel.add(Box.createVerticalStrut(10));
		selectionPanel.add(existingCustomerRadio);
		selectionPanel.add(Box.createVerticalStrut(5));
		selectionPanel.add(existingCustomerPanel);

		JButton proceedButton = new JButton("Proceed to Payment");
		proceedButton.setBackground(new Color(70, 130, 180));
		proceedButton.setForeground(Color.WHITE);
		proceedButton.addActionListener(e -> {
			if (newCustomerRadio.isSelected()) {
				String firstName = firstNameField.getText().trim();
				String middleName = middleNameField.getText().trim();
				String lastName = lastNameField.getText().trim();
				String phone = phoneField.getText().trim();
				String dob = dobField.getText().trim();
				String address = addressField.getText().trim();
				String description = descriptionField.getText().trim();
				// Validate inputs
				if (firstName.isEmpty() && middleName.isEmpty() && lastName.isEmpty()) {
					JOptionPane.showMessageDialog(this, "At least one name field must be filled!", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (phone.isEmpty()) {
					JOptionPane.showMessageDialog(this, "Phone number is required!", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (!address.isEmpty() && address.length() > 255) {
					JOptionPane.showMessageDialog(this, "Address must be 255 characters or less!", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					// Check for duplicate phone number
					List<Customer> customers = personService.getAllCustomers(1, Integer.MAX_VALUE);
					boolean phoneExists = customers.stream()
							.anyMatch(c -> c.getPhone() != null && c.getPhone().equals(phone));
					if (phoneExists) {
						JOptionPane.showMessageDialog(this,
								"Phone number already exists! Please enter a different number.", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					Customer customer = new Customer();
					customer.setPersonFirstName(firstName);
					customer.setPersonMiddleName(middleName);
					customer.setPersonLastName(lastName);
					customer.setPhone(phone);
					customer.setDateOfBirth(dob);
					// Safely set address and description if supported
					try {
						customer.setAddress(address);
						customer.setDescription(description);
					} catch (NoSuchMethodError ex) {
						System.err.println("Address/Description not supported in Customer class: " + ex.getMessage());
					}
					customer.generateCustomerNumber();
					personService.createCustomer(customer);
					selectedCustomer = customer;
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, "Error creating customer: " + ex.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			} else {
				try {
					String selectedName = customerList.getSelectedValue();
					if (selectedName == null) {
						JOptionPane.showMessageDialog(this, "Please select a customer!", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					List<Customer> customers = personService.getAllCustomers(1, Integer.MAX_VALUE);
					selectedCustomer = customers.stream().filter(c -> getCustomerFullName(c).equals(selectedName))
							.findFirst().orElse(null);
					if (selectedCustomer == null) {
						JOptionPane.showMessageDialog(this, "Customer not found!", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(this, "Error selecting customer: " + ex.getMessage(), "Error",
							JOptionPane.ERROR_MESSAGE);
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

	/**
	 * Gets the customer's full name.
	 */
	private String getCustomerFullName(Customer customer) {
		return (customer.getPersonFirstName() != null ? customer.getPersonFirstName() : "") + " "
				+ (customer.getPersonMiddleName() != null ? customer.getPersonMiddleName() + " " : "")
				+ (customer.getPersonLastName() != null ? customer.getPersonLastName() : "");
	}

	/**
	 * Processes the payment.
	 */
	private void proceedToPayment(BigDecimal totalAmount) {
		if (cartItems.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Cart is empty!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (loggedInEmployee == null) {
			JOptionPane.showMessageDialog(this, "No logged-in employee found!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		boolean allOrdersSuccessful = true;
		StringBuilder errorMessages = new StringBuilder();

		OrderDetail order = new OrderDetail();
		order.setTotalAmount(totalAmount.doubleValue());
		order.setCustomer(selectedCustomer);
		order.setOrderDate(new java.util.Date());

		for (Product product : cartItems) {
			try {
				Product fullProduct = productService.getProductById(product.getId());
				if (fullProduct == null || product.getId() <= 0) {
					errorMessages.append("Invalid product: ").append(product.getName()).append("\n");
					allOrdersSuccessful = false;
					continue;
				}
				if (product.getQuantity() == null || product.getQuantity() <= 0) {
					errorMessages.append("Invalid quantity for product: ").append(product.getName()).append("\n");
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
			} catch (Exception ex) {
				errorMessages.append("Error with ").append(product.getName()).append(": ").append(ex.getMessage())
						.append("\n");
				allOrdersSuccessful = false;
			}
		}

		if (order.getItems().isEmpty()) {
			JOptionPane.showMessageDialog(this, "No valid items to process in order!", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		boolean success = orderService.createOrder(order);
		if (!success) {
			errorMessages.append("Failed to create order\n");
			allOrdersSuccessful = false;
		} else {
			JDialog paymentDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Payment", true);
			paymentDialog.setSize(600, 500);
			paymentDialog.setLayout(new BorderLayout());
			paymentDialog.setLocationRelativeTo(null);

			PaymentPanel paymentPanel = new PaymentPanel(order, selectedCustomer, paymentService, personService,
					invoiceService,giftVoucherService, cartItems, loggedInEmployee);
			paymentPanel.setOnPaymentCompleteListener(() -> {
				resetAfterPayment();
				paymentDialog.dispose();
			});
			paymentDialog.add(paymentPanel, BorderLayout.CENTER);
			paymentDialog.setVisible(true);
		}

		if (!allOrdersSuccessful) {
			JOptionPane.showMessageDialog(this, "Some orders failed:\n" + errorMessages.toString(), "Partial Success",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	/**
	 * Enables or disables container components recursively.
	 */
	private void setEnabledRecursive(Container container, boolean enabled) {
		for (Component component : container.getComponents()) {
			component.setEnabled(enabled);
			if (component instanceof Container) {
				setEnabledRecursive((Container) component, enabled);
			}
		}
	}

	/**
	 * Resets the panel after payment.
	 */
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
		
	}
}