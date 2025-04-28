
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
	public List<UserSession> findAll(int pageNumber, int pageSize) throws Exception {
	    Session session = null;
	    try {
	        session = sessionFactory.openSession();

	        // Tính toán offset dựa trên pageNumber và pageSize
	        int offset = (pageNumber - 1) * pageSize; // Lưu ý pageNumber bắt đầu từ 1

	        // Sử dụng HQL để lấy tất cả các UserSession, và áp dụng phân trang
	        List<UserSession> userSessions = session.createQuery("from UserSession", UserSession.class)
	                                                 .setFirstResult(offset)  // Thiết lập vị trí bắt đầu
	                                                 .setMaxResults(pageSize) // Thiết lập số lượng bản ghi mỗi trang
	                                                 .list();

	        Log.info("All UserSessions retrieved successfully with pagination");
	        return userSessions;
	    } catch (Exception e) {
	        Log.error("Error while retrieving all UserSessions with pagination", e);
	        throw e;
	    } finally {
	        if (session != null)
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
	
	public boolean invalidateSession(long id) throws Exception {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			UserSession userSession = session.get(UserSession.class, id);
			if (userSession != null) {
				userSession.invalidate(); // Đặt active = false và cập nhật expiry
				session.update(userSession);
				transaction.commit();
				Log.info("UserSession invalidated successfully");
				return true;
			}
			return false;
		} catch (Exception e) {
			if (transaction != null) transaction.rollback();
			Log.error("Error invalidating session", e);
			throw e;
		} finally {
			session.close();
		}
	}

	
	public List<UserSession> findActiveSessions() throws Exception {
		Session session = sessionFactory.openSession();
		try {
			List<UserSession> sessions = session.createQuery(
				"FROM UserSession WHERE active = true", UserSession.class).list();
			Log.info("Active sessions retrieved successfully");
			return sessions;
		} catch (Exception e) {
			Log.error("Error retrieving active sessions", e);
			throw e;
		} finally {
			session.close();
		}
	}

	public UserSession findBySessionToken(String token) throws Exception {
		Session session = sessionFactory.openSession();
		try {
			UserSession userSession = session.createQuery(
				"FROM UserSession WHERE sessionToken = :token", UserSession.class)
				.setParameter("token", token)
				.uniqueResult();
			Log.info("Session retrieved by token successfully");
			return userSession;
		} catch (Exception e) {
			Log.error("Error retrieving session by token", e);
			throw e;
		} finally {
			session.close();
		}
	}

}
