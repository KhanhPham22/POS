package ui;

import javax.swing.*;
import java.awt.*;
import ui.Elements.SidebarPanel;
import dao.CategoryDao;
import dao.CustomerDao;
import dao.EmployeeDao;
import dao.ItemDao;
import dao.OwnerDao;
import dao.PaymentDao;
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
import service.ItemService;
import service.ItemServiceImpl;
import service.PaymentService;
import service.PaymentServiceImpl;
import service.PersonService;
import service.PersonServiceImpl;
import service.ProductService;
import service.ProductServiceImpl;
import service.StoreServiceImpl;
import service.SupplierService;
import service.SupplierServiceImpl;

public class HomeEmployeeFrame extends JFrame implements SidebarPanel.SidebarListener {

    private final String iconPath = "C:\\TTTN\\POS PROJECT\\img\\";
    private final String[] sidebarIcons = { "home_icon.png", "menue_icon.png", "order_icon.png", "promos_icon.png",
            "logout_icon.png" };
    private final String[] sidebarNames = { "Home", "Menu", "OrderHistory", "Promotion", "Logout" };
    private JPanel contentPanel;
    private final String username;
    private final ImageIcon logoIcon = new ImageIcon("C:\\TTTN\\POS PROJECT\\img\\lck.png");

    // Services required for panels
    private PersonService personService;
    private CategoryService categoryService;
    private ProductService productService;
    private PaymentService paymentService;
    private SupplierService supplierService;
    private ItemService itemService;
    private StoreServiceImpl storeService;
    private HashService hashService;
    private AuthenticationService authService;

    public HomeEmployeeFrame(PersonService personService, SupplierService supplierService, ItemService itemService,
			StoreServiceImpl storeService, HashService hashService, AuthenticationService authService,
			ProductService productService, CategoryService categoryService,String username,PaymentService paymentService) {
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

        // Load ProfilePanel by default
        loadProfilePanel();
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
        case "Menu":
            loadMenuPanel();
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

    

    private void loadMenuPanel() {
        MenuPanel menuPanel = new MenuPanel(categoryService, productService, personService, paymentService);
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
}