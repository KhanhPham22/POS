
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

    // Constructor initializes the Hibernate SessionFactory
    public UserSessionDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    // Setter to specify the entity class (useful for generic handling)
    public void setClass(Class<UserSession> UserSession) {
        this.UserSession = UserSession;
    }

    // Create a new UserSession and persist it to the database
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
                transaction.rollback(); // Rollback on error
            }
            throw e;
        } finally {
            session.close(); // Ensure session is closed
        }
    }

    // Find a UserSession by its ID
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

    // Retrieve a paginated list of UserSession objects
    @Override
    public List<UserSession> findAll(int pageNumber, int pageSize) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();

            // Calculate pagination offset
            int offset = (pageNumber - 1) * pageSize;

            // HQL query to fetch UserSessions with pagination
            List<UserSession> userSessions = session
                .createQuery("from UserSession", UserSession.class)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
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

    // Update an existing UserSession
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

    // Delete a UserSession by its ID
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

    // Delete a UserSession object
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

    // Invalidate a session by setting its active flag to false and updating expiry time
    public boolean invalidateSession(long id) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            UserSession userSession = session.get(UserSession.class, id);
            if (userSession != null) {
                userSession.invalidate(); // Mark session as inactive
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

    // Find all active (non-expired and active = true) sessions
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

    // Find a session by its token (used for authentication/session management)
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
