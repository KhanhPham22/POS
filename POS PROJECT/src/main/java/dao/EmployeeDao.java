
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
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public void setClass(Class<Employee> Employee) {
		this.Employee = Employee;
	}

	public boolean create(Employee Employee) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.save(Employee);
			transaction.commit();
			Log.info("Employee persisted in database successfully");
			return true;
		} catch (Exception e) {
			Log.error("Database error while persisting Employee", e);
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public Employee findById(long id) throws Exception {
		Session session = sessionFactory.openSession();
		try {
			Employee Employee = session.get(Employee.class, id);
			Log.info("Employee with id: " + id + " retrieved successfully from database");
			return Employee;
		} catch (Exception e) {
			Log.error("Database error while retrieving Employee with id:" + id, e);
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public List<Employee> findAll() throws Exception {
		Session session = sessionFactory.openSession();
		try {
			List<Employee> Employees = session.createQuery("from Employee").list();
			Log.info("All Employees retrieved successfully from database");
			return Employees;
		} catch (Exception e) {
			Log.error("Error while retrieving Employees from database", e);
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public boolean update(Employee Employee) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.update(Employee);
			transaction.commit();
			Log.info("Employee with id: "+Employee.getPersonId()+" updated in database successfully");
			return true;
		} catch (Exception e) {
			Log.error("Database error while updating Employee with id: "+Employee.getPersonId(), e);
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
			Employee Employee = session.get(Employee.class, id);
			session.delete(Employee);
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

	@Override
	public boolean delete(Employee Employee) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.delete(Employee);
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
	
	public Employee findByUsername(String username) throws Exception {
		Session session = sessionFactory.openSession();
		try {
			Employee Employee = session.createQuery("from Employee where loginUsername = :username", Employee.class)
					.setParameter("username", username).uniqueResult();
			Log.info("Employee with username: " + username + " retrieved successfully from database");
			return Employee;
		} catch (Exception e) {
			Log.error("Database error while retrieving Employee with username:" + username, e);
			throw e;
		} finally {
			session.close();
		}
	}
}
