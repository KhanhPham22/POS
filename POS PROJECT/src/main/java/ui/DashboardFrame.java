package ui;

import javax.swing.*;
import java.awt.*;
import model.Owner;
import service.*;
import ui.Elements.SidebarPanel;

public class DashboardFrame extends JFrame implements SidebarPanel.SidebarListener {
    private JPanel contentPanel;
    private final String iconPath = "C:\\TTTN\\POS PROJECT\\img\\";
    private final String[] sidebarIcons = { "home_icon.png", "customers.png", "employee.png", "product.png",
            "dashboard.png", "supplier.png", "warehouse.png", "store.png", "logout_icon.png" };
    private final String[] sidebarNames = { "Home", "Customer", "Employee", "Product", "Dashboard", "Supplier",
            "Warehouse", "Store", "Logout" };
    private final String username;
    private final ImageIcon logoIcon = new ImageIcon("C:\\TTTN\\POS PROJECT\\img\\lck.png");

    private final SupplierService supplierService;
    private final ItemService itemService;
    private final StoreServiceImpl storeService; // Changed to StoreServiceImpl
    private final PersonService personService;
    private final HashService hashService;
    private final AuthenticationService authService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final DashboardService dashboardService;

    public DashboardFrame(SupplierService supplierService, ItemService itemService, StoreServiceImpl storeService,
            PersonService personService, HashService hashService, AuthenticationService authService,
            ProductService productService, CategoryService categoryService, DashboardService dashboardService,
            String username) {
        this.supplierService = supplierService;
        this.itemService = itemService;
        this.storeService = storeService;
        this.personService = personService;
        this.hashService = hashService;
        this.authService = authService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.dashboardService = dashboardService;
        this.username = username;

        setTitle("Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        SidebarPanel sidebar = new SidebarPanel(sidebarIcons, sidebarNames, iconPath, username, this);
        add(sidebar, BorderLayout.WEST);

        loadDashboardPanel();
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
                productService, categoryService,dashboardService, username).setVisible(true);
        dispose();
    }

    private void openEmployeeManager() {
        new EmployeeManager(personService, supplierService, itemService, storeService, hashService, authService,
                productService, categoryService,dashboardService, username).setVisible(true);
        dispose();
    }

    private void openProductFrame() {
        ProductFrame productFrame = new ProductFrame(personService, supplierService, itemService, storeService,
                hashService, authService, productService, categoryService,dashboardService, username);
        productFrame.setVisible(true);
        dispose();
    }

    private void loadDashboardPanel() {
        DashboardPanel dashboardPanel = new DashboardPanel(dashboardService, personService, productService);
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
        StoreFrame storeFrame = new StoreFrame(supplierService, itemService, storeService, personService, hashService,
                authService, productService, categoryService,dashboardService, username);
        storeFrame.setVisible(true);
        dispose();
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.showFrame();
            });
        }
    }
}