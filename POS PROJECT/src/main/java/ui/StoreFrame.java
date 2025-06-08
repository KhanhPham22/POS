package ui;

import javax.swing.*;

import dao.CustomerDao;
import dao.EmployeeDao;
import dao.ItemDao;
import dao.OwnerDao;
import dao.PersonDao;
import dao.StoreDao;
import dao.SupplierDao;
import model.Item;
import model.Owner;
import model.Store;
import model.Supplier;
import service.AuthenticationService;
import service.CategoryService;
import service.DashboardService;
import service.HashService;
import service.InvoiceService;
import service.ItemService;
import service.ItemServiceImpl;
import service.PersonService;
import service.PersonServiceImpl;
import service.ProductService;
import service.StoreServiceImpl;
import service.SupplierService;
import service.SupplierServiceImpl;

import java.awt.*;
import ui.Elements.*;
import java.awt.*;
import java.util.List;

public class StoreFrame extends JFrame implements SidebarPanel.SidebarListener {
	private JPanel contentPanel;
	private final String iconPath = "C:\\TTTN\\POS PROJECT\\img\\";
	private final String[] sidebarIcons = { "home_icon.png", "customers.png", "employee.png", "product.png",
			"dashboard.png", "supplier.png", "warehouse.png", "store.png", "logout_icon.png" };
	private final String[] sidebarNames = { "Home", "Customer", "Employee", "Product", "Dashboard", "Supplier",
			"Warehouse", "Store", "Logout" };
	private final String username ; 
	private final ImageIcon logoIcon = new ImageIcon("C:\\TTTN\\POS PROJECT\\img\\lck.png");

	private SupplierService supplierService;
	private ItemService itemService;
	private StoreServiceImpl storeService;
	private PersonService personService;
	private HashService hashService;
	private AuthenticationService authService;
	private CategoryService categoryService;
	private ProductService productService;
	private DashboardService dashboardService;
	private InvoiceService invoiceService;

	public StoreFrame(SupplierService supplierService, ItemService itemService, StoreServiceImpl storeService,
			PersonService personService, HashService hashService, AuthenticationService authService,
			ProductService productService, CategoryService categoryService,DashboardService dashboardService,InvoiceService invoiceService,String username) {
		this.supplierService = supplierService;
		this.itemService = itemService;
		this.storeService = storeService;
		this.personService = personService;
		this.hashService = hashService;
		this.authService = authService;
		this.categoryService = categoryService;
		this.productService = productService;
		this.dashboardService = dashboardService;
		this.invoiceService = invoiceService;
		this.username = username;

		setTitle("Quản lý cửa hàng");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
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
			openDashboardFrame();
			break;
		case "Supplier":
			openSupplierFrame();
			break;
		case "Warehouse":
			openWarehouseFrame();
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
		StorePanel storePanel = new StorePanel(storeService);
		contentPanel.add(storePanel, BorderLayout.CENTER);
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

	private void openCustomerManager() {
		new CustomerManager(personService, supplierService, itemService, storeService, hashService, authService,
				productService, categoryService,dashboardService,invoiceService,username).setVisible(true);
		dispose();
	}

	private void openEmployeeManager() {
		new EmployeeManager(personService, supplierService, itemService, storeService, hashService, authService,
				productService, categoryService,dashboardService,invoiceService,username).setVisible(true);
		dispose();
	}

	private void openProductFrame() {
		ProductFrame productFrame = new ProductFrame(personService, supplierService, itemService, storeService,
				hashService, authService, productService, categoryService,dashboardService,invoiceService,username);
		productFrame.setVisible(true);
		dispose();
	}

	private void openDashboardFrame() {
	    DashboardFrame dashboardFrame = new DashboardFrame(supplierService, itemService, storeService, personService,
	            hashService, authService, productService, categoryService, dashboardService,invoiceService, username);
	    dashboardFrame.setVisible(true);
	    dispose();
	}

	private void openSupplierFrame() {
		SupplierFrame supplierFrame = new SupplierFrame(supplierService, itemService, storeService, personService,
				hashService, authService, productService, categoryService,dashboardService,invoiceService,username);
		supplierFrame.setVisible(true);
		dispose();
	}

	private void openWarehouseFrame() {
        WarehouseFrame warehouseFrame = new WarehouseFrame(supplierService, itemService, storeService, personService,
                hashService, authService, productService, categoryService, dashboardService,invoiceService, username);
        warehouseFrame.setVisible(true);
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

	private void addInfoRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, String valueText) {
		gbc.gridx = 0;
		gbc.gridy = row;
		panel.add(new JLabel(labelText), gbc);

		gbc.gridx = 1;
		panel.add(new JLabel(valueText != null ? valueText : ""), gbc);
	}



}
