package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PosUI extends JFrame{

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PosUI window = new PosUI();
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
	public PosUI() {
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
        String[] sidebarIcons = {"home_icon.png", "menue_icon.png", "history_icon.png", "wallet_icon.png", "promos_icon.png", "logout_icon.png"};
        String[] sidebarNames = {"Home", "Menu", "History", "Wallet", "Promos", "Logout"};
        for (int i = 0; i < sidebarIcons.length; i++) {
            // Kết hợp đường dẫn thư mục chung với tên file icon
            ImageIcon icon = new ImageIcon(iconPath + sidebarIcons[i]);
            Image scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH); // Resize icon
            JButton button = new JButton(new ImageIcon(scaledImage));
            button.setToolTipText(sidebarNames[i]); // Thêm tooltip để hiển thị tên nút khi hover
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            button.setBackground(sidebarNames[i].equals("Menu") ? new Color(139, 69, 19) : Color.WHITE);
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
        
     // Thanh tìm kiếm và danh mục
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel categoryLabel = new JLabel("Choose Category");
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(categoryLabel, BorderLayout.NORTH);

        JTextField searchField = new JTextField("Search category or menu...");
        searchField.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        searchField.setPreferredSize(new Dimension(0, 40));
        topPanel.add(searchField, BorderLayout.CENTER);

        // Định nghĩa đường dẫn thư mục chung cho icon
        String iconPath1 = "C:\\TTTN\\POS PROJECT\\img\\";

        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        categoryPanel.setBackground(Color.WHITE);
        String[] categories = {"All", "Coffee", "Juice", "Milk ", "Snack", "Dessert"};
        String[] categoryIcons = {"all.png", "coffee.png", "juice.png", "milk.png", "snack.png", "dessert.png"};
        for (int i = 0; i < categories.length; i++) {
            try {
                ImageIcon icon = new ImageIcon(iconPath1 + categoryIcons[i]); // Kết hợp đường dẫn với tên file icon
                if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
                    throw new Exception("Icon not found: " + categoryIcons[i]);
                }
                Image scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH); // Resize icon
                JButton categoryButton = new JButton(new ImageIcon(scaledImage));
                categoryButton.setToolTipText(categories[i]); // Thêm tooltip để hiển thị tên danh mục khi hover
                categoryButton.setBackground(Color.LIGHT_GRAY);
                categoryButton.setBorderPainted(false);
                categoryButton.setFocusPainted(false);
                categoryPanel.add(categoryButton);
            } catch (Exception e) {
                System.err.println("Error loading icon: " + categoryIcons[i] + " - " + e.getMessage());
                // Fallback: dùng text nếu không load được icon
                JButton categoryButton = new JButton(categories[i]);
                categoryButton.setBackground(Color.LIGHT_GRAY);
                categoryButton.setBorderPainted(false);
                categoryButton.setFocusPainted(false);
                categoryPanel.add(categoryButton);
            }
        }
        topPanel.add(categoryPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        
     // Phần menu cà phê
        JPanel coffeePanel = new JPanel(new BorderLayout());
        coffeePanel.setBackground(new Color(240, 242, 245));

        JLabel coffeeMenuLabel = new JLabel("Coffee Menu");
        coffeeMenuLabel.setFont(new Font("Arial", Font.BOLD, 16));
        coffeePanel.add(coffeeMenuLabel, BorderLayout.NORTH);

        JPanel coffeeItemsPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        coffeeItemsPanel.setBackground(new Color(240, 242, 245));
        
     // Phần này bạn sẽ thay bằng logic từ file riêng
        // Tôi để dữ liệu giả để minh họa giao diện
        String[] coffeeNames = {"Caramel Frappuccino", "Chocolate Frappuccino", "Peppermint Macchiato", "Coffee Latte"};
        String[] coffeePrices = {"$3.95", "$4.51", "$5.34", "$4.79"};
        for (int i = 0; i < coffeeNames.length; i++) {
            JPanel coffeeItem = new JPanel(new BorderLayout());
            coffeeItem.setBackground(Color.WHITE);
            coffeeItem.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel coffeeImage = new JLabel(coffeeNames[i], SwingConstants.CENTER); // Thay bằng hình ảnh nếu cần
            coffeeItem.add(coffeeImage, BorderLayout.CENTER);

            JLabel coffeeName = new JLabel(coffeeNames[i], SwingConstants.CENTER);
            coffeeName.setFont(new Font("Arial", Font.BOLD, 14));
            coffeeItem.add(coffeeName, BorderLayout.NORTH);

            JLabel coffeePrice = new JLabel(coffeePrices[i], SwingConstants.CENTER);
            coffeeItem.add(coffeePrice, BorderLayout.SOUTH);

            // Tùy chọn Mood, Size, Sugar, Ice
            JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
            optionsPanel.add(new JLabel("Mood"));
            optionsPanel.add(new JLabel("Size"));
            optionsPanel.add(new JLabel("Sugar"));
            optionsPanel.add(new JLabel("Ice"));
            coffeeItem.add(optionsPanel, BorderLayout.CENTER);

            JButton addButton = new JButton("Add to Billing");
            addButton.setBackground(new Color(139, 69, 19));
            addButton.setForeground(Color.WHITE);
            addButton.setBorderPainted(false);
            addButton.setFocusPainted(false);
            coffeeItem.add(addButton, BorderLayout.SOUTH);

            coffeeItemsPanel.add(coffeeItem);
        }
        
        coffeePanel.add(new JScrollPane(coffeeItemsPanel), BorderLayout.CENTER);
        mainPanel.add(coffeePanel, BorderLayout.CENTER);
        
     // Phần hóa đơn (Bills)
        JPanel billPanel = new JPanel(new BorderLayout());
        billPanel.setBackground(Color.WHITE);
        billPanel.setPreferredSize(new Dimension(300, 0));
        billPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel billLabel = new JLabel("BILLS");
        billLabel.setFont(new Font("Arial", Font.BOLD, 16));
        billPanel.add(billLabel, BorderLayout.NORTH);

        JPanel billItemsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        billItemsPanel.setBackground(Color.WHITE);

        // Thêm các mục hóa đơn (dữ liệu giả)
        String[] billItems = {"Caramel Frappuccino x1 $3.95", "Chocolate Frappuccino x2 $9.02", "Peppermint Macchiato x1 $5.34"};
        for (String item : billItems) {
            JLabel billItem = new JLabel(item);
            billItemsPanel.add(billItem);
        }

        billPanel.add(new JScrollPane(billItemsPanel), BorderLayout.CENTER);

        // Tổng tiền
        JPanel totalPanel = new JPanel(new GridLayout(3, 2));
        totalPanel.setBackground(Color.WHITE);
        totalPanel.add(new JLabel("Subtotal"));
        totalPanel.add(new JLabel("$18.31"));
        totalPanel.add(new JLabel("Tax (10%)"));
        totalPanel.add(new JLabel("$1.831"));
        totalPanel.add(new JLabel("Total"));
        totalPanel.add(new JLabel("$20.141"));
        billPanel.add(totalPanel, BorderLayout.SOUTH);

        // Phương thức thanh toán (bỏ Credit Card)
        JPanel paymentPanel = new JPanel(new FlowLayout());
        paymentPanel.setBackground(Color.WHITE);
        JButton cashButton = new JButton("Cash");
        JButton eWalletButton = new JButton("E-Wallet");
        paymentPanel.add(cashButton);
        paymentPanel.add(eWalletButton);

        billPanel.add(paymentPanel, BorderLayout.SOUTH);

        JButton printBillButton = new JButton("Print Bills");
        printBillButton.setBackground(new Color(139, 69, 19));
        printBillButton.setForeground(Color.WHITE);
        printBillButton.setBorderPainted(false);
        printBillButton.setFocusPainted(false);
        billPanel.add(printBillButton, BorderLayout.SOUTH);

        frame.add(billPanel, BorderLayout.EAST);
        frame.add(mainPanel, BorderLayout.CENTER);

        // Phần thông tin nhân viên (góc trên bên phải)
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(new Color(240, 242, 245));
        JLabel userLabel = new JLabel("Jelly Grande");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userPanel.add(userLabel);
        userPanel.add(new JLabel("I'm a Cashier"));
        mainPanel.add(userPanel, BorderLayout.NORTH);
	}

}
