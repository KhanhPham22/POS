
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

	// Constructor to initialize sessionFactory
	public CustomerDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	// Setter to set the class type for Customer (used for generic purposes)
	public void setClass(Class<Customer> Customer) {
		this.Customer = Customer;
	}

	// Method to create a new Customer record in the database
	public boolean create(Customer Customer) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.save(Customer);  // Save the Customer object to the database
			transaction.commit();
			Log.info("Customer persisted in database successfully");
			return true;
		} catch (Exception e) {
			Log.error("Database error while persisting Customer", e);
			if (transaction != null) {
				transaction.rollback();  // Rollback in case of an error
			}
			throw e;
		} finally {
			session.close();  // Ensure the session is closed
		}
	}
	
	@Override
	// Method to retrieve a Customer by its ID from the database
	public Customer findById(long id) throws Exception {
		Session session = sessionFactory.openSession();
		try {
			Customer Customer = session.get(Customer.class, id);  // Retrieve Customer by ID
			Log.info("Customer with id: " + id + " retrieved successfully from database");
			return Customer;
		} catch (Exception e) {
			Log.error("Database error while retrieving Customer with id:" + id, e);
			throw e;
		} finally {
			session.close();  // Ensure the session is closed
		}
	}

	@Override
	// Method to retrieve all Customers with pagination support
	public List<Customer> findAll(int pageNumber, int pageSize) throws Exception {
	    Session session = null;
	    try {
	        session = sessionFactory.openSession();

	        // Calculate offset based on pageNumber and pageSize
	        int offset = (pageNumber - 1) * pageSize;  // Note: pageNumber starts from 1

	        // Use HQL to retrieve all Customers and apply pagination
	        List<Customer> customers = session.createQuery("from Customer", Customer.class)
	                                          .setFirstResult(offset)  // Set starting position
	                                          .setMaxResults(pageSize) // Set the number of records per page
	                                          .list();

	        Log.info("All Customers retrieved successfully from database. Total count: " + customers.size());
	        return customers;
	    } catch (Exception e) {
	        Log.error("Error while retrieving all Customers with pagination from database", e);
	        throw e;
	    } finally {
	        if (session != null)
	            session.close();  // Ensure session is closed
	    }
	}

	@Override
	// Method to update a Customer in the database
	public boolean update(Customer customer) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.update(customer);  // Update the Customer object in the database
			transaction.commit();
			Log.info("Customer with id: "+customer.getPersonId()+" updated in database successfully");
			return true;
		} catch (Exception e) {
			Log.error("Database error while updating Customer with id: "+customer.getPersonId(), e);
			if (transaction != null) {
				transaction.rollback();  // Rollback in case of an error
			}
			throw e;
		} finally {
			session.close();  // Ensure the session is closed
		}
	}

	@Override
	// Method to delete a Customer by its ID
	public boolean deleteById(long id) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Customer Customer = session.get(Customer.class, id);  // Retrieve Customer by ID
			session.delete(Customer);  // Delete the Customer object from the database
			transaction.commit();
			Log.info("Customer with id: " + id + " deleted from database successfully");
			return true;
		} catch (Exception e) {
			Log.error("Database error while deleting Customer with id: " + id, e);
			if (transaction != null) {
				transaction.rollback();  // Rollback in case of an error
			}
			throw e;
		} finally {
			session.close();  // Ensure the session is closed
		}
	}

	@Override
	// Method to delete a Customer from the database
	public boolean delete(Customer Customer) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.delete(Customer);  // Delete the Customer object from the database
			transaction.commit();
			Log.info("Customer with id: " + Customer.getPersonId() + " deleted from database successfully");
			return true;
		} catch (Exception e) {
			Log.error("Database error while deleting Customer with id: " + Customer.getPersonId(), e);
			if (transaction != null) {
				transaction.rollback();  // Rollback in case of an error
			}
			throw e;
		} finally {
			session.close();  // Ensure the session is closed
		}
	}
	
	// Method to find a Customer by name and phone number
	public Customer findByNameAndPhone(String name, String phone) throws Exception {
		Session session = sessionFactory.openSession();
		try {
			// HQL query to find Customer by name and phone
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
			session.close();  // Ensure the session is closed
		}
	}
	
	// Method to find a Customer by phone number
	public Customer findByPhone(String phone) throws Exception {
        Session session = sessionFactory.openSession();
        try {
            // HQL query to find Customer by phone
            String hql = "FROM Customer WHERE phone = :phone";
            Customer customer = session.createQuery(hql, Customer.class)
                    .setParameter("phone", phone)
                    .uniqueResult();
            if (customer != null) {
                Log.info("Customer with phone: " + phone + " retrieved successfully from database");
            } else {
                Log.info("No Customer found with phone: " + phone);
            }
            return customer;
        } catch (Exception e) {
            Log.error("Database error while retrieving Customer with phone: " + phone, e);
            throw e;
        } finally {
            session.close();  // Ensure the session is closed
        }
    }
}
