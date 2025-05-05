package ui;

import java.awt.*;
import ui.Elements.*;
import javax.swing.*;

import dao.EmployeeDao;
import dao.OwnerDao;
import dao.PersonDao;
import dao.StoreDao;
import model.Employee;
import model.Owner;
import service.ItemService;
import service.PersonService;
import service.PersonServiceImpl;
import service.StoreServiceImpl;
import service.SupplierService;
import service.HashService;
import ui.Elements.SearchBar;
import ui.Elements.SidebarPanel;

import java.awt.*;
import java.util.List;

public class EmployeeManager extends JFrame implements SidebarPanel.SidebarListener {
	private final String iconPath = "C:\\TTTN\\POS PROJECT\\img\\";
	private final String[] sidebarIcons = { "home_icon.png", "customers.png", "employee.png", "product.png",
			"dashboard.png", "supplier.png", "warehouse.png", "store.png", "logout_icon.png" };
	private final String[] sidebarNames = { "Home", "Customer", "Employee", "Product", "Dashboard", "Supplier",
			"Warehouse", "Store", "Logout" };
	private JPanel contentPanel;
	private final String username = "admin"; // Replace with actual username
	private final ImageIcon logoIcon = new ImageIcon("C:\\TTTN\\POS PROJECT\\img\\lck.png");

	private PersonService personService;
	private SupplierService supplierService;
	private ItemService itemService;
	private StoreServiceImpl storeService;
	private HashService hashService;

	public EmployeeManager(PersonService personService, HashService hashService) {
		this.personService = personService;
		setTitle("Quản lý nhân viên");
		setSize(800, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		setLayout(new BorderLayout());

		contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);

		SidebarPanel sidebar = new SidebarPanel(sidebarIcons, sidebarNames, iconPath, username, this);
		add(sidebar, BorderLayout.WEST);

		loadPersonPanel(); // Default to Employee page
		add(contentPanel, BorderLayout.CENTER);
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
			loadPersonPanel();
			break;
		case "Product":
			loadProductPanel();
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

	private void loadPersonPanel() {
		PersonPanel personPanel = new PersonPanel(personService, hashService);
		contentPanel.add(personPanel, BorderLayout.CENTER);
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

	private void loadWarehousePanel() {
		JPanel warehousePanel = new JPanel(new BorderLayout());
		warehousePanel.setBackground(Color.WHITE);
		warehousePanel.add(new JLabel("Warehouse Page (Under Construction)", SwingConstants.CENTER));
		contentPanel.add(warehousePanel, BorderLayout.CENTER);
	}

	private void openStoreFrame() {
		StoreServiceImpl storeService = new StoreServiceImpl(new StoreDao()); // Use StoreServiceImpl directly
		StoreFrame storeFrame = new StoreFrame(supplierService, itemService, storeService);
		storeFrame.setVisible(true);
		dispose();
	}

	private void openSupplierFrame() {
		new SupplierFrame(supplierService, itemService);
		dispose(); // Close current frame
	}

	private void handleLogout() {
		int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận đăng xuất",
				JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			dispose(); // Close the frame (or redirect to login screen)
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			EmployeeDao employeeDao = new EmployeeDao();
			OwnerDao ownerDao = new OwnerDao();
			PersonService personService = new PersonServiceImpl(employeeDao, null, ownerDao);
			HashService hashService = new HashService();
			EmployeeManager frame = new EmployeeManager(personService, hashService);
		});
	}

}
