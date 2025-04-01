package dao;

import jakarta.persistence.*;
import model.Supplier;
import model.WarehouseImport;

import java.util.List;

public class WarehouseImportDao implements GenericDao<WarehouseImport> {

    private Class<WarehouseImport> warehouseImportClass;
    private EntityManager entityManager;

    public WarehouseImportDao(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.warehouseImportClass = WarehouseImport.class;
    }

    @Override
    public void setClass(Class<WarehouseImport> classToSet) {
        this.warehouseImportClass = classToSet;
    }

    @Override
    public WarehouseImport findById(long id) throws Exception {
        // Truy vấn WarehouseImport theo ID
        return entityManager.find(warehouseImportClass, id);
    }

    @Override
    public List<WarehouseImport> findAll() throws Exception {
        // Truy vấn tất cả WarehouseImport
        String query = "SELECT w FROM WarehouseImport w";
        TypedQuery<WarehouseImport> typedQuery = entityManager.createQuery(query, warehouseImportClass);
        return typedQuery.getResultList();
    }

    @Override
    public boolean create(WarehouseImport entity) throws Exception {
        try {
            // Lưu WarehouseImport mới
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e; // Hoặc xử lý lỗi theo cách của bạn
        }
    }

    @Override
    public boolean update(WarehouseImport entity) throws Exception {
        try {
            // Cập nhật WarehouseImport
            entityManager.getTransaction().begin();
            entityManager.merge(entity);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public boolean delete(WarehouseImport entity) throws Exception {
        try {
            // Xóa WarehouseImport
            entityManager.getTransaction().begin();
            entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    @Override
    public boolean deleteById(long entityId) throws Exception {
        try {
            // Xóa WarehouseImport theo ID
            WarehouseImport warehouseImport = findById(entityId);
            if (warehouseImport != null) {
                return delete(warehouseImport);
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

    // Tìm WarehouseImport theo nhà cung cấp (Supplier)
    public List<WarehouseImport> findBySupplier(Supplier supplier) throws Exception {
        String query = "SELECT w FROM WarehouseImport w WHERE w.supplier = :supplier";
        TypedQuery<WarehouseImport> typedQuery = entityManager.createQuery(query, warehouseImportClass);
        typedQuery.setParameter("supplier", supplier);
        return typedQuery.getResultList();
    }

    // Tìm WarehouseImport theo trạng thái
    public List<WarehouseImport> findByStatus(String status) throws Exception {
        String query = "SELECT w FROM WarehouseImport w WHERE w.status = :status";
        TypedQuery<WarehouseImport> typedQuery = entityManager.createQuery(query, warehouseImportClass);
        typedQuery.setParameter("status", status);
        return typedQuery.getResultList();
    }
}

