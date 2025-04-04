
package dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import model.UserSession;
import util.HibernateUtil;

public class UserSessionDao implements GenericDao<UserSession> {
	
	private static final Logger Log = LogManager.getLogger(UserSessionDao.class);
	
	private SessionFactory sessionFactory;

	private Class<UserSession> UserSession;

	public UserSessionDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public void setClass(Class<UserSession> UserSession) {
		this.UserSession = UserSession;
	}

	public boolean create(UserSession UserSession) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.save(UserSession);
			transaction.commit();
			Log.info("UserSession persisted in database successfully");
			return true;
		} catch (Exception e) {
			Log.error("Database error while persisting UserSession", e);
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public UserSession findById(long id) throws Exception {
		Session session = sessionFactory.openSession();
		try {
			UserSession UserSession = session.get(UserSession.class, id);
			Log.info("UserSession with id: " + id + " retrieved successfully from database");
			return UserSession;
		} catch (Exception e) {
			Log.error("Database error while retrieving UserSession", e);
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public List<UserSession> findAll() throws Exception {
		Session session = sessionFactory.openSession();
		try {
			List<UserSession> UserSessions = session.createQuery("from UserSession", UserSession.class).list();
			Log.info("All UserSessions retrieved successfully from database");
			return UserSessions;
		} catch (Exception e) {
			Log.error("Database error while retrieving all UserSessions", e);
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public boolean update(UserSession UserSession) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.update(UserSession);
			transaction.commit();
			Log.info("UserSession updated in database successfully");
			return true;
		} catch (Exception e) {
			Log.error("Database error while updating UserSession", e);
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
			UserSession UserSession = session.get(UserSession.class, id);
			session.delete(UserSession);
			transaction.commit();
			Log.info("UserSession deleted from database successfully");
			return true;
		} catch (Exception e) {
			Log.error("Database error while deleting UserSession", e);
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		} finally {
			session.close();
		}
	}

	@Override
	public boolean delete(UserSession UserSession) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			session.delete(UserSession);
			transaction.commit();
			Log.info("UserSession deleted from database successfully");
			return true;
		} catch (Exception e) {
			Log.error("Database error while deleting UserSession", e);
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		} finally {
			session.close();
		}
	}
}
