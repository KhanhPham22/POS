package ui;

import javax.swing.*;

import dao.ItemDao;
import dao.PersonDao;
import dao.StoreDao;
import dao.SupplierDao;
import model.Item;
import model.Store;
import model.Supplier;
import service.ItemService;
import service.ItemServiceImpl;
import service.PersonServiceImpl;
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
    private final String username = "admin"; // Replace with actual username
    private final ImageIcon logoIcon = new ImageIcon("C:\\TTTN\\POS PROJECT\\img\\lck.png");
    private SupplierService supplierService;
    private ItemService itemService;
    private StoreServiceImpl storeService;

    public StoreFrame(SupplierService supplierService, ItemService itemService, StoreServiceImpl storeService) {
        this.supplierService = supplierService;
        this.itemService = itemService;
        this.storeService = storeService;
        setTitle("Quản lý cửa hàng");
        setSize(800, 600);
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
                loadHomePanel();
                break;
            case "Customer":
                loadCustomerPanel();
                break;
            case "Employee":
            	openEmployeeManager();
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

    private void openEmployeeManager() {
    	//new EmployeeManager(new PersonServiceImpl(new PersonDao()));
        dispose(); // đóng SupplierFrame
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

    private void openSupplierFrame() {
        new SupplierFrame(supplierService, itemService);
        dispose(); // Close current frame
    }

    private void loadWarehousePanel() {
        JPanel warehousePanel = new JPanel(new BorderLayout());
        warehousePanel.setBackground(Color.WHITE);
        warehousePanel.add(new JLabel("Warehouse Page (Under Construction)", SwingConstants.CENTER));
        contentPanel.add(warehousePanel, BorderLayout.CENTER);
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            dispose(); // Close the frame (or redirect to login screen)
        }
    }

    private void addInfoRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, String valueText) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        panel.add(new JLabel(valueText != null ? valueText : ""), gbc);
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            SupplierDao supplierDao = new SupplierDao();
//            supplierDao.setClass(Supplier.class);
//
//            ItemDao itemDao = new ItemDao();
//            itemDao.setClass(Item.class);
//
//            StoreDao storeDao = new StoreDao();
//            storeDao.setClass(Store.class);
//
//            SupplierService supplierService = new SupplierServiceImpl(supplierDao);
//            ItemService itemService = new ItemServiceImpl(itemDao);
//            StoreServiceImpl storeService = new StoreServiceImpl(storeDao);
//
//            StoreFrame frame = new StoreFrame(supplierService, itemService, storeService);
//            frame.setVisible(true);
//        });
//    }


}
