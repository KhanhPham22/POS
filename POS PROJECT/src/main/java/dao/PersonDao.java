
package dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import model.Person;
import util.HibernateUtil;

public class PersonDao implements GenericDao<Person> {
	
	private static final Logger Log = LogManager.getLogger(PersonDao.class);
	
	private SessionFactory sessionFactory;
	private Class<Person> Person;

	// Constructor - Initialize Hibernate SessionFactory
	public PersonDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	// Set the entity class (Person) for generic operations
	public void setClass(Class<Person> Person) {
		this.Person = Person;
	}

	// Create a new Person entry in the database
	public boolean create(Person Person) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.save(Person); // Save the person object
			transaction.commit(); // Commit transaction
			Log.info("Person persisted in database successfully");
			return true;
		} catch (Exception e) {
			Log.error("Database error while persisting Person", e);
			if (transaction != null) {
				transaction.rollback(); // Rollback in case of error
			}
			throw e;
		} finally {
			session.close(); // Always close session
		}
	}

	// Find a Person by their unique ID
	@Override
	public Person findById(long id) throws Exception {
		Session session = sessionFactory.openSession();
		try {
			Person Person = session.get(Person.class, id); // Retrieve person by ID
			Log.info("Person with id: " + id + " retrieved successfully from database");
			return Person;
		} catch (Exception e) {
			Log.error("Database error while retrieving Person with id:" + id, e);
			throw e;
		} finally {
			session.close(); // Close session
		}
	}

	// Retrieve a paginated list of all Persons
	@Override
	public List<Person> findAll(int pageNumber, int pageSize) throws Exception {
	    Session session = null;
	    try {
	        session = sessionFactory.openSession();

	        // Calculate offset based on page number and size
	        int offset = (pageNumber - 1) * pageSize;

	        // Use HQL to retrieve paginated list of persons
	        List<Person> persons = session.createQuery("from Person", Person.class)
	                                      .setFirstResult(offset)  // Set start position
	                                      .setMaxResults(pageSize) // Set max results per page
	                                      .list();

	        Log.info("All Persons retrieved successfully with pagination from database");
	        return persons;
	    } catch (Exception e) {
	        Log.error("Error while retrieving all Persons with pagination from database", e);
	        throw e;
	    } finally {
	        if (session != null)
	            session.close(); // Ensure session is closed
	    }
	}

	// Update an existing Person record
	@Override
	public boolean update(Person Person) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.update(Person); // Update person object
			transaction.commit();
			Log.info("Person updated in database successfully");
			return true;
		} catch (Exception e) {
			Log.error("Database error while updating Person", e);
			if (transaction != null) {
				transaction.rollback(); // Rollback transaction if failure occurs
			}
			throw e;
		} finally {
			session.close(); // Always close session
		}
	}

	// Delete a Person by their ID
	@Override
	public boolean deleteById(long id) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			Person Person = session.get(Person.class, id); // Get the person
			session.delete(Person); // Delete the person
			transaction.commit();
			Log.info("Person with id: " + id + " deleted successfully from database");
			return true;
		} catch (Exception e) {
			Log.error("Database error while deleting Person with id:" + id, e);
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		} finally {
			session.close();
		}
	}

	// Delete a Person entity directly
	@Override
	public boolean delete(Person Person) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.delete(Person); // Delete the person entity
			transaction.commit();
			Log.info("Person deleted successfully from database");
			return true;
		} catch (Exception e) {
			Log.error("Database error while deleting Person", e);
			if (transaction != null) {
				transaction.rollback(); // Rollback on error
			}
			throw e;
		} finally {
			session.close(); // Close session
		}
	}
}
