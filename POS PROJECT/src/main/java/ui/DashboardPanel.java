package ui;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import service.DashboardService;
import service.InvoiceService;
import service.PersonService;
import service.ProductService;
import model.Dashboard;
import model.Invoice;

public class DashboardPanel extends JPanel {
	private final DashboardService dashboardService;
	private final PersonService personService;
	private final ProductService productService;
	private final InvoiceService invoiceService;
	private final DecimalFormat currencyFormat = new DecimalFormat("#,###.000 VND");

	public DashboardPanel(DashboardService dashboardService, PersonService personService, ProductService productService,
			InvoiceService invoiceService) {
		this.dashboardService = dashboardService;
		this.personService = personService;
		this.productService = productService;
		this.invoiceService = invoiceService; // Khởi tạo
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		initializeUI();
	}

	private void initializeUI() {
		// Title Panel
		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(Color.WHITE);
		titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		JLabel titleLabel = new JLabel("Dashboard Overview");
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
		titlePanel.add(titleLabel);
		add(titlePanel, BorderLayout.NORTH);

		// Metrics Panel
		JPanel metricsPanel = new JPanel(new GridLayout(1, 4, 20, 20));
		metricsPanel.setBackground(Color.WHITE);
		metricsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		// Total Revenue
		JPanel revenuePanel = createMetricPanel("Total Revenue", getTotalRevenue(), new Color(46, 204, 113));
		metricsPanel.add(revenuePanel);

		// Total Customers
		JPanel customersPanel = createMetricPanel("Total Customers", String.valueOf(getTotalCustomers()),
				new Color(52, 152, 219));
		metricsPanel.add(customersPanel);

		// Total Employees
		JPanel employeesPanel = createMetricPanel("Total Employees", String.valueOf(getTotalEmployees()),
				new Color(155, 89, 182));
		metricsPanel.add(employeesPanel);

		// Total Products
		JPanel productsPanel = createMetricPanel("Total Products", String.valueOf(getTotalProducts()),
				new Color(231, 76, 60));
		metricsPanel.add(productsPanel);

		add(metricsPanel, BorderLayout.CENTER);
	}

	private JPanel createMetricPanel(String title, String value, Color color) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(color, 2),
				BorderFactory.createEmptyBorder(20, 20, 20, 20)));

		JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
		titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
		titleLabel.setForeground(color);
		panel.add(titleLabel, BorderLayout.NORTH);

		JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
		valueLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
		valueLabel.setForeground(Color.BLACK);
		panel.add(valueLabel, BorderLayout.CENTER);

		return panel;
	}

	/**
	 * Calculates and returns the total revenue from all invoices.
	 * It fetches all invoices with pagination (page 1, max size),
	 * sums up the final prices, and formats the result using currency format.
	 * 
	 * @return Formatted total revenue as a string, or "Error" if an exception occurs.
	 */
	private String getTotalRevenue() {
		try {
			// Fetch all invoices from page 1 with maximum number of records.
			List<Invoice> invoices = invoiceService.getAllInvoices(1, Integer.MAX_VALUE);
			
			// Calculate total revenue by summing up all final prices
			BigDecimal totalRevenue = invoices.stream()
				.map(invoice -> BigDecimal.valueOf(invoice.getFinalPrice()))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
			
			return currencyFormat.format(totalRevenue);
		} catch (Exception e) {
			return "Error";
		}
	}

	/**
	 * Returns the total number of customers in the system.
	 * It uses page 1 with maximum size to retrieve all customer records.
	 *
	 * @return Total number of customers, or 0 if an exception occurs.
	 */
	private int getTotalCustomers() {
		try {
			// 1 means page number; Integer.MAX_VALUE means fetch all on one page
			return personService.getAllCustomers(1, Integer.MAX_VALUE).size();
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Returns the total number of employees in the system.
	 * It uses page 1 with maximum size to retrieve all employee records.
	 *
	 * @return Total number of employees, or 0 if an exception occurs.
	 */
	private int getTotalEmployees() {
		try {
			return personService.getAllEmployees(1, Integer.MAX_VALUE).size();
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Returns the total number of products in the system.
	 * It fetches all product records using page 1 and maximum size.
	 *
	 * @return Total number of products, or 0 if an exception occurs.
	 */
	private int getTotalProducts() {
		try {
			return productService.getAllProducts(1, Integer.MAX_VALUE).size();
		} catch (Exception e) {
			return 0;
		}
	}
}