package dao;

import model.Warehouse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public class WarehouseDao implements GenericDao<Warehouse> {

    private static final Logger Log = LogManager.getLogger(WarehouseDao.class);

    private SessionFactory sessionFactory;
    private Class<Warehouse> warehouseClass;

    public WarehouseDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void setClass(Class<Warehouse> warehouseClass) {
        this.warehouseClass = warehouseClass; // Gán lớp Warehouse
    }

    @Override
    public boolean create(Warehouse warehouse) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(warehouse);
            transaction.commit();
            Log.info("Warehouse persisted successfully: " + warehouse.getName());
            return true;
        } catch (Exception e) {
            Log.error("Error while saving Warehouse", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public Warehouse findById(long id) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            Warehouse warehouse = session.get(Warehouse.class, id);
            if (warehouse != null) {
                Log.info("Warehouse with id: " + id + " retrieved successfully");
            } else {
                Log.warn("Warehouse with id: " + id + " not found");
            }
            return warehouse;
        } catch (Exception e) {
            Log.error("Error while retrieving Warehouse with id: " + id, e);
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Warehouse> findAll(int pageNumber, int pageSize) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();

            // Tính toán offset dựa trên pageNumber và pageSize
            int offset = (pageNumber - 1) * pageSize; // Lưu ý pageNumber bắt đầu từ 1

            // Sử dụng HQL để lấy tất cả các Warehouse, và áp dụng phân trang
            List<Warehouse> warehouses = session.createQuery("FROM Warehouse", Warehouse.class)
                                                .setFirstResult(offset)  // Thiết lập vị trí bắt đầu
                                                .setMaxResults(pageSize) // Thiết lập số lượng bản ghi mỗi trang
                                                .list();

            Log.info("All Warehouses retrieved successfully with pagination");
            return warehouses;
        } catch (Exception e) {
            Log.error("Error while retrieving all Warehouses with pagination", e);
            throw e;
        } finally {
            if (session != null)
                session.close();
        }
    }

    @Override
    public boolean update(Warehouse warehouse) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.update(warehouse);
            transaction.commit();
            Log.info("Warehouse updated successfully: " + warehouse.getName());
            return true;
        } catch (Exception e) {
            Log.error("Error while updating Warehouse", e);
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
            Warehouse warehouse = session.get(Warehouse.class, id);
            if (warehouse != null) {
                session.delete(warehouse);
                transaction.commit();
                Log.info("Warehouse with id: " + id + " deleted successfully");
            } else {
                Log.warn("Warehouse with id: " + id + " not found for deletion");
            }
            return true;
        } catch (Exception e) {
            Log.error("Error while deleting Warehouse with id: " + id, e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public boolean delete(Warehouse warehouse) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.delete(warehouse);
            transaction.commit();
            Log.info("Warehouse deleted successfully: " + warehouse.getName());
            return true;
        } catch (Exception e) {
            Log.error("Error while deleting Warehouse", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    public Warehouse findByName(String name) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            String hql = "FROM Warehouse WHERE name = :name"; // Đã sửa
            Warehouse result = session.createQuery(hql, Warehouse.class)
                                        .setParameter("name", name)
                                        .uniqueResult();
            if (result != null) {
                Log.info("Warehouse with name: " + name + " retrieved successfully");
            } else {
                Log.warn("No Warehouse found with name: " + name);
            }
            return result;
        } catch (Exception e) {
            Log.error("Error while retrieving Warehouse by name: " + name, e);
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    public Warehouse findByShortName(String shortName) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            String hql = "FROM Warehouse WHERE shortName = :shortName"; // Đã sửa
            Warehouse result = session.createQuery(hql, Warehouse.class)
                                        .setParameter("shortName", shortName)
                                        .uniqueResult();
            if (result != null) {
                Log.info("Warehouse with shortName: " + shortName + " retrieved successfully");
            } else {
                Log.warn("No Warehouse found with shortName: " + shortName);
            }
            return result;
        } catch (Exception e) {
            Log.error("Error while retrieving Warehouse by shortName: " + shortName, e);
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }
}

