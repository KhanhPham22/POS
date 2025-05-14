package ui;

import javax.swing.*;
import javax.swing.border.Border;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import service.AuthenticationService;

public class ResetPasswordFrame extends JFrame {
	// Constructor receives an AuthenticationService to handle password reset logic
	public ResetPasswordFrame(AuthenticationService authService) {
		// Frame setup
		setTitle("Reset Password - Coffee LCK");
		setSize(550, 450);
		setLocationRelativeTo(null); // Center on screen
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setUndecorated(true); // Remove title bar
		setShape(new RoundRectangle2D.Double(0, 0, 550, 450, 20, 20)); // Rounded corners
		// Create the main panel with a gradient background and shadow border
		JPanel contentPanel = new JPanel(new GridBagLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				GradientPaint gradient = new GradientPaint(0, 0, new Color(180, 255, 200), // Light green
						0, getHeight(), new Color(100, 200, 150) // Darker green
				);
				g2d.setPaint(gradient);
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBorder(BorderFactory.createCompoundBorder(new ShadowBorder(5),
				BorderFactory.createEmptyBorder(20, 20, 20, 20)));
		add(contentPanel);

		// Fonts used in UI
		Font titleFont = new Font("Segoe UI", Font.BOLD, 24);
		Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
		Font fieldFont = new Font("Segoe UI", Font.PLAIN, 14);

		// ===== Title =====
		GridBagConstraints gbcTitle = new GridBagConstraints();
		gbcTitle.insets = new Insets(20, 20, 30, 20);
		gbcTitle.gridx = 0;
		gbcTitle.gridy = 0;
		gbcTitle.gridwidth = 2;
		gbcTitle.fill = GridBagConstraints.HORIZONTAL;

		JLabel titleLabel = new JLabel("Reset Your Password");
		titleLabel.setFont(titleFont);
		titleLabel.setForeground(new Color(30, 100, 200));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(titleLabel, gbcTitle);

		// ===== Username Label =====
		GridBagConstraints gbcUsernameLabel = new GridBagConstraints();
		gbcUsernameLabel.insets = new Insets(10, 20, 10, 10);
		gbcUsernameLabel.gridx = 0;
		gbcUsernameLabel.gridy = 1;
		gbcUsernameLabel.anchor = GridBagConstraints.EAST;

		JLabel usernameLabel = new JLabel("Username:");
		usernameLabel.setFont(labelFont);
		contentPanel.add(usernameLabel, gbcUsernameLabel);

		// ===== Username Field =====
		GridBagConstraints gbcUsernameField = new GridBagConstraints();
		gbcUsernameField.insets = new Insets(10, 10, 10, 20);
		gbcUsernameField.gridx = 1;
		gbcUsernameField.gridy = 1;
		gbcUsernameField.fill = GridBagConstraints.HORIZONTAL;

		JTextField usernameField = new JTextField(20);
		usernameField.setFont(fieldFont);
		usernameField
				.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)),
						BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		contentPanel.add(usernameField, gbcUsernameField);

		// ===== New Password Label =====
		GridBagConstraints gbcNewPassLabel = new GridBagConstraints();
		gbcNewPassLabel.insets = new Insets(10, 20, 10, 10);
		gbcNewPassLabel.gridx = 0;
		gbcNewPassLabel.gridy = 2;
		gbcNewPassLabel.anchor = GridBagConstraints.EAST;

		JLabel newPasswordLabel = new JLabel("New Password:");
		newPasswordLabel.setFont(labelFont);
		contentPanel.add(newPasswordLabel, gbcNewPassLabel);

		// ===== New Password Field =====
		GridBagConstraints gbcNewPassField = new GridBagConstraints();
		gbcNewPassField.insets = new Insets(10, 10, 10, 20);
		gbcNewPassField.gridx = 1;
		gbcNewPassField.gridy = 2;
		gbcNewPassField.fill = GridBagConstraints.HORIZONTAL;

		JPasswordField newPasswordField = new JPasswordField(20);
		newPasswordField.setFont(fieldFont);
		newPasswordField
				.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)),
						BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		contentPanel.add(newPasswordField, gbcNewPassField);

		// ===== Confirm Password Label =====
		GridBagConstraints gbcConfirmLabel = new GridBagConstraints();
		gbcConfirmLabel.insets = new Insets(10, 20, 10, 10);
		gbcConfirmLabel.gridx = 0;
		gbcConfirmLabel.gridy = 3;
		gbcConfirmLabel.anchor = GridBagConstraints.EAST;

		JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
		confirmPasswordLabel.setFont(labelFont);
		contentPanel.add(confirmPasswordLabel, gbcConfirmLabel);

		// ===== Confirm Password Field =====
		GridBagConstraints gbcConfirmField = new GridBagConstraints();
		gbcConfirmField.insets = new Insets(10, 10, 10, 20);
		gbcConfirmField.gridx = 1;
		gbcConfirmField.gridy = 3;
		gbcConfirmField.fill = GridBagConstraints.HORIZONTAL;

		JPasswordField confirmPasswordField = new JPasswordField(20);
		confirmPasswordField.setFont(fieldFont);
		confirmPasswordField
				.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)),
						BorderFactory.createEmptyBorder(5, 10, 5, 10)));
		contentPanel.add(confirmPasswordField, gbcConfirmField);

		// ===== Reset Button =====
		GridBagConstraints gbcButton = new GridBagConstraints();
		gbcButton.insets = new Insets(30, 20, 20, 20);
		gbcButton.gridx = 0;
		gbcButton.gridy = 4;
		gbcButton.gridwidth = 2;
		gbcButton.anchor = GridBagConstraints.CENTER;

		JButton resetButton = new RoundedButton("Reset Password");
		resetButton.setBackground(new Color(30, 144, 255));
		resetButton.setForeground(Color.WHITE);
		resetButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
		resetButton.setPreferredSize(new Dimension(200, 45));
		resetButton.setFocusPainted(false);
		resetButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				resetButton.setBackground(new Color(0, 120, 220));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				resetButton.setBackground(new Color(30, 144, 255));
			}
		});
		contentPanel.add(resetButton, gbcButton);

		// ===== Event Reset =====
		resetButton.addActionListener(e -> {
			String username = usernameField.getText().trim();
			String newPassword = new String(newPasswordField.getPassword());
			String confirmPassword = new String(confirmPasswordField.getPassword());

			// Validate input
			if (username.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (!newPassword.equals(confirmPassword)) {
				JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Attempt password reset using authService
			try {
				boolean success = authService.resetPasswordByUsername(username, newPassword);
				if (success) {
					JOptionPane.showMessageDialog(this, "Password reset successfully!", "Success",
							JOptionPane.INFORMATION_MESSAGE);
					dispose();
				} else {
					JOptionPane.showMessageDialog(this, "Failed to reset password. Username not found.", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		});

		// Add drag functionality for undecorated frame
		FrameDragListener dragListener = new FrameDragListener(this);
		addMouseListener(dragListener);
		addMouseMotionListener(dragListener);

		setVisible(true);
	}

	// Custom Rounded Button
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
				g2.setColor(getBackground().darker());
			} else {
				g2.setColor(getBackground());
			}
			g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
			super.paintComponent(g2);
			g2.dispose();
		}
	}

	// Custom Shadow Border
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

	// Frame Drag Listener for Undecorated Frame
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
			Point currCoords = e.getLocationOnScreen();
			frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
		}
	}
}