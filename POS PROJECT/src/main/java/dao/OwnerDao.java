package dao;

import java.util.List;

import model.Owner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import util.HibernateUtil;

public class OwnerDao implements GenericDao<Owner> {

    private static final Logger Log = LogManager.getLogger(OwnerDao.class);

    private SessionFactory sessionFactory;
    private Class<Owner> Owner;

    public OwnerDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public void setClass(Class<Owner> Owner) {
        this.Owner = Owner;
    }

    @Override
    public boolean create(Owner owner) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(owner);
            transaction.commit();
            Log.info("Owner persisted successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error while saving Owner", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Owner findById(long id) throws Exception {
        Session session = sessionFactory.openSession();
        try {
            Owner owner = session.get(Owner.class, id);
            Log.info("Owner with id: " + id + " retrieved successfully");
            return owner;
        } catch (Exception e) {
            Log.error("Error while retrieving Owner", e);
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Owner> findAll() throws Exception {
        Session session = sessionFactory.openSession();
        try {
            List<Owner> owners = session.createQuery("from Owner", Owner.class).list();
            Log.info("All Owners retrieved successfully");
            return owners;
        } catch (Exception e) {
            Log.error("Error while retrieving all Owners", e);
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(Owner owner) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(owner);
            transaction.commit();
            Log.info("Owner updated successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error while updating Owner", e);
            if (transaction != null) transaction.rollback();
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
            Owner owner = session.get(Owner.class, id);
            session.delete(owner);
            transaction.commit();
            Log.info("Owner deleted successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error while deleting Owner by id", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(Owner owner) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(owner);
            transaction.commit();
            Log.info("Owner deleted successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error while deleting Owner", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
    
    public Owner findByUsername(String username) throws Exception {
    	Session session = sessionFactory.openSession();
    	try {
    		Owner owner = session.createQuery("from Owner where loginUsername = :username", Owner.class)
    				.setParameter("username", username).uniqueResult();
    		Log.info("Owner with username: " + username + " retrieved successfully from database");
    		return owner;
    	} catch (Exception e) {
    		Log.error("Database error while retrieving Owner with username: " + username, e);
    		throw e;
    	} finally {
    		session.close();
    	}
    }

}
