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
        sessionFactory = HibernateUtil.getSessionFactory(); // Initialize Hibernate SessionFactory
    }

    @Override
    public void setClass(Class<Store> Store) {
        this.Store = Store; // Set the entity class for generic DAO operations
    }

    @Override
    public boolean create(Store store) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction(); // Start transaction
            session.save(store); // Save store entity to database
            transaction.commit(); // Commit transaction
            Log.info("Store persisted in database successfully");
            return true;
        } catch (Exception e) {
            Log.error("Database error while persisting Store", e);
            if (transaction != null) transaction.rollback(); // Rollback in case of error
            throw e;
        } finally {
            session.close(); // Always close session
        }
    }

    @Override
    public Store findById(long id) throws Exception {
        Session session = sessionFactory.openSession();
        try {
            Store store = session.get(Store.class, id); // Retrieve store by primary key
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
    public List<Store> findAll(int pageNumber, int pageSize) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();

            // Calculate offset based on page number (1-based) and page size
            int offset = (pageNumber - 1) * pageSize;

            // Retrieve paginated list of stores
            List<Store> stores = session.createQuery("from Store", Store.class)
                                        .setFirstResult(offset)
                                        .setMaxResults(pageSize)
                                        .list();

            Log.info("All Stores retrieved successfully with pagination");
            return stores;
        } catch (Exception e) {
            Log.error("Error while retrieving all Stores with pagination", e);
            throw e;
        } finally {
            if (session != null)
                session.close();
        }
    }

    @Override
    public boolean update(Store store) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction(); // Start transaction
            session.update(store); // Update store entity
            transaction.commit(); // Commit changes
            Log.info("Store updated in database successfully");
            return true;
        } catch (Exception e) {
            Log.error("Database error while updating Store", e);
            if (transaction != null) transaction.rollback(); // Rollback on error
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
            Store store = session.get(Store.class, id); // Load store by ID
            session.delete(store); // Delete store entity
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
            session.delete(store); // Delete store entity directly
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

    /**
     * Find stores by exact name.
     * 
     * @param name Store name
     * @return List of stores with the given name
     */
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

    /**
     * Find stores by city.
     * 
     * @param city City name
     * @return List of stores located in the given city
     */
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
