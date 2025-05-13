
package dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import model.Employee;
import util.HibernateUtil;

public class EmployeeDao implements GenericDao<Employee> {
	
	private static final Logger Log = LogManager.getLogger(EmployeeDao.class);
	
	private SessionFactory sessionFactory;
	private Class<Employee> Employee;

	public EmployeeDao() {
	    try {
	        // Initialize Hibernate SessionFactory once
	        sessionFactory = HibernateUtil.getSessionFactory();
	        System.out.println("SessionFactory initialized successfully");
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Failed to initialize SessionFactory", e);
	    }
	}

	// Set the entity class used for generic operations
	public void setClass(Class<Employee> Employee) {
		this.Employee = Employee;
	}

	// Create a new Employee record in the database
	public boolean create(Employee Employee) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.save(Employee); // Save new Employee
			transaction.commit();
			Log.info("Employee persisted in database successfully");
			return true;
		} catch (Exception e) {
			Log.error("Database error while persisting Employee", e);
			if (transaction != null) {
				transaction.rollback(); // Rollback on failure
			}
			throw e;
		} finally {
			session.close(); // Always close session
		}
	}

	// Find an Employee by their ID
	@Override
	public Employee findById(long id) throws Exception {
		Session session = sessionFactory.openSession();
		try {
			Employee Employee = session.get(Employee.class, id); // Fetch by primary key
			Log.info("Employee with id: " + id + " retrieved successfully from database");
			return Employee;
		} catch (Exception e) {
			Log.error("Database error while retrieving Employee with id:" + id, e);
			throw e;
		} finally {
			session.close();
		}
	}

	// Retrieve a paginated list of all Employees
	@Override
	public List<Employee> findAll(int pageNumber, int pageSize) throws Exception {
	    Session session = null;
	    try {
	        session = sessionFactory.openSession();

	        // Calculate offset for pagination (pageNumber starts at 1)
	        int offset = (pageNumber - 1) * pageSize;

	        // Use HQL query with pagination
	        List<Employee> employees = session.createQuery("from Employee", Employee.class)
	                                          .setFirstResult(offset)
	                                          .setMaxResults(pageSize)
	                                          .list();

	        Log.info("All Employees retrieved successfully from database. Total count: " + employees.size());
	        return employees;
	    } catch (Exception e) {
	        Log.error("Error while retrieving Employees with pagination from database", e);
	        throw e;
	    } finally {
	        if (session != null)
	            session.close();
	    }
	}

	// Update an existing Employee in the database
	@Override
	public boolean update(Employee Employee) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.update(Employee); // Hibernate will generate UPDATE statement
			transaction.commit();
			Log.info("Employee with id: " + Employee.getPersonId() + " updated in database successfully");
			return true;
		} catch (Exception e) {
			Log.error("Database error while updating Employee with id: " + Employee.getPersonId(), e);
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		} finally {
			session.close();
		}
	}

	// Delete an Employee from database by their ID
	@Override
	public boolean deleteById(long id) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Employee Employee = session.get(Employee.class, id);
			session.delete(Employee); // Remove entity from DB
			transaction.commit();
			Log.info("Employee with id: " + id + " deleted from database successfully");
			return true;
		} catch (Exception e) {
			Log.error("Database error while deleting Employee with id: " + id, e);
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		} finally {
			session.close();
		}
	}

	// Delete an Employee by passing the entity object
	@Override
	public boolean delete(Employee Employee) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.delete(Employee); // Delete using object reference
			transaction.commit();
			Log.info("Employee with id: " + Employee.getPersonId() + " deleted from database successfully");
			return true;
		} catch (Exception e) {
			Log.error("Database error while deleting Employee with id: " + Employee.getPersonId(), e);
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		} finally {
			session.close();
		}
	}

	// Find Employee by loginUsername field
	public Employee findByUsername(String username) throws Exception {
        Session session = sessionFactory.openSession();
        try {
            Employee employee = session.createQuery("from Employee where loginUsername = :username", Employee.class)
                    .setParameter("username", username)
                    .uniqueResult();
            Log.info("Employee with username: " + username + " retrieved successfully from database");
            return employee;
        } catch (Exception e) {
            Log.error("Database error while retrieving Employee with username:" + username, e);
            throw e;
        } finally {
            session.close();
        }
    }

    // Find Employee by email field
    public Employee findByEmail(String email) throws Exception {
        Session session = sessionFactory.openSession();
        try {
            Employee employee = session.createQuery("from Employee where email = :email", Employee.class)
                    .setParameter("email", email)
                    .uniqueResult();
            Log.info("Employee with email: " + email + " retrieved successfully from database");
            return employee;
        } catch (Exception e) {
            Log.error("Database error while retrieving Employee with email: " + email, e);
            throw e;
        } finally {
            session.close();
        }
    }
}
