package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import model.Owner;
import service.AuthenticationService;
import service.CategoryService;
import service.HashService;
import service.ItemService;
import service.PersonService;
import service.ProductService;
import service.StoreServiceImpl;
import service.SupplierService;
import ui.Elements.SidebarPanel;

public class HomeOwnerFrame extends JFrame implements SidebarPanel.SidebarListener{

	private final String iconPath = "C:\\TTTN\\POS PROJECT\\img\\";
	private final String[] sidebarIcons = { "home_icon.png", "customers.png", "employee.png", "product.png",
			"dashboard.png", "supplier.png", "warehouse.png", "store.png", "logout_icon.png" };
	private final String[] sidebarNames = { "Home", "Customer", "Employee", "Product", "Dashboard", "Supplier",
			"Warehouse", "Store", "Logout" };
	private JPanel contentPanel;
	private final String username ; 
	private final ImageIcon logoIcon = new ImageIcon("C:\\TTTN\\POS PROJECT\\img\\lck.png");
	private PersonService personService;
	private SupplierService supplierService;
	private ItemService itemService;
	private StoreServiceImpl storeService;
	private HashService hashService;
	private AuthenticationService authService;
	private CategoryService categoryService;
	private ProductService productService;


	public HomeOwnerFrame(PersonService personService, SupplierService supplierService, ItemService itemService,
			StoreServiceImpl storeService, HashService hashService, AuthenticationService authService,
			ProductService productService, CategoryService categoryService,String username) {
		this.personService = personService;
		this.supplierService = supplierService;
		this.itemService = itemService;
		this.storeService = storeService;
		this.hashService = hashService;
		this.authService = authService;
		this.categoryService = categoryService;
		this.productService = productService;
		this.username = username;
		
		setTitle("Thông tin");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		setLayout(new BorderLayout());

		contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);

		SidebarPanel sidebar = new SidebarPanel(sidebarIcons, sidebarNames, iconPath, username, this);
		add(sidebar, BorderLayout.WEST);

		loadProfilePanel(); // Default to Employee page
		add(contentPanel, BorderLayout.CENTER);
		setVisible(true);
	}

	@Override
	public void onSidebarItemClick(String pageName) {
		contentPanel.removeAll();
		switch (pageName) {
		case "Home":
			loadProfilePanel();
			break;
		case "Customer":
			openCustomerManager();
			break;
		case "Employee":
			openEmployeeManager();
			break;
		case "Product":
			openProductFrame();
			break;
		case "Dashboard":
			loadDashboardPanel();
			break;
		case "Supplier":
			openSupplierFrame();
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
	
	 private void loadProfilePanel() {
	        try {
				Owner owner = personService.getOwnerByUsername(username);
	            if (owner == null) {
	                JOptionPane.showMessageDialog(this, "Owner not found for username: " + username, "Error",
	                        JOptionPane.ERROR_MESSAGE);
	                return;
	            }
	            ProfilePanel profilePanel = new ProfilePanel(personService);
	            profilePanel.setPerson(owner);
	            contentPanel.add(profilePanel, BorderLayout.CENTER);
	        } catch (Exception e) {
	            JOptionPane.showMessageDialog(this, "Error loading profile: " + e.getMessage(), "Error",
	                    JOptionPane.ERROR_MESSAGE);
	        }
	    }
	private void openEmployeeManager() {
		new EmployeeManager(personService, supplierService, itemService, storeService, hashService, authService,
				productService, categoryService,username).setVisible(true);
		dispose();

	}
	private void openCustomerManager() {
		new CustomerManager(personService, supplierService, itemService, storeService, hashService, authService,
				productService, categoryService,username).setVisible(true);
		dispose();
	}

	private void openProductFrame() {
		ProductFrame productFrame = new ProductFrame(personService, supplierService, itemService, storeService,
				hashService, authService, productService, categoryService,username);
		productFrame.setVisible(true);
		dispose();
	}

	private void loadDashboardPanel() {
		JPanel dashboardPanel = new JPanel(new BorderLayout());
		dashboardPanel.setBackground(Color.WHITE);
		dashboardPanel.add(new JLabel("Dashboard Page (Under Construction)", SwingConstants.CENTER));
		contentPanel.add(dashboardPanel, BorderLayout.CENTER);
	}

	private void loadWarehousePanel() {
		JPanel warehousePanel = new JPanel(new BorderLayout());
		warehousePanel.setBackground(Color.WHITE);
		warehousePanel.add(new JLabel("Warehouse Page (Under Construction)", SwingConstants.CENTER));
		contentPanel.add(warehousePanel, BorderLayout.CENTER);
	}

	private void openStoreFrame() {
		StoreFrame storeFrame = new StoreFrame(supplierService, itemService, storeService, personService, hashService,
				authService, productService, categoryService,username);
		storeFrame.setVisible(true);
		dispose();
	}

	private void openSupplierFrame() {
		SupplierFrame supplierFrame = new SupplierFrame(supplierService, itemService, storeService, personService,
				hashService, authService, productService, categoryService,username);
		supplierFrame.setVisible(true);
		dispose();
	}

	private void handleLogout() {
	    int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận đăng xuất",
	            JOptionPane.YES_NO_OPTION);
	    if (confirm == JOptionPane.YES_OPTION) {
	        dispose(); // Đóng ProductFrame hiện tại
	        SwingUtilities.invokeLater(() -> {
	        	LoginFrame loginFrame = new LoginFrame();
	        	loginFrame.showFrame();

	        });
	    }
	}
}
