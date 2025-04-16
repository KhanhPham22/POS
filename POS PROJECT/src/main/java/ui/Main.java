package ui;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import model.Employee;
import model.Owner;
import model.Customer;
import model.Category;
import model.Product;
import dao.CategoryDao;
import dao.ProductDao;
import dao.CustomerDao;
import dao.EmployeeDao;
import dao.OwnerDao;
import service.PersonServiceImpl;
import service.ProductServiceImpl;
import service.CategoryServiceImpl;
import util.HibernateUtil;

public class Main {
    public static void main(String[] args) {
    	try {
    	    // Khởi tạo DAO cho Employee
    	    EmployeeDao employeeDao = new EmployeeDao();
    	    employeeDao.setClass(Employee.class);

    	    CustomerDao customerDao = new CustomerDao();
    	    customerDao.setClass(Customer.class);

    	    OwnerDao ownerDao = new OwnerDao();
    	    ownerDao.setClass(Owner.class);

    	    
    	    PersonServiceImpl personService = new PersonServiceImpl(employeeDao, customerDao, ownerDao);

    	    // Tạo mock employee
    	    Employee employee = new Employee();
    	    employee.setPersonFirstName("Rom");
    	    employee.setPersonMiddleName("Rom");
    	    employee.setPersonLastName("Mu");
    	    employee.setPersonGender("Male");
    	    employee.setDateOfBirth("2008-01-01");
    	    employee.setPhone("0123456789");
    	    employee.setEmail("rom@example.com");
    	    employee.setAddress("123 Đường ABCD");
    	    employee.setCity("HCM");
    	    employee.setState("District 3");
    	    employee.setCountry("Vietnam");
    	    employee.setLoginUsername("rom123");
    	    employee.setLoginPassword("rom");
    	    employee.setEmployeeType("Part-time");
    	    employee.setDescription("Nhân viên word đâu");
    	    employee.generateEmployeeNumber(); // Tạo số hiệu nhân viên

    	    // Gọi service để tạo employee
    	    personService.createEmployee(employee);
    	    System.out.println("Tạo employee thành công!");

    	    // Gọi service để lấy employee theo username
    	    Employee found = personService.getEmployeeByUsername("rom123");
    	    if (found != null) {
    	        System.out.println("Tìm thấy employee: " + found.getPersonFirstName() + " " + found.getPersonLastName());
    	    } else {
    	        System.out.println("Không tìm thấy employee theo username");
    	    }
    	    
    	    // ===== Tạo và test CUSTOMER =====
    	    Customer customer = new Customer();
    	    customer.setPersonFirstName("Nguyen");
    	    customer.setPersonMiddleName("Thi");
    	    customer.setPersonLastName("Na");
    	    customer.setPersonGender("Female");
    	    customer.setDateOfBirth("1997-05-21");
    	    customer.setPhone("0987654321");
    	    customer.setEmail("linh@example.com");
    	    customer.setAddress("456 Đường XYZ");
    	    customer.setCity("Hanoi");
    	    customer.setState("Ba Dinh");
    	    customer.setCountry("Vietnam");
    	    customer.setDescription("Khách hàng thân thiết");
    	    customer.setPoints(11.0);
    	    
    	    personService.createCustomer(customer);
    	    System.out.println("Tạo customer thành công!");

    	    Customer foundCustomer = personService.getCustomerByNameandPhone("Na", "0987654321");
    	    if (foundCustomer != null) {
    	        System.out.println("Tìm thấy customer: " + foundCustomer.getPersonFirstName() + " " + foundCustomer.getPersonLastName());
    	    } else {
    	        System.out.println("Không tìm thấy customer theo tên và số điện thoại");
    	    }
    	    
    	 // ===== Tạo và test OWNER =====
    	    Owner owner = new Owner();
    	    owner.setPersonFirstName("Huynh");
    	    owner.setPersonMiddleName("Minh");
    	    owner.setPersonLastName("Quang");
    	    owner.setPersonGender("Male");
    	    owner.setDateOfBirth("1980-03-15");
    	    owner.setPhone("0112233445");
    	    owner.setEmail("minh@example.com");
    	    owner.setAddress("789 Đường DEF");
    	    owner.setCity("Da Nang");
    	    owner.setState("Hai Chau");
    	    owner.setCountry("Vietnam");
    	    owner.setLoginUsername("minhowner");
    	    owner.setLoginPassword("admin123");
    	    owner.setDescription("Chủ cửa hàng");
    	    owner.generateOwnerId();
    	    
    	    personService.createOwner(owner);
    	    System.out.println("Tạo owner thành công!");

    	    Owner foundOwner = personService.getOwnerByUsername("minhowner");
    	    if (foundOwner != null) {
    	        System.out.println("Tìm thấy owner: " + foundOwner.getPersonFirstName() + " " + foundOwner.getPersonLastName());
    	    } else {
    	        System.out.println("Không tìm thấy owner theo username");
    	    }
    	    

    	} catch (Exception e) {
    	    e.printStackTrace();
    	}

        }
    }


