package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import org.hibernate.SessionFactory;

import dao.ItemDao;
import dao.SupplierDao;
import model.Item;
import model.Supplier;
import model.Warehouse;
import service.ItemService;
import service.ItemServiceImpl;
import service.SupplierService;
import service.SupplierServiceImpl;
import ui.Elements.SidebarPanel;

public class SupplierFrame extends JFrame implements SidebarPanel.SidebarListener {
	private JPanel contentPanel;
	private final String iconPath = "C:\\TTTN\\POS PROJECT\\img\\";
	private final String[] sidebarIcons = { "home_icon.png", "customers.png", "employee.png", "product.png",
			"dashboard.png", "supplier.png", "warehouse.png", "store.png", "logout_icon.png" };
	private final String[] sidebarNames = { "Home", "Customer", "Employee", "Product", "Dashboard", "Supplier",
			"Warehouse", "Store", "Logout" };
	private final String username = "admin"; // Replace with actual username
	private final ImageIcon logoIcon = new ImageIcon("C:\\TTTN\\POS PROJECT\\img\\lck.png");

	private final SupplierService supplierService;
	private final ItemService itemService;

	public SupplierFrame(SupplierService supplierService, ItemService itemService) {
		this.supplierService = supplierService;
		this.itemService = itemService;

		setTitle("Supplier Management");
		setSize(1200, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		contentPanel = new JPanel(new BorderLayout());
		add(contentPanel, BorderLayout.CENTER);

		SidebarPanel sidebar = new SidebarPanel(sidebarIcons, sidebarNames, iconPath, username, this);

		add(sidebar, BorderLayout.WEST);

		// load supplier page mặc định
		loadSupplierPanel();

		setVisible(true);
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
			openStoreFrame();
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
		SupplierPanel supplierPanel = new SupplierPanel(supplierService, itemService);
		contentPanel.add(supplierPanel, BorderLayout.CENTER);
	}

	private void loadWarehousePanel() {
		JPanel warehousePanel = new JPanel(new BorderLayout());
		warehousePanel.setBackground(Color.WHITE);
		warehousePanel.add(new JLabel("Warehouse Page (Under Construction)", SwingConstants.CENTER));
		contentPanel.add(warehousePanel, BorderLayout.CENTER);
	}

	private void openStoreFrame() {
		StoreFrame storeFrame = new StoreFrame(supplierService, itemService); // đảm bảo StoreFrame có constructor phù
																				// hợp
		storeFrame.setVisible(true);
		dispose(); // đóng SupplierFrame hiện tại
	}

	private void handleLogout() {
		int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận đăng xuất",
				JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			dispose(); // Close the frame (or redirect to login screen)
		}
	}
	
	  public static void main(String[] args) {
	        // Khởi tạo service trực tiếp (Hibernate sẽ tự động được khởi tạo qua DAO)
	        SupplierService supplierService = new SupplierServiceImpl(new SupplierDao());
	        ItemService itemService = new ItemServiceImpl(new ItemDao());
	        
	        // Chạy UI trên EDT
	        SwingUtilities.invokeLater(() -> {
	            try {
	                SupplierFrame frame = new SupplierFrame(supplierService, itemService);
	                frame.setVisible(true);
	            } catch (Exception e) {
	                e.printStackTrace();
	                JOptionPane.showMessageDialog(null, 
	                    "Lỗi khi khởi chạy ứng dụng: " + e.getMessage(), 
	                    "Lỗi", JOptionPane.ERROR_MESSAGE);
	            }
	        });
	    }




}
