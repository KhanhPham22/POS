package ui;

import javax.swing.*;
import java.awt.*;
import ui.Elements.*;
import dao.*;
import model.Employee;
import service.*;

public class OrderHistoryFrame extends JFrame implements SidebarPanel.SidebarListener {
    private static final long serialVersionUID = 1L;
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
    private OrderService orderService;
    private InvoiceService invoiceService;

    public OrderHistoryFrame(String username) {
        this.username = username;
        initializeServices();

        setTitle("Order History - Cà phê lck");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        SidebarPanel sidebar = new SidebarPanel(sidebarIcons, sidebarNames, iconPath, username, this);
        add(sidebar, BorderLayout.WEST);

        // Load OrderPanel by default
        loadOrderPanel();
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
                loadHomePanel();
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

    private void loadHomePanel() {
        dispose(); // Close OrderHistoryFrame
        SwingUtilities.invokeLater(() -> new HomeEmployeeFrame(username));
    }

    private void loadMenuPanel() {
        dispose(); // Close OrderHistoryFrame
        SwingUtilities.invokeLater(() -> new PosUI(username));
    }

    private void loadOrderPanel() {
        try {
            OrderPanel orderPanel = new OrderPanel(orderService);
            contentPanel.add(orderPanel, BorderLayout.CENTER);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading order panel: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadPromotionPanel() {
        dispose(); // Close OrderHistoryFrame
        SwingUtilities.invokeLater(() -> new PromotionFrame(username, null));
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose(); // Close OrderHistoryFrame
            SwingUtilities.invokeLater(() -> {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.showFrame();
            });
        }
    }
}