package ui;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import dao.*;
import model.UserSession;
import service.*;

// LoginFrame class creates the login UI for the Coffee LCK application
public class LoginFrame extends JFrame {
	// Authentication service for handling login logic
	private final AuthenticationService authService;
	// Main frame for the login window
	private JFrame frame;

	// Constructor initializes the authentication service and UI
	public LoginFrame() {
		this.authService = new AuthenticationService(new EmployeeDao(), new OwnerDao(), new UserSessionDao(),
				new HashService());
		initialize();
	}

	// Initializes the UI components and layout
	private void initialize() {
		// Create and configure the main frame
		frame = new JFrame();
		frame.setTitle("Coffee LCK - Login");
		frame.setBounds(100, 100, 1000, 650); // Set window size
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null); // Center the window
		frame.setUndecorated(true); // Remove default window decorations
		frame.setShape(new RoundRectangle2D.Double(0, 0, 1000, 650, 30, 30)); // Apply rounded corners
		frame.getContentPane().setLayout(new BorderLayout());

		// === MAIN PANEL ===
		// Create main panel with a light blue gradient background
		JPanel mainPanel = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				GradientPaint gradient = new GradientPaint(0, 0, new Color(240, 245, 255), // Light blue top
						0, getHeight(), new Color(200, 220, 255) // Darker blue bottom
				);
				g2d.setPaint(gradient);
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding
		frame.getContentPane().add(mainPanel);

