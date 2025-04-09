package dao;

import model.WarehouseImport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class WarehouseImportDao implements GenericDao<WarehouseImport> {

    private static final Logger Log = LogManager.getLogger(WarehouseImportDao.class);

    private SessionFactory sessionFactory;
    private Class<WarehouseImport> warehouseImportClass;

    public WarehouseImportDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void setClass(Class<WarehouseImport> warehouseImportClass) {
        this.warehouseImportClass = warehouseImportClass; // Gán lớp WarehouseImport
    }

    @Override
    public boolean create(WarehouseImport warehouseImport) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(warehouseImport);
            transaction.commit();
            Log.info("WarehouseImport persisted successfully: " + warehouseImport.getName());
            return true;
        } catch (Exception e) {
            Log.error("Error while saving WarehouseImport", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public WarehouseImport findById(long id) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            WarehouseImport warehouseImport = session.get(WarehouseImport.class, id);
            if (warehouseImport != null) {
                Log.info("WarehouseImport with id: " + id + " retrieved successfully");
            } else {
                Log.warn("WarehouseImport with id: " + id + " not found");
            }
            return warehouseImport;
        } catch (Exception e) {
            Log.error("Error while retrieving WarehouseImport with id: " + id, e);
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<WarehouseImport> findAll() throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            List<WarehouseImport> warehouseImports = session.createQuery("from WarehouseImport", WarehouseImport.class).list();
            Log.info("All WarehouseImports retrieved successfully. Total count: " + warehouseImports.size());
            return warehouseImports;
        } catch (Exception e) {
            Log.error("Error while retrieving all WarehouseImports", e);
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public boolean update(WarehouseImport warehouseImport) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.update(warehouseImport);
            transaction.commit();
            Log.info("WarehouseImport updated successfully: " + warehouseImport.getName());
            return true;
        } catch (Exception e) {
            Log.error("Error while updating WarehouseImport", e);
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
            WarehouseImport warehouseImport = session.get(WarehouseImport.class, id);
            if (warehouseImport != null) {
                session.delete(warehouseImport);
                transaction.commit();
                Log.info("WarehouseImport with id: " + id + " deleted successfully");
            } else {
                Log.warn("WarehouseImport with id: " + id + " not found for deletion");
            }
            return true;
        } catch (Exception e) {
            Log.error("Error while deleting WarehouseImport with id: " + id, e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public boolean delete(WarehouseImport warehouseImport) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.delete(warehouseImport);
            transaction.commit();
            Log.info("WarehouseImport deleted successfully: " + warehouseImport.getName());
            return true;
        } catch (Exception e) {
            Log.error("Error while deleting WarehouseImport", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }
    
    public WarehouseImport findByName(String name) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            String hql = "FROM WarehouseImport WHERE name = :name";
            WarehouseImport result = session.createQuery(hql, WarehouseImport.class)
                                            .setParameter("name", name)
                                            .uniqueResult();
            if (result != null) {
                Log.info("WarehouseImport with name: " + name + " retrieved successfully");
            } else {
                Log.warn("No WarehouseImport found with name: " + name);
            }
            return result;
        } catch (Exception e) {
            Log.error("Error while retrieving WarehouseImport by name: " + name, e);
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    public WarehouseImport findByShortName(String shortName) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            String hql = "FROM WarehouseImport WHERE shortName = :shortName";
            WarehouseImport result = session.createQuery(hql, WarehouseImport.class)
                                            .setParameter("shortName", shortName)
                                            .uniqueResult();
            if (result != null) {
                Log.info("WarehouseImport with shortName: " + shortName + " retrieved successfully");
            } else {
                Log.warn("No WarehouseImport found with shortName: " + shortName);
            }
            return result;
        } catch (Exception e) {
            Log.error("Error while retrieving WarehouseImport by shortName: " + shortName, e);
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

}
