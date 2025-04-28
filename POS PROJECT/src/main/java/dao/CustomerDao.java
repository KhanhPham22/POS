
package dao;

import java.io.Serializable;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.BeanUtils;

import model.Customer;
import util.HibernateUtil;

public class CustomerDao implements GenericDao<Customer> {
	
	private static final Logger Log = LogManager.getLogger(CustomerDao.class);

	private SessionFactory sessionFactory;

	private Class<Customer> Customer;

	public CustomerDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public void setClass(Class<Customer> Customer) {
		this.Customer = Customer;
	}

	public boolean create(Customer Customer) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.save(Customer);
			transaction.commit();
			Log.info("Customer persisted in database successfully");
			return true;
		} catch (Exception e) {
			Log.error("Database error while persisting Customer", e);
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		} finally {
			session.close();
		}
	}
	
	@Override
	public Customer findById(long id) throws Exception {
		Session session = sessionFactory.openSession();
		try {
			Customer Customer = session.get(Customer.class, id);
			Log.info("Customer with id: " + id + " retrieved successfully from database");
			return Customer;
		} catch (Exception e) {
			Log.error("Database error while retrieving Customer with id:" + id, e);
			throw e;
		} finally {
			session.close();
		}

	}

	@Override
	public List<Customer> findAll(int pageNumber, int pageSize) throws Exception {
	    Session session = null;
	    try {
	        session = sessionFactory.openSession();

	        // Tính toán offset dựa trên pageNumber và pageSize
	        int offset = (pageNumber - 1) * pageSize; // Lưu ý pageNumber bắt đầu từ 1

	        // Sử dụng HQL để lấy tất cả các Customer, và áp dụng phân trang
	        List<Customer> customers = session.createQuery("from Customer", Customer.class)
	                                          .setFirstResult(offset)  // Thiết lập vị trí bắt đầu
	                                          .setMaxResults(pageSize) // Thiết lập số lượng bản ghi mỗi trang
	                                          .list();

	        Log.info("All Customers retrieved successfully from database. Total count: " + customers.size());
	        return customers;
	    } catch (Exception e) {
	        Log.error("Error while retrieving all Customers with pagination from database", e);
	        throw e;
	    } finally {
	        if (session != null)
	            session.close();
	    }
	}


	@Override
	public boolean update(Customer customer) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.update(customer);
			transaction.commit();
			Log.info("Customer with id: "+customer.getPersonId()+" updated in database successfully");
			return true;
		} catch (Exception e) {
			Log.error("Database error while updating Customer with id: "+customer.getPersonId(), e);
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public boolean deleteById(long id) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Customer Customer = session.get(Customer.class, id);
			session.delete(Customer);
			transaction.commit();
			Log.info("Customer with id: " + id + " deleted from database successfully");
			return true;
		} catch (Exception e) {
			Log.error("Database error while deleting Customer with id: " + id, e);
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public boolean delete(Customer Customer) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.delete(Customer);
			transaction.commit();
			Log.info("Customer with id: " + Customer.getPersonId() + " deleted from database successfully");
			return true;
		} catch (Exception e) {
			Log.error("Database error while deleting Customer with id: " + Customer.getPersonId(), e);
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		} finally {
			session.close();
		}
	}
	
	public Customer findByNameAndPhone(String name, String phone) throws Exception {
		Session session = sessionFactory.openSession();
		try {
			String hql = "FROM Customer WHERE personFirstName = :name AND phone = :phone";
			Customer customer = session.createQuery(hql, Customer.class)
				.setParameter("name", name)
				.setParameter("phone", phone)
				.uniqueResult();
			
			if (customer != null) {
				Log.info("Customer with name: " + name + " and phone: " + phone + " retrieved successfully from database");
			} else {
				Log.info("No Customer found with name: " + name + " and phone: " + phone);
			}
			return customer;
		} catch (Exception e) {
			Log.error("Database error while retrieving Customer with name: " + name + " and phone: " + phone, e);
			throw e;
		} finally {
			session.close();
		}
	}

}
