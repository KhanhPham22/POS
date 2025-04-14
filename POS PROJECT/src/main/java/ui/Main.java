package ui;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import model.Employee;
import dao.EmployeeDao;
import service.PersonServiceImpl;
import util.HibernateUtil;
public class Main {
    public static void main(String[] args) {
        try {
            // Tạo đối tượng DAO
            EmployeeDao employeeDao = new EmployeeDao();
            employeeDao.setClass(Employee.class);

            // Tạo mock employee
            Employee employee = new Employee();
            employee.setPersonFirstName("Khanh");
            employee.setPersonMiddleName("Gia");
            employee.setPersonLastName("Pham");
            employee.setPersonGender("Male");
            employee.setDateOfBirth("2000-01-01");
            employee.setPhone("0123456789");
            employee.setEmail("khanh@example.com");
            employee.setAddress("123 Đường ABC");
            employee.setCity("HCM");
            employee.setState("District 1");
            employee.setLoginUsername("khanh123");
            employee.setLoginPassword("secret");
            employee.setEmployeeType("Full-time");
            employee.setDescription("Nhân viên test");
            employee.generateEmployeeNumber(); // Tạo số hiệu nhân viên

            // Gọi hàm tạo employee
            boolean created = employeeDao.create(employee);
            System.out.println("Tạo employee thành công: " + created);

            // Lấy employee theo username
            Employee found = employeeDao.findByUsername("khanh123");
            if (found != null) {
                System.out.println("Tìm thấy employee: " + found.getPersonFirstName() + " " + found.getPersonLastName());
            } else {
                System.out.println("Không tìm thấy employee theo username");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

