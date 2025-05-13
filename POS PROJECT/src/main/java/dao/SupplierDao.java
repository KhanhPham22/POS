package dao;

import model.Supplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

/**
 * Data Access Object (DAO) implementation for managing Supplier entities using Hibernate.
 * Provides basic CRUD operations, pagination, and query methods by name, phone, and tax code.
 */
public class SupplierDao implements GenericDao<Supplier> {

    private static final Logger Log = LogManager.getLogger(SupplierDao.class);

    private SessionFactory sessionFactory;
    private Class<Supplier> supplierClass;

    // Constructor to initialize Hibernate SessionFactory
    public SupplierDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    // Set the entity class type
    @Override
    public void setClass(Class<Supplier> supplierClass) {
        this.supplierClass = supplierClass;
    }

    // Save a new Supplier to the database
    @Override
    public boolean create(Supplier supplier) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(supplier);
            transaction.commit();
            Log.info("Supplier persisted successfully: " + supplier.getName());
            return true;
        } catch (Exception e) {
            Log.error("Error while saving Supplier", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    // Find a Supplier by its ID
    @Override
    public Supplier findById(long id) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Supplier supplier = session.get(Supplier.class, id);
            if (supplier != null) {
                Log.info("Supplier with id: " + id + " retrieved successfully");
            } else {
                Log.warn("Supplier with id: " + id + " not found");
            }
            return supplier;
        } catch (Exception e) {
            Log.error("Error while retrieving Supplier with id: " + id, e);
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    // Retrieve a paginated list of all Suppliers
    public List<Supplier> findAll(int pageNumber, int pageSize) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();

            // Calculate offset based on current page number
            int offset = (pageNumber - 1) * pageSize;

            // Query with pagination
            List<Supplier> suppliers = session.createQuery("from Supplier", Supplier.class)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .list();

            // Initialize related items before session closes
            for (Supplier supplier : suppliers) {
                Hibernate.initialize(supplier.getItems());
            }

            Log.info("Suppliers retrieved with pagination. Page: " + pageNumber + ", Size: " + pageSize);
            return suppliers;
        } catch (Exception e) {
            Log.error("Error while retrieving suppliers with pagination", e);
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    // Update an existing Supplier
    @Override
    public boolean update(Supplier supplier) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.update(supplier);
            transaction.commit();
            Log.info("Supplier updated successfully: " + supplier.getName());
            return true;
        } catch (Exception e) {
            Log.error("Error while updating Supplier", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    // Delete a Supplier by its ID
    @Override
    public boolean deleteById(long id) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Supplier supplier = session.get(Supplier.class, id);
            if (supplier != null) {
                session.delete(supplier);
                transaction.commit();
                Log.info("Supplier with id: " + id + " deleted successfully");
            } else {
                Log.warn("Supplier with id: " + id + " not found for deletion");
            }
            return true;
        } catch (Exception e) {
            Log.error("Error while deleting Supplier with id: " + id, e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    // Delete a given Supplier entity
    @Override
    public boolean delete(Supplier supplier) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.delete(supplier);
            transaction.commit();
            Log.info("Supplier deleted successfully: " + supplier.getName());
            return true;
        } catch (Exception e) {
            Log.error("Error while deleting Supplier", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    // Search suppliers whose name contains the given string (case-insensitive)
    public List<Supplier> findByName(String name) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            String hql = "FROM Supplier WHERE lower(name) LIKE :name";
            List<Supplier> suppliers = session.createQuery(hql, Supplier.class)
                .setParameter("name", "%" + name.toLowerCase() + "%")
                .list();

            for (Supplier supplier : suppliers) {
                Hibernate.initialize(supplier.getItems());
            }

            Log.info("Suppliers retrieved with name like: " + name);
            return suppliers;
        } catch (Exception e) {
            Log.error("Error while retrieving suppliers by name: " + name, e);
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    // Find a supplier by phone number
    public Supplier findByPhone(String phone) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            String hql = "FROM Supplier WHERE phone = :phone";
            return session.createQuery(hql, Supplier.class)
                          .setParameter("phone", phone)
                          .uniqueResult();
        } catch (Exception e) {
            Log.error("Error while retrieving supplier by phone: " + phone, e);
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    // Find a supplier by tax code
    public Supplier findByTaxCode(String taxCode) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            String hql = "FROM Supplier WHERE taxCode = :taxCode";
            return session.createQuery(hql, Supplier.class)
                          .setParameter("taxCode", taxCode)
                          .uniqueResult();
        } finally {
            if (session != null) session.close();
        }
    }

    // Find a supplier by exact name (case-insensitive)
    public Supplier findByNameExact(String name) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            String hql = "FROM Supplier WHERE lower(name) = :name";
            return session.createQuery(hql, Supplier.class)
                          .setParameter("name", name.toLowerCase())
                          .uniqueResult();
        } catch (Exception e) {
            Log.error("Error while checking exact name: " + name, e);
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }
}