		// Add close button at top-right corner
		JPanel titleBarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		titleBarPanel.setBackground(new Color(240, 245, 255));
		titleBarPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 5));
		JButton closeButton = new JButton("X");
		closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
		closeButton.setForeground(Color.WHITE);
		closeButton.setBackground(new Color(255, 0, 0)); // Red background
		closeButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
		closeButton.setFocusPainted(false);
		closeButton.addActionListener(e -> System.exit(0)); // Close application
		closeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				closeButton.setBackground(new Color(200, 0, 0)); // Darker red on hover
			}

			@Override
			public void mouseExited(MouseEvent e) {
				closeButton.setBackground(new Color(255, 0, 0));
			}
		});
		titleBarPanel.add(closeButton);
		mainPanel.add(titleBarPanel, BorderLayout.NORTH);

		// === LOGIN PANEL (LEFT) ===
		// Create login panel for input fields and buttons
		JPanel loginPanel = new JPanel(new GridBagLayout());
		loginPanel.setPreferredSize(new Dimension(450, 650)); // Fixed width
		loginPanel.setBackground(Color.WHITE);
		loginPanel.setBorder(BorderFactory.createCompoundBorder(new ShadowBorder(5), // Custom shadow effect
				BorderFactory.createEmptyBorder(20, 20, 20, 20) // Inner padding
		));
		loginPanel.setOpaque(true);
		mainPanel.add(loginPanel, BorderLayout.WEST);

		// Define fonts for UI elements
		Font titleFont = new Font("Segoe UI", Font.BOLD, 28);
		Font fieldFont = new Font("Segoe UI", Font.PLAIN, 16);
		Font linkFont = new Font("Segoe UI", Font.PLAIN, 14);

		GridBagConstraints gbc;

		// **Title**
		// Add title label "Coffee LCK"
		JLabel lblLogin = new JLabel("Coffee LCK");
		lblLogin.setFont(titleFont);
		lblLogin.setForeground(new Color(30, 100, 200)); // Dark blue color
		lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(20, 10, 40, 10); // Top and bottom padding
		gbc.anchor = GridBagConstraints.CENTER;
		loginPanel.add(lblLogin, gbc);

		// **Username/Email**
		// Create panel for username/email input with icon
		JPanel emailPanel = new JPanel(new BorderLayout(10, 0));
		emailPanel.setBackground(Color.WHITE);
		emailPanel
				.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), // Gray
																														// border
						BorderFactory.createEmptyBorder(5, 10, 5, 10) // Inner padding
				));

		// Add username/email icon
		ImageIcon emailIcon = new ImageIcon("C:\\TTTN\\POS PROJECT\\img\\login.png");
		Image emailImg = emailIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		JLabel emailIconLabel = new JLabel(new ImageIcon(emailImg));
		emailPanel.add(emailIconLabel, BorderLayout.WEST);

		// Add username/email text field
		JTextField emailField = new JTextField(20);
		emailField.setFont(fieldFont);
		emailField.setBorder(null);
		emailField.setForeground(new Color(50, 50, 50)); // Dark gray text
		emailField.setToolTipText("Enter username or email"); // Add tooltip
		emailPanel.add(emailField, BorderLayout.CENTER);

		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(10, 20, 10, 20); // Padding
		gbc.fill = GridBagConstraints.HORIZONTAL;
		loginPanel.add(emailPanel, gbc);

		// **Password**
		// Create panel for password input with icon
		JPanel passwordPanel = new JPanel(new BorderLayout(10, 0));
		passwordPanel.setBackground(Color.WHITE);
		passwordPanel
				.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)),
						BorderFactory.createEmptyBorder(5, 10, 5, 10)));

		// Add password icon
		ImageIcon passIcon = new ImageIcon("C:\\TTTN\\POS PROJECT\\img\\password.png");
		Image passImg = passIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
		JLabel passIconLabel = new JLabel(new ImageIcon(passImg));
		passwordPanel.add(passIconLabel, BorderLayout.WEST);

		// Add password field
		JPasswordField passwordField = new JPasswordField(20);
		passwordField.setFont(fieldFont);
		passwordField.setBorder(null);
		passwordField.setForeground(new Color(50, 50, 50));
		passwordPanel.add(passwordField, BorderLayout.CENTER);

		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(10, 20, 10, 20);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		loginPanel.add(passwordPanel, gbc);

		// **Forgot Password**
		// Add "Forgot Password" link
		JLabel forgotPasswordLink = new JLabel("Forgot Password?");
		forgotPasswordLink.setFont(linkFont);
		forgotPasswordLink.setForeground(new Color(30, 144, 255)); // Blue link color
		forgotPasswordLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
		forgotPasswordLink.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Open reset password window
				ResetPasswordFrame resetFrame = new ResetPasswordFrame(authService);
				resetFrame.setVisible(true);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				forgotPasswordLink.setForeground(new Color(0, 100, 200)); // Darker blue on hover
			}

			@Override
			public void mouseExited(MouseEvent e) {
				forgotPasswordLink.setForeground(new Color(30, 144, 255));
			}
		});
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(10, 20, 20, 20);
		gbc.anchor = GridBagConstraints.CENTER;
		loginPanel.add(forgotPasswordLink, gbc);

		// **Login Button**
		// Create rounded login button
		JButton loginButton = new RoundedButton("Log In");
		loginButton.setBackground(new Color(30, 144, 255)); // Blue background
		loginButton.setForeground(Color.WHITE);
		loginButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
		loginButton.setPreferredSize(new Dimension(200, 50));
		loginButton.setFocusPainted(false);
		loginButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				loginButton.setBackground(new Color(0, 120, 220)); // Darker blue on hover
			}

			@Override
			public void mouseExited(MouseEvent e) {
				loginButton.setBackground(new Color(30, 144, 255));
			}
		});
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(30, 20, 10, 20); // Extra top padding
		gbc.anchor = GridBagConstraints.CENTER;
		loginPanel.add(loginButton, gbc);

		// === IMAGE PANEL (RIGHT) ===
		// Create panel for logo with green gradient
		JPanel imagePanel = new JPanel(new GridBagLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				GradientPaint gradient = new GradientPaint(0, 0, new Color(180, 255, 200), // Light green top
						0, getHeight(), new Color(100, 200, 150) // Darker green bottom
				);
				g2d.setPaint(gradient);
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		imagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		mainPanel.add(imagePanel, BorderLayout.CENTER);

		// Add logo image
		ImageIcon icon = new ImageIcon("C:\\TTTN\\POS PROJECT\\img\\lck.png");
		Image img = icon.getImage().getScaledInstance(350, 250, Image.SCALE_SMOOTH);
		JLabel imageLabel = new JLabel(new ImageIcon(img));
		imagePanel.add(imageLabel);

		// Login logic
		// Attach login action to button
		loginButton.addActionListener(e -> handleLogin(emailField, passwordField));

		// Add drag functionality for undecorated frame
		FrameDragListener dragListener = new FrameDragListener(frame);
		frame.addMouseListener(dragListener);
		frame.addMouseMotionListener(dragListener);

		frame.setVisible(true); // Show the window
	}

	// Handles login logic
	private void handleLogin(JTextField emailField, JPasswordField passwordField) {
		// Get username or email and password from fields
		String identifier = emailField.getText().trim();
		String password = new String(passwordField.getPassword());

		// Validate input
		if (identifier.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(frame, "Username/Email and password cannot be empty!", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			// Attempt login
			UserSession session = authService.login(identifier, password);
			JOptionPane.showMessageDialog(frame, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

			// Redirect based on user role
			if (session.getEmployee() != null) {
				SwingUtilities.invokeLater(() -> {
					// Open POS UI for employees
					PosUI posUI = new PosUI(session.getUsername());
					posUI.setVisible(true);
				});
			} else if (session.getOwner() != null) {
				SwingUtilities.invokeLater(() -> {
					// Open Employee Manager UI for owners
					EmployeeManager managerFrame = new EmployeeManager(
							new PersonServiceImpl(new EmployeeDao(), new CustomerDao(), new OwnerDao()),
							new SupplierServiceImpl(new SupplierDao()), new ItemServiceImpl(new ItemDao()),
							new StoreServiceImpl(new StoreDao()), new HashService(),
							new AuthenticationService(new EmployeeDao(), new OwnerDao(), new UserSessionDao(),
									new HashService()),
							new ProductServiceImpl(new ProductDao()), new CategoryServiceImpl(new CategoryDao()),
							new DashboardServiceImpl(new DashboardDao()),
							new InvoiceServiceImpl(new InvoiceDao()),
							session.getUsername());
					managerFrame.setVisible(true);
				});
			} else {
				JOptionPane.showMessageDialog(frame, "Unknown role!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			frame.dispose(); // Close login window

		} catch (Exception ex) {
			// Show error message for login failure
			JOptionPane.showMessageDialog(frame, "Login failed: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void showFrame() {
		frame.setVisible(true);
	}

	// Custom Rounded Button class
	private static class RoundedButton extends JButton {
		public RoundedButton(String text) {
			super(text);
			setContentAreaFilled(false);
			setBorderPainted(false);
		}

		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			if (getModel().isArmed()) {
				g2.setColor(getBackground().darker()); // Darker color when pressed
			} else {
				g2.setColor(getBackground());
			}
			g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25); // Draw rounded rectangle
			super.paintComponent(g2);
			g2.dispose();
		}
	}

	// Custom Shadow Border class
	private static class ShadowBorder implements Border {
		private int radius;

		public ShadowBorder(int radius) {
			this.radius = radius;
		}

		@Override
		public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(new Color(0, 0, 0, 50)); // Semi-transparent black
			g2.fillRoundRect(x + radius, y + radius, width - radius * 2, height - radius * 2, 20, 20);
			g2.dispose();
		}

		@Override
		public Insets getBorderInsets(Component c) {
			return new Insets(radius, radius, radius, radius);
		}

		@Override
		public boolean isBorderOpaque() {
			return false;
		}
	}

	// Frame Drag Listener for moving undecorated frame
	private static class FrameDragListener extends MouseAdapter {
		private final JFrame frame;
		private Point mouseDownCompCoords = null;

		public FrameDragListener(JFrame frame) {
			this.frame = frame;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			mouseDownCompCoords = null;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			mouseDownCompCoords = e.getPoint();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			// Move window based on mouse drag
			Point currCoords = e.getLocationOnScreen();
			frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
		}
	}
}