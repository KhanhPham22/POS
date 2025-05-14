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
import dao.StoreDao;
import dao.SupplierDao;
import model.Item;
import model.Supplier;
import model.Warehouse;
import service.AuthenticationService;
import service.CategoryService;
import service.HashService;
import service.ItemService;
import service.ItemServiceImpl;
import service.PersonService;
import service.ProductService;
import service.StoreService;
import service.StoreServiceImpl;
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
	private final String username ;
	private final ImageIcon logoIcon = new ImageIcon("C:\\TTTN\\POS PROJECT\\img\\lck.png");

	private final SupplierService supplierService;
	private final ItemService itemService;
	private StoreServiceImpl storeService;
	private PersonService personService;
	private HashService hashService;
	private AuthenticationService authService;
	private CategoryService categoryService;
	private ProductService productService;

	public SupplierFrame(SupplierService supplierService, ItemService itemService, StoreServiceImpl storeService,
			PersonService personService, HashService hashService, AuthenticationService authService,
			ProductService productService, CategoryService categoryService,String username) {
		this.supplierService = supplierService;
		this.itemService = itemService;
		this.storeService = storeService;
		this.personService = personService;
		this.hashService = hashService;
		this.authService = authService;
		this.categoryService = categoryService;
		this.productService = productService;
		this.username = username;

		setTitle("Supplier Management");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		contentPanel = new JPanel(new BorderLayout());
		add(contentPanel, BorderLayout.CENTER);

		SidebarPanel sidebar = new SidebarPanel(sidebarIcons, sidebarNames, iconPath, username, this);
		add(sidebar, BorderLayout.WEST);

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

	private void openCustomerManager() {
		new CustomerManager(personService, supplierService, itemService, storeService, hashService, authService,
				productService, categoryService,username).setVisible(true);
		dispose();
	}

	private void openEmployeeManager() {
		new EmployeeManager(personService, supplierService, itemService, storeService, hashService, authService,
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
		StoreServiceImpl storeService = new StoreServiceImpl(new StoreDao()); // Use StoreServiceImpl directly
		StoreFrame storeFrame = new StoreFrame(supplierService, itemService, storeService, personService, hashService,
				authService, productService, categoryService,username);
		storeFrame.setVisible(true);
		dispose();
	}

	private void handleLogout() {
		int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận đăng xuất",
				JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			dispose();
		}
	}

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            try {
//                SupplierService supplierService = new SupplierServiceImpl(new SupplierDao());
//                ItemService itemService = new ItemServiceImpl(new ItemDao());
//                SupplierFrame frame = new SupplierFrame(supplierService, itemService);
//                frame.setVisible(true);
//            } catch (Exception e) {
//                e.printStackTrace();
//                JOptionPane.showMessageDialog(null,
//                    "Lỗi khi khởi chạy ứng dụng: " + e.getMessage(),
//                    "Lỗi", JOptionPane.ERROR_MESSAGE);
//            }
//        });
//    }
}
