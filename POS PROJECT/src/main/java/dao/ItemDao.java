package dao;

import java.util.List;

import model.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import util.HibernateUtil;

public class ItemDao implements GenericDao<Item> {

    private static final Logger Log = LogManager.getLogger(ItemDao.class);

    private SessionFactory sessionFactory;
    private Class<Item> itemClass;

    public ItemDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public void setClass(Class<Item> itemClass) {
        this.itemClass = itemClass;
    }

    @Override
    public boolean create(Item item) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(item);
            transaction.commit();
            Log.info("Item persisted successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error while saving Item", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public Item findById(long id) throws Exception {
        Session session = sessionFactory.openSession();
        try {
            Item item = session.get(Item.class, id);
            Log.info("Item with id: " + id + " retrieved successfully");
            return item;
        } catch (Exception e) {
            Log.error("Error while retrieving Item", e);
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public List<Item> findAll() throws Exception {
        Session session = sessionFactory.openSession();
        try {
            List<Item> items = session.createQuery("from Item", Item.class).list();
            Log.info("All Items retrieved successfully");
            return items;
        } catch (Exception e) {
            Log.error("Error while retrieving all Items", e);
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(Item item) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(item);
            transaction.commit();
            Log.info("Item updated successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error while updating Item", e);
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
            Item item = session.get(Item.class, id);
            session.delete(item);
            transaction.commit();
            Log.info("Item deleted successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error while deleting Item by id", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(Item item) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(item);
            transaction.commit();
            Log.info("Item deleted successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error while deleting Item", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
