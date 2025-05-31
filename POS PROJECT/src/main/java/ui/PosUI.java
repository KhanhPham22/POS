
package ui;

import javax.swing.*;
import java.awt.*;
import ui.Elements.*;

import dao.*;
import model.Employee;
import service.*;

public class PosUI extends JFrame implements SidebarPanel.SidebarListener {

	private final String iconPath = "C:\\TTTN\\POS PROJECT\\img\\";
	private final String[] sidebarIcons = { "home_icon.png", "menue_icon.png", "order_icon.png", "promos_icon.png",
			"logout_icon.png" };
	private final String[] sidebarNames = { "Home", "Menue", "OrderHistory", "Promotion", "Logout" };
	private JPanel contentPanel;
	private final String username;
	private final ImageIcon logoIcon = new ImageIcon("C:\\TTTN\\POS PROJECT\\img\\lck.png");

	// Services required for MenuPanel
	private PersonService personService;
	private CategoryService categoryService;
	private ProductService productService;
	private PaymentService paymentService;
	private OrderService orderService;
	private InvoiceService invoiceService;
	private GiftVoucherService giftVoucherService; // Add field
	private SupplierService supplierService;
	private ItemService itemService;
	private StoreServiceImpl storeService;
	private HashService hashService;
	private AuthenticationService authService;

	// Store MenuePanel to pass to PromotionFrame
	private MenuePanel menuePanel;

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

		// Load default panel (e.g., Menue)
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
			GiftVoucherDao giftVoucherDao = new GiftVoucherDao(); // Add DAO for GiftVoucher

			// Initialize services
			personService = new PersonServiceImpl(employeeDao, customerDao, ownerDao);
			supplierService = new SupplierServiceImpl(supplierDao);
			itemService = new ItemServiceImpl(itemDao);
			storeService = new StoreServiceImpl(storeDao);
			hashService = new HashService();
			authService = new AuthenticationService(employeeDao, ownerDao, userSessionDao, hashService);
			categoryService = new CategoryServiceImpl(categoryDao);
			productService = new ProductServiceImpl(productDao);
			paymentService = new PaymentServiceImpl(paymentDao);
			orderService = new OrderServiceImpl(orderDao);
			invoiceService = new InvoiceServiceImpl(invoiceDao);
			giftVoucherService = new GiftVoucherServiceImpl(giftVoucherDao); // Initialize GiftVoucherService
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
		dispose(); // Close PosUI
		SwingUtilities.invokeLater(() -> new HomeEmployeeFrame(username));
	}

	private void loadMenuePanel() {
		try {
			Employee employee = personService.getEmployeeByUsername(username);
			if (employee == null) {
				JOptionPane.showMessageDialog(this, "Employee not found for username: " + username, "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			menuePanel = new MenuePanel(categoryService, productService, personService, paymentService, orderService,
					invoiceService, giftVoucherService, employee); // Add giftVoucherService
			contentPanel.add(menuePanel, BorderLayout.CENTER);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error loading menu panel: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void loadOrderPanel() {
		dispose(); // Close PosUI
		SwingUtilities.invokeLater(() -> new OrderHistoryFrame(username));
	}

	private void loadPromotionPanel() {
		if (menuePanel == null) {
			JOptionPane.showMessageDialog(this, "Menu panel not loaded. Please load the menu first.", "Error",
					JOptionPane.ERROR_MESSAGE);
			loadMenuePanel();
			return;
		}
		dispose(); // Close PosUI
		SwingUtilities.invokeLater(() -> new PromotionFrame(username, menuePanel));
	}

	private void handleLogout() {
		int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận đăng xuất",
				JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			dispose(); // Close PosUI
			SwingUtilities.invokeLater(() -> {
				LoginFrame loginFrame = new LoginFrame();
				loginFrame.showFrame();
			});
		}
	}
}