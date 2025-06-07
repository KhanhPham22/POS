package ui;

import java.awt.*;
import ui.Elements.*;
import javax.swing.*;

import dao.CustomerDao;
import dao.EmployeeDao;
import dao.ItemDao;
import dao.OwnerDao;
import dao.PersonDao;
import dao.StoreDao;
import dao.SupplierDao;
import dao.UserSessionDao;
import model.Employee;
import model.Owner;
import service.ItemService;
import service.ItemServiceImpl;
import service.PersonService;
import service.PersonServiceImpl;
import service.ProductService;
import service.StoreServiceImpl;
import service.SupplierService;
import service.SupplierServiceImpl;
import service.AuthenticationService;
import service.CategoryService;
import service.DashboardService;
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
	private DashboardService dashboardService;

	public EmployeeManager(PersonService personService, SupplierService supplierService, ItemService itemService,
			StoreServiceImpl storeService, HashService hashService, AuthenticationService authService,
			ProductService productService, CategoryService categoryService,DashboardService dashboardService,String username) {
		this.personService = personService;
		this.supplierService = supplierService;
		this.itemService = itemService;
		this.storeService = storeService;
		this.hashService = hashService;
		this.authService = authService;
		this.categoryService = categoryService;
		this.productService = productService;
		this.dashboardService = dashboardService;
		this.username = username;

		setTitle("Quản lý nhân viên");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
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
			loadProfilePanel();
			break;
		case "Customer":
			openCustomerManager();
			break;
		case "Employee":
			loadPersonPanel();
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
		PersonPanel personPanel = new PersonPanel(personService, hashService, authService);
		contentPanel.add(personPanel, BorderLayout.CENTER);
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
				productService, categoryService,dashboardService,username).setVisible(true);
		dispose();
	}

	private void openProductFrame() {
		ProductFrame productFrame = new ProductFrame(personService, supplierService, itemService, storeService,
				hashService, authService, productService, categoryService,dashboardService,username);
		productFrame.setVisible(true);
		dispose();
	}

	private void openDashboardFrame() {
	    DashboardFrame dashboardFrame = new DashboardFrame(supplierService, itemService, storeService, personService,
	            hashService, authService, productService, categoryService, dashboardService, username);
	    dashboardFrame.setVisible(true);
	    dispose();
	}

	private void loadWarehousePanel() {
		JPanel warehousePanel = new JPanel(new BorderLayout());
		warehousePanel.setBackground(Color.WHITE);
		warehousePanel.add(new JLabel("Warehouse Page (Under Construction)", SwingConstants.CENTER));
		contentPanel.add(warehousePanel, BorderLayout.CENTER);
	}

	private void openStoreFrame() {
		StoreFrame storeFrame = new StoreFrame(supplierService, itemService, storeService, personService, hashService,
				authService, productService, categoryService,dashboardService,username);
		storeFrame.setVisible(true);
		dispose();
	}

	private void openSupplierFrame() {
		SupplierFrame supplierFrame = new SupplierFrame(supplierService, itemService, storeService, personService,
				hashService, authService, productService, categoryService,dashboardService, username);
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

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            try {
//                // Khởi tạo các DAO
//                EmployeeDao employeeDao = new EmployeeDao();
//                OwnerDao ownerDao = new OwnerDao();
//                CustomerDao customerDao = new CustomerDao();
//                SupplierDao supplierDao = new SupplierDao();
//                ItemDao itemDao = new ItemDao();
//                StoreDao storeDao = new StoreDao();
//                UserSessionDao userSessionDao = new UserSessionDao();
//                
//                // Khởi tạo các service
//                PersonService personService = new PersonServiceImpl(employeeDao, customerDao, ownerDao);
//                SupplierService supplierService = new SupplierServiceImpl(supplierDao);
//                ItemService itemService = new ItemServiceImpl(itemDao);
//                StoreServiceImpl storeService = new StoreServiceImpl(storeDao);
//                HashService hashService = new HashService();
//                AuthenticationService authService = new AuthenticationService(employeeDao, ownerDao, userSessionDao, hashService);
//                
//                // Tạo và hiển thị frame chính
//                EmployeeManager frame = new EmployeeManager(
//                    personService, 
//                    supplierService, 
//                    itemService, 
//                    storeService, 
//                    hashService,
//                    authService
//                );
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