package dao;

import java.util.List;

import model.Item;
import model.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import util.HibernateUtil;

public class ItemDao implements GenericDao<Item> {

    private static final Logger Log = LogManager.getLogger(ItemDao.class);

    private SessionFactory sessionFactory;
    private Class<Item> Item;

    public ItemDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void setClass(Class<Item> Item) {
        this.Item = Item;
    }

    /**
     * Save a new Item into the database.
     */
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
            if (transaction != null)
                transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Retrieve an Item by its ID.
     */
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

    /**
     * Retrieve all Items with pagination.
     */
    @Override
    public List<Item> findAll(int pageNumber, int pageSize) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();

            // Calculate the offset for pagination
            int offset = pageNumber * pageSize;

            // Execute the HQL query to fetch Items with pagination
            List<Item> items = session.createQuery("from Item", Item.class)
                    .setFirstResult(offset)
                    .setMaxResults(pageSize)
                    .list();

            // Initialize associations (if any) while the session is still open
            for (Item item : items) {
                Hibernate.initialize(item.getSupplier()); // Assuming Item has a reference to Supplier
            }

            Log.info("All items retrieved successfully. Total count: " + items.size());
            return items;
        } catch (Exception e) {
            Log.error("Error while retrieving all Items with pagination", e);
            throw e;
        } finally {
            if (session != null)
                session.close();
        }
    }

    /**
     * Update an existing Item in the database.
     */
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
            if (transaction != null)
                transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Delete an Item by its ID.
     */
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
            if (transaction != null)
                transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Delete a given Item entity.
     */
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
            if (transaction != null)
                transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Find Items by name (case-insensitive search).
     */
    public List<Item> findByName(String name) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();

            String hql = "FROM Item WHERE lower(name) LIKE :name";
            List<Item> items = session.createQuery(hql, Item.class)
                    .setParameter("name", "%" + name.toLowerCase() + "%")
                    .list();

            // Initialize associated entities (e.g. Supplier)
            for (Item item : items) {
                Hibernate.initialize(item.getSupplier());
            }

            Log.info("Items retrieved with name like: " + name);
            return items;
        } catch (Exception e) {
            Log.error("Error while retrieving items by name", e);
            throw e;
        } finally {
            if (session != null)
                session.close();
        }
    }

    /**
     * Find Items by a specific supplier ID with pagination.
     */
    public List<Item> findBySupplierId(long supplierId, int pageNumber, int pageSize) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();

            // Calculate the offset based on the page number and size
            int offset = (pageNumber - 1) * pageSize;
            System.out.println("Fetching items for supplierId=" + supplierId + ", pageNumber=" + pageNumber + ", pageSize=" + pageSize + ", offset=" + offset);

            String hql = "FROM Item WHERE supplier.id = :supplierId";
            List<Item> items = session.createQuery(hql, Item.class)
                    .setParameter("supplierId", supplierId)
                    .setFirstResult(offset)
                    .setMaxResults(pageSize)
                    .list();

            // Print the number of items retrieved
            System.out.println("Items retrieved: " + items.size());

            // Initialize associated entities
            for (Item item : items) {
                Hibernate.initialize(item.getSupplier());
            }

            Log.info("Items for supplier with ID: " + supplierId + " retrieved successfully.");
            return items;
        } catch (Exception e) {
            Log.error("Error while retrieving items by supplierId", e);
            throw e;
        } finally {
            if (session != null)
                session.close();
        }
    }
}
