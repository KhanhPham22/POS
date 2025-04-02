package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminUI extends JFrame {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminUI window = new AdminUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AdminUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1200, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(new Color(240, 242, 245)); 
        frame.setLayout(new BorderLayout());
        
     // Thanh bên trái (Sidebar)
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setBackground(Color.WHITE);
        sidebarPanel.setPreferredSize(new Dimension(80, 0));
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
     // Logo (Thay bằng icon)
        ImageIcon logoIcon = new ImageIcon("C:\\\\TTTN\\\\POS PROJECT\\\\img\\\\lck.png"); // Đường dẫn đến file icon logo
        Image logoImage = logoIcon.getImage().getScaledInstance(70, 50, Image.SCALE_SMOOTH); // Resize icon
        JLabel logoLabel = new JLabel(new ImageIcon(logoImage), SwingConstants.CENTER);
        sidebarPanel.add(logoLabel);
        sidebarPanel.add(Box.createVerticalStrut(30));
        
        // Định nghĩa đường dẫn thư mục chung
        String iconPath = "C:\\TTTN\\POS PROJECT\\img\\";

        // Các nút trong sidebar (Thay bằng icon)
        String[] sidebarIcons = {"home_icon.png", "employee.png", "product.png", "dashboard.png", "warehouse.png", "supplier.png", "logout_icon.png"};
        String[] sidebarNames = {"Home", "Employee", "Product", "Dashboard", "Warehouse", "Supplier", "Logout"};
        for (int i = 0; i < sidebarIcons.length; i++) {
            // Kết hợp đường dẫn thư mục chung với tên file icon
            ImageIcon icon = new ImageIcon(iconPath + sidebarIcons[i]);
            Image scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH); // Resize icon
            JButton button = new JButton(new ImageIcon(scaledImage));
            button.setToolTipText(sidebarNames[i]); // Thêm tooltip để hiển thị tên nút khi hover
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            button.setBackground(sidebarNames[i].equals("Home") ? new Color(139, 69, 19) : Color.WHITE);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            sidebarPanel.add(button);
            sidebarPanel.add(Box.createVerticalStrut(10));
        }
        frame.add(sidebarPanel, BorderLayout.WEST);
        
     // Phần chính (Main Content)
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(240, 242, 245));
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}

}
