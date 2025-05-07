
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
	        sessionFactory = HibernateUtil.getSessionFactory();
	        System.out.println("SessionFactory initialized successfully");
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Failed to initialize SessionFactory", e);
	    }
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
	public List<Employee> findAll(int pageNumber, int pageSize) throws Exception {
	    Session session = null;
	    try {
	        session = sessionFactory.openSession();

	        // Tính toán offset dựa trên pageNumber và pageSize
	        int offset = (pageNumber - 1) * pageSize; // Lưu ý pageNumber bắt đầu từ 1

	        // Sử dụng HQL để lấy tất cả các Employee, và áp dụng phân trang
	        List<Employee> employees = session.createQuery("from Employee", Employee.class)
	                                          .setFirstResult(offset)  // Thiết lập vị trí bắt đầu
	                                          .setMaxResults(pageSize) // Thiết lập số lượng bản ghi mỗi trang
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
            Employee employee = session.createQuery("from Employee where loginUsername = :username", Employee.class)
                    .setParameter("username", username).uniqueResult();
            Log.info("Employee with username: " + username + " retrieved successfully from database");
            return employee;
        } catch (Exception e) {
            Log.error("Database error while retrieving Employee with username:" + username, e);
            throw e;
        } finally {
            session.close();
        }
    }
	
	 public Employee findByEmail(String email) throws Exception {
	        Session session = sessionFactory.openSession();
	        try {
	            Employee employee = session.createQuery("from Employee where email = :email", Employee.class)
	                    .setParameter("email", email).uniqueResult();
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
