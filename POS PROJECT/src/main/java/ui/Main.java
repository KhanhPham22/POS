package ui;

import java.awt.EventQueue;
import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import model.Employee;
import model.Item;
import model.Owner;
import model.Customer;
import model.Category;
import model.Product;
import model.Store;
import model.Supplier;
import dao.CategoryDao;
import dao.ProductDao;
import dao.CustomerDao;
import dao.EmployeeDao;
import dao.ItemDao;
import dao.OwnerDao;
import dao.StoreDao;
import dao.SupplierDao;
import service.PersonServiceImpl;
import service.ProductServiceImpl;
import service.StoreService;
import service.CategoryServiceImpl;
import service.HashService;
import service.ItemServiceImpl;
import service.StoreServiceImpl;
import service.SupplierServiceImpl;
import util.HibernateUtil;

public class Main {
	public static void main(String[] args) {

		// Chạy mock data trước khi hiển thị giao diện
		Main.mockData();

		// Chạy UI ở luồng giao diện
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// Hiển thị màn hình đăng nhập
					new LoginFrame();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void mockData() {
		try {
			// Khởi tạo DAO
//			EmployeeDao employeeDao = new EmployeeDao();
//			employeeDao.setClass(Employee.class);
//
//			OwnerDao ownerDao = new OwnerDao();
//			ownerDao.setClass(Owner.class);
//			
//			ItemDao itemDao = new ItemDao();
//			itemDao.setClass(Item.class);
//
//			SupplierDao supplierDao = new SupplierDao();
//			supplierDao.setClass(Supplier.class);

			StoreDao storeDao = new StoreDao();
			storeDao.setClass(Store.class);

			// Bỏ qua customer
//			PersonServiceImpl personService = new PersonServiceImpl(employeeDao, null, ownerDao);
//
//			HashService hashService = new HashService(); // Để băm password
//			
//			SupplierServiceImpl supplierService = new SupplierServiceImpl(supplierDao);
//			ItemServiceImpl itemService = new ItemServiceImpl(itemDao);
			StoreService storeService = new StoreServiceImpl(storeDao);
			// Supplier
//			Supplier supplier = new Supplier();
//			supplier.setName("Phan cung vn");
//			supplier.setContactName("nhap phan cung");
//			supplier.setPhone("111");
//			supplier.setEmail("phancung@example.com");
//			supplier.setAddress("123 duong Dien Bien Phu");
//			supplier.setTaxCode("123");
//			
//			supplierService.createSupplier(supplier);
//			System.out.println("Tao supplier thanh cong");

			// Item
//			Item item = new Item();
//			item.setName("May xay sinh to");
//			item.setType("phan cung");
//			item.setUnit("vnd");
//			item.setDescription("may xay sinh to tu VN");
//			item.setQuantity(1);
//			item.setSupplier(supplier);
//			
//			itemService.createItem(item);
//			System.out.println("tao item thanh cong");

			// ===== Tạo và test EMPLOYEE =====
//			Employee employee = new Employee();
//			employee.setPersonFirstName("Tran");
//			employee.setPersonMiddleName("Quang");
//			employee.setPersonLastName("Tien");
//			employee.setPersonGender("Male");
//			employee.setDateOfBirth("2003-03-09");
//			employee.setPhone("0123456789");
//			employee.setEmail("tranquangtien@example.com");
//			employee.setAddress("333 Đường ABCD");
//			employee.setCity("HCM");
//			employee.setState("District 1");
//			employee.setCountry("Vietnam");
//			employee.setLoginUsername("tranquangtien");
//			employee.setLoginPassword(hashService.hash("tien123")); // ✅ hash password
//			employee.setEmployeeType("Full-time");
//			employee.setDescription("tiến học lập trình game bắn zombie ");
//			employee.generateEmployeeNumber();
//
//			personService.createEmployee(employee);
//			System.out.println("✅ Tạo employee thành công!");
//
//			Employee found = personService.getEmployeeByUsername("tranquangtien");
//			if (found != null) {
//				System.out.println(
//						"🔍 Tìm thấy employee: " + found.getPersonFirstName() + " " + found.getPersonLastName());
//			} else {
//				System.out.println("❌ Không tìm thấy employee theo username");
//			}
//
//			// ===== Tạo và test OWNER =====
//			Owner owner = new Owner();
//			owner.setPersonFirstName("Rom");
//			owner.setPersonMiddleName("Rom");
//			owner.setPersonLastName("Mu");
//			owner.setPersonGender("Male");
//			owner.setDateOfBirth("1999-08-15");
//			owner.setPhone("0112233445");
//			owner.setEmail("rommu@example.com");
//			owner.setAddress("999 đường quang tiến");
//			owner.setCity("Ho Chi Minh");
//			owner.setState("Gò vấp");
//			owner.setCountry("Vietnam");
//			owner.setLoginUsername("rommu");
//			owner.setLoginPassword(hashService.hash("rommu123")); // ✅ hash password
//			owner.setDescription("Chủ cửa hàng");
//			owner.generateOwnerId();
//
//			personService.createOwner(owner);
//			System.out.println("✅ Tạo owner thành công!");
//
//			Owner foundOwner = personService.getOwnerByUsername("rommu");
//			if (foundOwner != null) {
//				System.out.println(
//						"🔍 Tìm thấy owner: " + foundOwner.getPersonFirstName() + " " + foundOwner.getPersonLastName());
//			} else {
//				System.out.println("❌ Không tìm thấy owner theo username");
//			}

			// Tạo và test Store
			Store store = new Store();
			store.setName("Cà phê LCK quận 12");
			store.setShortName("CF LCK Q12");
			store.setDescription("CF SỐ 1 QUẬN 12");
			store.setAddress("Đường số 3 công viên phần mềm quang trung");
			store.setCity("Ho Chi Minh");
			store.setState("Quan 12");
			store.setZip("7777");
			store.setEmail("lckcoffeeq12@example.com");
			store.setPhone("0987654321");
			store.setWebsite("www.lckcoffee.com");
			store.setFax("2222");

			storeService.createStore(store);
			System.out.println("Tạo store thành công");

			// Test hàm tìm kiếm store
			List<Store> foundStores = storeService.getStoresByName("Cà phê LCK quận 12");
			if (foundStores != null && !foundStores.isEmpty()) {
				Store firstStore = foundStores.get(0);
				System.out.println("Thông tin chi tiết store: " + firstStore.getDescription() + " | SĐT: "
						+ firstStore.getPhone());
			} else {
				System.out.println("Không tìm thấy cửa hàng");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
