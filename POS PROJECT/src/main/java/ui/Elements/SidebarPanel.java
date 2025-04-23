package ui.Elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SidebarPanel extends JPanel {
	private final String[] iconFiles;
	private final String[] pageNames;
	private final String iconPath;
	private final SidebarListener listener;
	private final String username;

	public interface SidebarListener {
		void onSidebarItemClick(String pageName);
	}

	public SidebarPanel(String[] iconFiles, String[] pageNames, String iconPath, String username,
			SidebarListener listener) {
		this.iconFiles = iconFiles;
		this.pageNames = pageNames;
		this.iconPath = iconPath;
		this.username = username;
		this.listener = listener;

		initUI();
	}

	private void initUI() {
		setLayout(new BorderLayout());
		setBackground(Color.DARK_GRAY);

		// Logo panel
		JPanel logoPanel = new JPanel();
		logoPanel.setBackground(Color.DARK_GRAY);
		logoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		ImageIcon rawLogo = new ImageIcon(iconPath + "lck.png"); // đảm bảo file logo có sẵn
		Image scaledLogo = rawLogo.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
		JLabel logoLabel = new JLabel(new ImageIcon(scaledLogo));
		logoPanel.add(logoLabel);

		add(logoPanel, BorderLayout.NORTH); // Thêm logoPanel vào đầu sidebar

		JPanel menuPanel = new JPanel(new GridLayout(iconFiles.length, 1));
		menuPanel.setBackground(Color.DARK_GRAY);

		for (int i = 0; i < iconFiles.length; i++) {
			JLabel label = createSidebarItem(iconFiles[i], pageNames[i]);
			menuPanel.add(label);
		}

		add(menuPanel, BorderLayout.CENTER);
		add(createUserInfoPanel(), BorderLayout.SOUTH);
	}

	private JPanel createUserInfoPanel() {
		JPanel userPanel = new JPanel(new BorderLayout());
		userPanel.setBackground(Color.GRAY);
		userPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		JLabel userLabel = new JLabel("Logged in as: " + username);
		userLabel.setForeground(Color.WHITE);
		userLabel.setFont(userLabel.getFont().deriveFont(Font.ITALIC, 12f));

		userPanel.add(userLabel, BorderLayout.CENTER);
		return userPanel;
	}

	private JLabel createSidebarItem(String iconFile, String pageName) {
		ImageIcon rawIcon = new ImageIcon(iconPath + iconFile);
		Image scaledImg = rawIcon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH); // bạn chỉnh size ở đây
		ImageIcon icon = new ImageIcon(scaledImg);

		JLabel label = new JLabel(pageName, icon, JLabel.LEFT);
		label.setForeground(Color.WHITE);
		label.setIconTextGap(10);
		label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				listener.onSidebarItemClick(pageName);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				label.setForeground(Color.CYAN);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				label.setForeground(Color.WHITE);
			}
		});

		return label;
	}

}
