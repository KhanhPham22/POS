package util;

import dao.CategoryDao;
import dao.CustomerDao;
import dao.DashboardDao;
import dao.EmployeeDao;
import dao.FeedbackDao;
import dao.GiftVoucherDao;
import dao.InvoiceDao;
import dao.ItemDao;
import dao.OrderDetailDao;
import dao.OwnerDao;
import dao.PaymentDao;
import dao.PayrollDao;
import dao.PersonDao;
import dao.ProductDao;
import dao.StoreDao;
import dao.SupplierDao;
import dao.UserSessionDao;
import dao.WarehouseDao;
import service.AuthenticationService;
import service.CategoryService;
import service.CategoryServiceImpl;
import service.DashboardService;
import service.DashboardServiceImpl;
import service.FeedbackService;
import service.FeedbackServiceImpl;
import service.GiftVoucherService;
import service.GiftVoucherServiceImpl;
import service.HashService;
import service.InvoiceService;
import service.InvoiceServiceImpl;
import service.ItemService;
import service.ItemServiceImpl;
import service.OrderService;
import service.OrderServiceImpl;
import service.PaymentService;
import service.PaymentServiceImpl;
import service.PayrollService;
import service.PayrollServiceImpl;
import service.PersonService;
import service.PersonServiceImpl;
import service.ProductService;
import service.ProductServiceImpl;
import service.StoreService;
import service.StoreServiceImpl;
import service.SupplierService;
import service.SupplierServiceImpl;
import service.UserSessionService;
import service.UserSessionServiceImpl;
import service.WarehouseService;
import service.WarehouseServiceImpl;

public class Injector {
	private static EmployeeDao employeeDao;
	private static CategoryDao categoryDao;
	private static DashboardDao dashboardDao;
	private static FeedbackDao feedbackDao;
	private static GiftVoucherDao giftvoucherDao;
	private static OrderDetailDao orderdetailDao;
	private static OwnerDao ownerDao;
	private static PayrollDao payrollDao;
	private static PaymentDao paymentDao;
	private static ProductDao productDao;
	private static StoreDao storeDao;
	private static SupplierDao supplierDao;
	private static CustomerDao customerDao;
	private static InvoiceDao invoiceDao;
	private static ItemDao itemDao;
	private static PersonDao personDao;
	private static UserSessionDao userSessionDao;
	private static WarehouseDao warehouseDao;
	
	public static EmployeeDao getEmployeeDao() {
		if (employeeDao == null) {
			employeeDao = new EmployeeDao();
		}
		return employeeDao;
	}
	
	public static PaymentDao getPaymentDao() {
		if (paymentDao == null) {
			paymentDao = new PaymentDao();
		}
		return paymentDao;
	}
	
	public static CategoryDao getCategoryDao() {
		if (categoryDao == null) {
			categoryDao = new CategoryDao();
		}
		return categoryDao;
	}
	
	public static ProductDao getProductDao() {
		if (productDao == null) {
			productDao = new ProductDao();
		}
		return productDao;
	}
	
	public static FeedbackDao getFeedbackDao() {
		if (feedbackDao == null) {
			feedbackDao = new FeedbackDao();
		}
		return feedbackDao;
	}
	
	public static PayrollDao getPayrollDao() {
		if (payrollDao == null) {
			payrollDao = new PayrollDao();
		}
		return payrollDao;
	}
	
	public static SupplierDao getSupplierDao() {
		if (supplierDao == null) {
			supplierDao = new SupplierDao();
		}
		return supplierDao;
	}
	
	public static DashboardDao getDashboardDao() {
		if (dashboardDao == null) {
			dashboardDao = new DashboardDao();
		}
		return dashboardDao;
	}
	
	public static OwnerDao getOwnerDao() {
		if (ownerDao == null) {
			ownerDao = new OwnerDao();
		}
		return ownerDao;
	}
	
	public static CustomerDao getCustomerDao() {
		if (customerDao == null) {
			customerDao = new CustomerDao();
		}
		return customerDao;
	}
	
	public static PersonDao getPersonDao() {
		if (personDao == null) {
			personDao = new PersonDao();
		}
		return personDao;
	}
	
	public static StoreDao getStoreDao() {
		if (storeDao == null) {
			storeDao = new StoreDao();
		}
		return storeDao;
	}
	
	public static GiftVoucherDao getGiftVoucherDao() {
		if (giftvoucherDao == null) {
			giftvoucherDao = new GiftVoucherDao();
		}
		return giftvoucherDao;
	}
	
	public static InvoiceDao getInvoiceDao() {
		if (invoiceDao == null) {
			invoiceDao = new InvoiceDao();
		}
		return invoiceDao;
	}
	
	public static ItemDao getItemDao() {
		if (itemDao == null) {
			itemDao = new ItemDao();
		}
		return itemDao;
	}
	
	public static OrderDetailDao getOrderDetailDao() {
		if (orderdetailDao == null) {
			orderdetailDao = new OrderDetailDao();
		}
		return orderdetailDao;
	}
	
	public static UserSessionDao getUserSessionDao() {
		if (userSessionDao == null) {
			userSessionDao = new UserSessionDao();
		}
		return userSessionDao;
	}
	
	public static WarehouseDao getWarehouseImportDao() {
		if (warehouseDao == null) {
			warehouseDao = new WarehouseDao();
		}
		return warehouseDao;
	}
	
	public static AuthenticationService createAuthenticationService() {
	    return new AuthenticationService(getEmployeeDao(), getUserSessionDao(), new HashService());
	}

	
	public static PaymentService createPaymentService() {
		return new PaymentServiceImpl(getPaymentDao());
	}
	
	public static PayrollService createPayrollService() {
		return new PayrollServiceImpl(getPayrollDao());
	}
	
	public static DashboardService createDashboardService() {
		return new DashboardServiceImpl(getDashboardDao());
	}
	
	public static FeedbackService createFeedbackService() {
		return new FeedbackServiceImpl(getFeedbackDao());
	}
	
	public static OrderService createOrderService() {
		return new OrderServiceImpl(getOrderDetailDao());
	}
	
	public static GiftVoucherService createGiftVoucherService() {
		return new GiftVoucherServiceImpl(getGiftVoucherDao());
	}
	
	public static CategoryService createCategoryService() {
		return new CategoryServiceImpl(getCategoryDao());
	}
	
	public static ProductService createProductService() {
		return new ProductServiceImpl(getProductDao());
	}
	
	public static InvoiceService createInvoiceService() {
		return new InvoiceServiceImpl(getInvoiceDao());
	}

	public static ItemService createItemService() {
		return new ItemServiceImpl(getItemDao());
	}
	
	public static PersonService createPersonService() {
		return new PersonServiceImpl(getEmployeeDao(), getCustomerDao(), getOwnerDao());
	}
	
	public static StoreService createStoreService() {
		return new StoreServiceImpl(getStoreDao());
	}
	
	public static SupplierService createSupplierService() {
		return new SupplierServiceImpl(getSupplierDao());
	}
	
	public static UserSessionService createUserSessionService() {
		return new UserSessionServiceImpl(getUserSessionDao());
	}
	
	public static WarehouseService createWarehouseService() {
		return new WarehouseServiceImpl(getWarehouseImportDao());
	}
}
