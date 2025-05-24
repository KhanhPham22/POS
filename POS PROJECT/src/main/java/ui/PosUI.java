package ui;

import javax.swing.*;
import java.awt.*;
import ui.Elements.*;

import dao.CategoryDao;
import dao.CustomerDao;
import dao.EmployeeDao;
import dao.InvoiceDao;
import dao.ItemDao;
import dao.OrderDetailDao;
import dao.OwnerDao;
import dao.PaymentDao;
import dao.PersonDao;
import dao.ProductDao;
import dao.StoreDao;
import dao.SupplierDao;
import dao.UserSessionDao;
import model.Employee;
import model.Owner;
import service.AuthenticationService;
import service.CategoryService;
import service.CategoryServiceImpl;
import service.HashService;
import service.InvoiceService;
import service.InvoiceServiceImpl;
import service.ItemService;
import service.ItemServiceImpl;
import service.OrderService;
import service.OrderServiceImpl;
import service.PaymentService;
import service.PaymentServiceImpl;
import service.PersonService;
import service.PersonServiceImpl;
import service.ProductService;
import service.ProductServiceImpl;
import service.StoreServiceImpl;
import service.SupplierService;
import service.SupplierServiceImpl;

public class PosUI extends JFrame implements SidebarPanel.SidebarListener {

	private final String iconPath = "C:\\TTTN\\POS PROJECT\\img\\";
	private final String[] sidebarIcons = { "home_icon.png", "menue_icon.png", "order_icon.png", "promos_icon.png",
			"logout_icon.png" };
	private final String[] sidebarNames = { "Home", "Menue", "OrderHistory", "Promotion", "Logout" };
	private JPanel contentPanel;
	private final String username; // Replace with actual username
	private final ImageIcon logoIcon = new ImageIcon("C:\\TTTN\\POS PROJECT\\img\\lck.png");

	// Services required for MenuPanel
	private PersonService personService;
	private CategoryService categoryService;
	private ProductService productService;
	private PaymentService paymentService;
	private OrderService orderService;
	private InvoiceService invoiceService;

	public PosUI(String username) {
		this.username = username;
		initializeServices();

		setTitle("Cà phê lck");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		setLayout(new BorderLayout());

		contentPanel = new JPanel(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);

		SidebarPanel sidebar = new SidebarPanel(sidebarIcons, sidebarNames, iconPath, username, this);
		add(sidebar, BorderLayout.WEST);

		// Load default panel (e.g., Home)
		loadMenuePanel();
		add(contentPanel, BorderLayout.CENTER);
		setVisible(true);
	}

	private void initializeServices() {
		try {
			// Initialize DAOs
			EmployeeDao employeeDao = new EmployeeDao();
			OwnerDao ownerDao = new OwnerDao();
			CustomerDao customerDao = new CustomerDao();
			SupplierDao supplierDao = new SupplierDao();
			ItemDao itemDao = new ItemDao();
			StoreDao storeDao = new StoreDao();
			UserSessionDao userSessionDao = new UserSessionDao();
			CategoryDao categoryDao = new CategoryDao();
			ProductDao productDao = new ProductDao();
			PaymentDao paymentDao = new PaymentDao();
			InvoiceDao invoiceDao = new InvoiceDao();
			OrderDetailDao orderDao = new OrderDetailDao();

			// Initialize services
			personService = new PersonServiceImpl(employeeDao, customerDao, ownerDao);
			SupplierService supplierService = new SupplierServiceImpl(supplierDao);
			ItemService itemService = new ItemServiceImpl(itemDao);
			StoreServiceImpl storeService = new StoreServiceImpl(storeDao);
			HashService hashService = new HashService();
			AuthenticationService authService = new AuthenticationService(employeeDao, ownerDao, userSessionDao,
					hashService);
			categoryService = new CategoryServiceImpl(categoryDao);
			productService = new ProductServiceImpl(productDao);
			paymentService = new PaymentServiceImpl(paymentDao);
			orderService = new OrderServiceImpl(orderDao);
			invoiceService = new InvoiceServiceImpl(invoiceDao);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Lỗi khi khởi tạo dịch vụ: " + e.getMessage(), "Lỗi",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void onSidebarItemClick(String pageName) {
		contentPanel.removeAll();
		switch (pageName) {
		case "Home":
			loadProfilePanel();
			break;
		case "Menue":
			loadMenuePanel();
			break;
		case "OrderHistory":
			loadOrderPanel();
			break;
		case "Promotion":
			loadPromotionPanel();
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
			Employee employee = personService.getEmployeeByUsername(username);
			if (employee == null) {
				JOptionPane.showMessageDialog(this, "Owner not found for username: " + username, "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			ProfilePanel profilePanel = new ProfilePanel(personService);
			profilePanel.setPerson(employee);
			contentPanel.add(profilePanel, BorderLayout.CENTER);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error loading profile: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void loadMenuePanel() {
		MenuPanel menuPanel = new MenuPanel(categoryService, productService, personService, paymentService,
				orderService, invoiceService, false);
		contentPanel.add(menuPanel, BorderLayout.CENTER);
	}

	private void loadOrderPanel() {
		JPanel orderPanel = new JPanel(new BorderLayout());
		orderPanel.setBackground(Color.WHITE);
		orderPanel.add(new JLabel("Order History Page (Under Construction)", SwingConstants.CENTER));
		contentPanel.add(orderPanel, BorderLayout.CENTER);
	}

	private void loadPromotionPanel() {
		JPanel promotionPanel = new JPanel(new BorderLayout());
		promotionPanel.setBackground(Color.WHITE);
		promotionPanel.add(new JLabel("Promotion Page (Under Construction)", SwingConstants.CENTER));
		contentPanel.add(promotionPanel, BorderLayout.CENTER);
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
//                new PosUI();
//            } catch (Exception e) {
//                e.printStackTrace();
//                JOptionPane.showMessageDialog(null, "Lỗi khi khởi chạy ứng dụng: " + e.getMessage(), "Lỗi",
//                        JOptionPane.ERROR_MESSAGE);
//            }
//        });
//    }
}