package ui;

import java.awt.EventQueue;
import java.math.BigDecimal;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import model.Employee;
import model.Owner;
import model.Customer;
import model.Category;
import model.Product;
import model.Store;
import dao.CategoryDao;
import dao.ProductDao;
import dao.CustomerDao;
import dao.EmployeeDao;
import dao.OwnerDao;
import dao.StoreDao;
import service.PersonServiceImpl;
import service.ProductServiceImpl;
import service.CategoryServiceImpl;
import service.HashService;
import service.StoreServiceImpl;
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
					LoginFrame window = new LoginFrame();
					window.frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void mockData() {
		try {
			// Khởi tạo DAO
			EmployeeDao employeeDao = new EmployeeDao();
			employeeDao.setClass(Employee.class);

			OwnerDao ownerDao = new OwnerDao();
			ownerDao.setClass(Owner.class);

			// Bỏ qua customer
			PersonServiceImpl personService = new PersonServiceImpl(employeeDao, null, ownerDao);

			HashService hashService = new HashService(); // Để băm password

			// ===== Tạo và test EMPLOYEE =====
			Employee employee = new Employee();
			employee.setPersonFirstName("Tung");
			employee.setPersonMiddleName("Tung");
			employee.setPersonLastName("Sahur");
			employee.setPersonGender("Male");
			employee.setDateOfBirth("2008-03-06");
			employee.setPhone("0123456789");
			employee.setEmail("tungtungsahur@example.com");
			employee.setAddress("123 Đường ABCD");
			employee.setCity("HCM");
			employee.setState("District 3");
			employee.setCountry("Vietnam");
			employee.setLoginUsername("tungtungsahur");
			employee.setLoginPassword(hashService.hash("sahur")); // ✅ hash password
			employee.setEmployeeType("Part-time");
			employee.setDescription("tung tung tung sa ha ru ");
			employee.generateEmployeeNumber();

			personService.createEmployee(employee);
			System.out.println("✅ Tạo employee thành công!");

			Employee found = personService.getEmployeeByUsername("tungtungsahur");
			if (found != null) {
				System.out.println(
						"🔍 Tìm thấy employee: " + found.getPersonFirstName() + " " + found.getPersonLastName());
			} else {
				System.out.println("❌ Không tìm thấy employee theo username");
			}

			// ===== Tạo và test OWNER =====
			Owner owner = new Owner();
			owner.setPersonFirstName("Tran");
			owner.setPersonMiddleName("Minh");
			owner.setPersonLastName("Ly");
			owner.setPersonGender("Male");
			owner.setDateOfBirth("1988-03-15");
			owner.setPhone("0112233445");
			owner.setEmail("lytran@example.com");
			owner.setAddress("789 Đường DEF");
			owner.setCity("Da Nang");
			owner.setState("Hai Chau");
			owner.setCountry("Vietnam");
			owner.setLoginUsername("lytran");
			owner.setLoginPassword(hashService.hash("lytran123")); // ✅ hash password
			owner.setDescription("Chủ cửa hàng");
			owner.generateOwnerId();

			personService.createOwner(owner);
			System.out.println("✅ Tạo owner thành công!");

			Owner foundOwner = personService.getOwnerByUsername("lytran");
			if (foundOwner != null) {
				System.out.println(
						"🔍 Tìm thấy owner: " + foundOwner.getPersonFirstName() + " " + foundOwner.getPersonLastName());
			} else {
				System.out.println("❌ Không tìm thấy owner theo username");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
