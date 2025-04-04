
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
	public List<Customer> findAll() throws Exception {
		Session session = sessionFactory.openSession();
		try {
			List<Customer> Customers = session.createQuery("from Customer", Customer.class).list();
			Log.info("All Customers retrieved successfully from database");
			return Customers;
		} catch (Exception e) {
			Log.error("Error while retrieving all Customers from database", e);
			throw e;
		} finally {
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
}
