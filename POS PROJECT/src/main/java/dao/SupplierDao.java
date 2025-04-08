package dao;

import model.Supplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class SupplierDao implements GenericDao<Supplier> {

    private static final Logger Log = LogManager.getLogger(SupplierDao.class);

    private SessionFactory sessionFactory;
    private Class<Supplier> supplierClass;

    public SupplierDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void setClass(Class<Supplier> supplierClass) {
        this.supplierClass = supplierClass; // Gán lớp Supplier
    }

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

    @Override
    public List<Supplier> findAll() throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            List<Supplier> suppliers = session.createQuery("from Supplier", Supplier.class).list();
            Log.info("All suppliers retrieved successfully. Total count: " + suppliers.size());
            return suppliers;
        } catch (Exception e) {
            Log.error("Error while retrieving all Suppliers", e);
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

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
    public List<Supplier> findByName(String name) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            String hql = "FROM Supplier WHERE lower(name) LIKE :name";
            List<Supplier> suppliers = session.createQuery(hql, Supplier.class)
                .setParameter("name", "%" + name.toLowerCase() + "%")
                .list();
            Log.info("Suppliers retrieved with name like: " + name);
            return suppliers;
        } catch (Exception e) {
            Log.error("Error while retrieving suppliers by name: " + name, e);
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

}
