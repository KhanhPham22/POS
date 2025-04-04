
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

	public PersonDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public void setClass(Class<Person> Person) {
		this.Person = Person;
	}

	public boolean create(Person Person) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.save(Person);
			transaction.commit();
			Log.info("Person persisted in database successfully");
			return true;
		} catch (Exception e) {
			Log.error("Database error while persisting Person", e);
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public Person findById(long id) throws Exception {
		Session session = sessionFactory.openSession();
		try {
			Person Person = session.get(Person.class, id);
			Log.info("Person with id: " + id + " retrieved successfully from database");
			return Person;
		} catch (Exception e) {
			Log.error("Database error while retrieving Person with id:" + id, e);
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public List<Person> findAll() throws Exception {
		Session session = sessionFactory.openSession();
		try {
			List<Person> Person = session.createQuery("from Person", Person.class).list();
			Log.info("All Person retrieved successfully from database");
			return Person;
		} catch (Exception e) {
			Log.error("Database error while retrieving all Person", e);
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public boolean update(Person Person) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.update(Person);
			transaction.commit();
			Log.info("Person updated in database successfully");
			return true;
		} catch (Exception e) {
			Log.error("Database error while updating Person", e);
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
			Person Person = session.get(Person.class, id);
			session.delete(Person);
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

	@Override
	public boolean delete(Person Person) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.delete(Person);
			transaction.commit();
			Log.info("Person deleted successfully from database");
			return true;
		} catch (Exception e) {
			Log.error("Database error while deleting Person", e);
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		} finally {
			session.close();
		}
	}
}
