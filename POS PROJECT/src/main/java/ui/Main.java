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

}
