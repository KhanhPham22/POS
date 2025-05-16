package ui.Elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SidebarPanel extends JPanel {
	private final String[] iconFiles;     // Array of icon file names
	private final String[] pageNames;     // Array of page names corresponding to icons
	private final String iconPath;        // Path to icon directory
	private final SidebarListener listener; // Listener for sidebar item clicks
	private final String username;        // Logged-in username to display

	// Listener interface to handle sidebar item click events
	public interface SidebarListener {
		void onSidebarItemClick(String pageName);
	}

	// Constructor
	public SidebarPanel(String[] iconFiles, String[] pageNames, String iconPath, String username,
			SidebarListener listener) {
		this.iconFiles = iconFiles;
		this.pageNames = pageNames;
		this.iconPath = iconPath;
		this.username = username;
		this.listener = listener;

		initUI(); // Initialize UI components
	}

	// Initialize the sidebar UI
	private void initUI() {
		setLayout(new BorderLayout());        // Set layout for the panel
		setBackground(Color.DARK_GRAY);       // Set background color

		// ----- Top Logo Panel -----
		JPanel logoPanel = new JPanel();
		logoPanel.setBackground(Color.DARK_GRAY);
		logoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Load and scale the logo image
		ImageIcon rawLogo = new ImageIcon(iconPath + "lck.png"); // Make sure logo file exists
		Image scaledLogo = rawLogo.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
		JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
		logoPanel.add(logoLabel);

		add(logoPanel, BorderLayout.NORTH); // Add logo panel to the top of the sidebar

		// ----- Center Menu Items -----
		JPanel menuPanel = new JPanel(new GridLayout(iconFiles.length, 1)); // One row per item
		menuPanel.setBackground(Color.DARK_GRAY);

		// Create and add each sidebar item
		for (int i = 0; i < iconFiles.length; i++) {
			JLabel label = createSidebarItem(iconFiles[i], pageNames[i]);
			menuPanel.add(label);
		}

		add(menuPanel, BorderLayout.CENTER); // Add menu panel to the center
		add(createUserInfoPanel(), BorderLayout.SOUTH); // Add user info at the bottom
	}

	// Create the user info panel displayed at the bottom
	private JPanel createUserInfoPanel() {
		JPanel userPanel = new JPanel(new BorderLayout());
		userPanel.setBackground(Color.GRAY);
		userPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Display logged-in user
		JLabel userLabel = new JLabel("Logged in as: " + username);
		userLabel.setForeground(Color.WHITE);
		userLabel.setFont(userLabel.getFont().deriveFont(Font.ITALIC, 12f));

		userPanel.add(userLabel, BorderLayout.CENTER);
		return userPanel;
	}

	// Create individual sidebar menu item
	private JLabel createSidebarItem(String iconFile, String pageName) {
		// Load and scale icon
		ImageIcon rawIcon = new ImageIcon(iconPath + iconFile);
		Image scaledImg = rawIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH); // Adjust icon size here
		ImageIcon icon = new ImageIcon(scaledImg);

		// Create label with icon and text
		JLabel label = new JLabel(pageName, icon, JLabel.LEFT);
		label.setForeground(Color.WHITE);
		label.setIconTextGap(10); // Space between icon and text
		label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Mouse interaction for hover effect and click action
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				listener.onSidebarItemClick(pageName); // Trigger callback when clicked
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				label.setForeground(Color.CYAN); // Highlight on hover
			}

			@Override
			public void mouseExited(MouseEvent e) {
				label.setForeground(Color.WHITE); // Restore color when not hovered
			}
		});

		return label;
	}
}
