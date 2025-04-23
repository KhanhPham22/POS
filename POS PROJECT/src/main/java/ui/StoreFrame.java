package ui;

import javax.swing.*;

import dao.StoreDao;
import model.Store;
import java.awt.*;
import ui.Elements.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StoreFrame extends JFrame implements SidebarPanel.SidebarListener {
	private JPanel contentPanel;
	private final String iconPath = "C:\\TTTN\\POS PROJECT\\img\\";
	private final String[] sidebarIcons = { "home_icon.png", "customers.png", "employee.png", "product.png",
			"dashboard.png", "supplier.png", "warehouse.png", "store.png", "logout_icon.png" };
	private final String[] sidebarNames = { "Home", "Customer", "Employee", "Product", "Dashboard", "Supplier",
			"Warehouse", "Store", "Logout" };
	private final String username = "admin"; // Replace with actual username
	private final ImageIcon logoIcon = new ImageIcon("C:\\TTTN\\POS PROJECT\\img\\lck.png");

	public StoreFrame() {
		setTitle("Quản lý cửa hàng");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		setLayout(new BorderLayout());

		contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);

		SidebarPanel sidebar = new SidebarPanel(sidebarIcons, sidebarNames, iconPath, username, this);
		add(sidebar, BorderLayout.WEST);

		loadStorePanel(); // Default to Store page
		add(contentPanel, BorderLayout.CENTER);
	}

	@Override
	public void onSidebarItemClick(String pageName) {
		contentPanel.removeAll();
		switch (pageName) {
		case "Home":
			loadHomePanel();
			break;
		case "Customer":
			loadCustomerPanel();
			break;
		case "Employee":
			loadEmployeePanel();
			break;
		case "Product":
			loadProductPanel();
			break;
		case "Dashboard":
			loadDashboardPanel();
			break;
		case "Supplier":
			loadSupplierPanel();
			break;
		case "Warehouse":
			loadWarehousePanel();
			break;
		case "Store":
			loadStorePanel();
			break;
		case "Logout":
			handleLogout();
			break;
		default:
			contentPanel.add(new JLabel("Unknown page: " + pageName, SwingConstants.CENTER));
		}
		contentPanel.revalidate();
		contentPanel.repaint();
	}

	private void loadStorePanel() {
	    StoreDao storeDao = new StoreDao();
	    storeDao.setClass(Store.class);
	    try {
	        List<Store> stores = storeDao.findAll();
	        if (!stores.isEmpty()) {
	            Store store = stores.get(0);

	            Color blueBg = new Color(230, 240, 255); // Light blue background
	            Color borderColor = new Color(180, 200, 230); // Light border
	            JPanel storePanel = new JPanel(new BorderLayout(10, 10));
	            storePanel.setBackground(Color.WHITE);

	            // Title panel
	            JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	            JLabel titleLabel = new JLabel("Thông tin cửa hàng");
	            titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
	            titlePanel.add(titleLabel);
	            titlePanel.setBackground(Color.WHITE);

	            // Main content panel with centered logo and details
	            JPanel mainContentPanel = new JPanel(new BorderLayout(10, 10));
	            mainContentPanel.setBackground(Color.WHITE);
	            mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Add padding for more white space

	            // Logo panel (centered)
	            JPanel logoPanel = new JPanel();
	            logoPanel.setBackground(Color.WHITE);
	            logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	            JLabel logoLabel = new JLabel();
	            if (logoIcon.getImage() != null) {
	                Image scaledImage = logoIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
	                logoLabel.setIcon(new ImageIcon(scaledImage));
	            } else {
	                logoLabel.setText("LOGO SHOP");
	            }
	            logoLabel.setPreferredSize(new Dimension(100, 100));
	            logoLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
	            logoPanel.add(logoLabel);

	            // Details panel (wrapped in a frame with a table)
	            JPanel detailsWrapper = new JPanel(new BorderLayout());
	            detailsWrapper.setBackground(blueBg);
	            detailsWrapper.setBorder(BorderFactory.createLineBorder(borderColor));
	            detailsWrapper.setPreferredSize(new Dimension(0, 300)); // Adjust height as needed

	            // Create table for store details
	            String[] columnNames = {"Thông tin", "Chi tiết"};
	            Object[][] data = {
	                {"Tên cửa hàng:", store.getName()},
	                {"Tên viết tắt cửa hàng:", store.getShortName()},
	                {"Mô tả:", store.getDescription()},
	                {"Địa chỉ:", store.getAddress()},
	                {"Thành phố:", store.getCity()},
	                {"Quận:", store.getState()},
	                {"Zip:", store.getZip()},
	                {"Số điện thoại:", store.getPhone()},
	                {"Email:", store.getEmail()},
	                {"Số fax:", store.getFax()}
	            };

	            JTable table = new JTable(data, columnNames);
	            table.setBackground(blueBg);
	            table.setFillsViewportHeight(true);
	            table.setRowHeight(25); // Adjust row height for better readability
	            table.setGridColor(Color.LIGHT_GRAY); // Border color for table cells
	            table.setShowGrid(true); // Show grid lines
	            table.setEnabled(false); // Disable editing to make it display-only

	            // Customize table header with a bolder font
                Font headerFont = new Font("Arial", Font.BOLD, 16); // Even bolder for header
                table.getTableHeader().setFont(headerFont);
                table.getTableHeader().setBackground(new Color(200, 220, 240)); // Slightly darker blue for header

	            // Add table to a scroll pane
	            JScrollPane scrollPane = new JScrollPane(table);
	            scrollPane.setBackground(blueBg);
	            scrollPane.setBorder(BorderFactory.createEmptyBorder()); // Remove scroll pane border for seamless look

	            detailsWrapper.add(scrollPane, BorderLayout.CENTER);

	            // Add components to main content panel
	            mainContentPanel.add(logoPanel, BorderLayout.NORTH);
	            mainContentPanel.add(detailsWrapper, BorderLayout.CENTER);

	            // Add components to store panel
	            storePanel.add(titlePanel, BorderLayout.NORTH);
	            storePanel.add(mainContentPanel, BorderLayout.CENTER);

	            contentPanel.add(storePanel, BorderLayout.CENTER);
	        } else {
	            JOptionPane.showMessageDialog(this, "Không tìm thấy cửa hàng trong cơ sở dữ liệu.", "Lỗi",
	                    JOptionPane.ERROR_MESSAGE);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Lỗi khi truy xuất dữ liệu: " + e.getMessage(), "Lỗi",
	                JOptionPane.ERROR_MESSAGE);
	    }
	}

	private void loadHomePanel() {
		JPanel homePanel = new JPanel(new BorderLayout());
		homePanel.setBackground(Color.WHITE);
		homePanel.add(new JLabel("Home Page (Under Construction)", SwingConstants.CENTER));
		contentPanel.add(homePanel, BorderLayout.CENTER);
	}

	private void loadCustomerPanel() {
		JPanel customerPanel = new JPanel(new BorderLayout());
		customerPanel.setBackground(Color.WHITE);
		customerPanel.add(new JLabel("Customer Page (Under Construction)", SwingConstants.CENTER));
		contentPanel.add(customerPanel, BorderLayout.CENTER);
	}

	private void loadEmployeePanel() {
		JPanel employeePanel = new JPanel(new BorderLayout());
		employeePanel.setBackground(Color.WHITE);
		employeePanel.add(new JLabel("Employee Page (Under Construction)", SwingConstants.CENTER));
		contentPanel.add(employeePanel, BorderLayout.CENTER);
	}

	private void loadProductPanel() {
		JPanel productPanel = new JPanel(new BorderLayout());
		productPanel.setBackground(Color.WHITE);
		productPanel.add(new JLabel("Product Page (Under Construction)", SwingConstants.CENTER));
		contentPanel.add(productPanel, BorderLayout.CENTER);
	}

	private void loadDashboardPanel() {
		JPanel dashboardPanel = new JPanel(new BorderLayout());
		dashboardPanel.setBackground(Color.WHITE);
		dashboardPanel.add(new JLabel("Dashboard Page (Under Construction)", SwingConstants.CENTER));
		contentPanel.add(dashboardPanel, BorderLayout.CENTER);
	}

	private void loadSupplierPanel() {
		JPanel supplierPanel = new JPanel(new BorderLayout());
		supplierPanel.setBackground(Color.WHITE);
		supplierPanel.add(new JLabel("Supplier Page (Under Construction)", SwingConstants.CENTER));
		contentPanel.add(supplierPanel, BorderLayout.CENTER);
	}

	private void loadWarehousePanel() {
		JPanel warehousePanel = new JPanel(new BorderLayout());
		warehousePanel.setBackground(Color.WHITE);
		warehousePanel.add(new JLabel("Warehouse Page (Under Construction)", SwingConstants.CENTER));
		contentPanel.add(warehousePanel, BorderLayout.CENTER);
	}

	private void handleLogout() {
		int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận đăng xuất",
				JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			dispose(); // Close the frame (or redirect to login screen)
		}
	}

	private void addInfoRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, String valueText) {
		gbc.gridx = 0;
		gbc.gridy = row;
		panel.add(new JLabel(labelText), gbc);

		gbc.gridx = 1;
		panel.add(new JLabel(valueText != null ? valueText : ""), gbc);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new StoreFrame().setVisible(true);
		});
	}
}
