package ui;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import model.Employee;
import util.HibernateUtil;

public class Main {

    public static void main(String[] args) {
        // Lấy session từ Hibernate
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction transaction = null;

        try {
            // Bắt đầu transaction
            transaction = session.beginTransaction();

            // Mock data using array without constructor
            Employee[] employees = new Employee[3];

            // Tạo đối tượng Employee sử dụng setter tự động từ Lombok
			/*
			 * employees[0] = new Employee(); employees[0].setName("Nguyễn Văn A");
			 * employees[0].setEmail("a@example.com"); employees[0].setPassword("123456");
			 * employees[0].setGender("Male"); employees[0].setAge(30);
			 * employees[0].setNational("Vietnam"); employees[0].setAddress("Hà Nội");
			 * 
			 * employees[1] = new Employee(); employees[1].setName("Trần Thị B");
			 * employees[1].setEmail("b@example.com"); employees[1].setPassword("abcdef");
			 * employees[1].setGender("Female"); employees[1].setAge(28);
			 * employees[1].setNational("Vietnam"); employees[1].setAddress("Hồ Chí Minh");
			 * 
			 * employees[2] = new Employee(); employees[2].setName("Lê Văn C");
			 * employees[2].setEmail("c@example.com"); employees[2].setPassword("qwerty");
			 * employees[2].setGender("Male"); employees[2].setAge(25);
			 * employees[2].setNational("Vietnam"); employees[2].setAddress("Đà Nẵng");
			 */
            // Persist mock data
            for (Employee emp : employees) {
                session.save(emp); // Sử dụng Hibernate để lưu đối tượng Employee
            }

            // Commit the transaction
            transaction.commit();

            System.out.println("Inserted employees into the database!");

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
