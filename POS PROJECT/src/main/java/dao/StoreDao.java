package dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import model.Store;
import util.HibernateUtil;

public class StoreDao implements GenericDao<Store> {

    private static final Logger Log = LogManager.getLogger(StoreDao.class);

    private SessionFactory sessionFactory;
    private Class<Store> Store;

    public StoreDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void setClass(Class<Store> Store) {
        this.Store = Store;
    }

    @Override
    public boolean create(Store store) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(store);
            transaction.commit();
            Log.info("Store persisted in database successfully");
            return true;
        } catch (Exception e) {
            Log.error("Database error while persisting Store", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Store findById(long id) throws Exception {
        Session session = sessionFactory.openSession();
        try {
            Store store = session.get(Store.class, id);
            Log.info("Store with id: " + id + " retrieved successfully from database");
            return store;
        } catch (Exception e) {
            Log.error("Database error while retrieving Store", e);
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Store> findAll() throws Exception {
        Session session = sessionFactory.openSession();
        try {
            List<Store> stores = session.createQuery("from Store", Store.class).list();
            Log.info("All Stores retrieved successfully from database");
            return stores;
        } catch (Exception e) {
            Log.error("Database error while retrieving all Stores", e);
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(Store store) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(store);
            transaction.commit();
            Log.info("Store updated in database successfully");
            return true;
        } catch (Exception e) {
            Log.error("Database error while updating Store", e);
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
            Store store = session.get(Store.class, id);
            session.delete(store);
            transaction.commit();
            Log.info("Store deleted from database successfully");
            return true;
        } catch (Exception e) {
            Log.error("Database error while deleting Store", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(Store store) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(store);
            transaction.commit();
            Log.info("Store deleted from database successfully");
            return true;
        } catch (Exception e) {
            Log.error("Database error while deleting Store", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
    
    public List<Store> findByName(String name) throws Exception {
        Session session = sessionFactory.openSession();
        try {
            List<Store> stores = session.createQuery("from Store where name = :name", Store.class)
                                        .setParameter("name", name)
                                        .list();
            Log.info("Stores with name '" + name + "' retrieved successfully from database");
            return stores;
        } catch (Exception e) {
            Log.error("Database error while retrieving Stores by name", e);
            throw e;
        } finally {
            session.close();
        }
    }

    public List<Store> findByCity(String city) throws Exception {
        Session session = sessionFactory.openSession();
        try {
            List<Store> stores = session.createQuery("from Store where city = :city", Store.class)
                                        .setParameter("city", city)
                                        .list();
            Log.info("Stores with city '" + city + "' retrieved successfully from database");
            return stores;
        } catch (Exception e) {
            Log.error("Database error while retrieving Stores by city", e);
            throw e;
        } finally {
            session.close();
        }
    }

}
