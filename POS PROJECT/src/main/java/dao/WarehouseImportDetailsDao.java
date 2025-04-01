package dao;

import jakarta.persistence.*;
import model.Item;
import model.WarehouseImport;
import model.WarehouseImportDetails;

import java.util.List;

public class WarehouseImportDetailsDao implements GenericDao<WarehouseImportDetails> {

    private Class<WarehouseImportDetails> warehouseImportDetailsClass;
    private EntityManager entityManager;

    public WarehouseImportDetailsDao(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.warehouseImportDetailsClass = WarehouseImportDetails.class;
    }

    @Override
    public void setClass(Class<WarehouseImportDetails> classToSet) {
        this.warehouseImportDetailsClass = classToSet;
    }

    @Override
    public WarehouseImportDetails findById(long id) throws Exception {
        // Truy vấn WarehouseImportDetails theo ID
        return entityManager.find(warehouseImportDetailsClass, id);
    }

    @Override
    public List<WarehouseImportDetails> findAll() throws Exception {
        // Truy vấn tất cả WarehouseImportDetails
        String query = "SELECT w FROM WarehouseImportDetails w";
        TypedQuery<WarehouseImportDetails> typedQuery = entityManager.createQuery(query, warehouseImportDetailsClass);
        return typedQuery.getResultList();
    }

    @Override
    public boolean create(WarehouseImportDetails entity) throws Exception {
        try {
            // Lưu WarehouseImportDetails mới
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
    public boolean update(WarehouseImportDetails entity) throws Exception {
        try {
            // Cập nhật WarehouseImportDetails
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
    public boolean delete(WarehouseImportDetails entity) throws Exception {
        try {
            // Xóa WarehouseImportDetails
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
            // Xóa WarehouseImportDetails theo ID
            WarehouseImportDetails warehouseImportDetails = findById(entityId);
            if (warehouseImportDetails != null) {
                return delete(warehouseImportDetails);
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

    // Tìm WarehouseImportDetails theo WarehouseImport
    public List<WarehouseImportDetails> findByWarehouseImport(WarehouseImport warehouseImport) throws Exception {
        String query = "SELECT w FROM WarehouseImportDetails w WHERE w.warehouseImport = :warehouseImport";
        TypedQuery<WarehouseImportDetails> typedQuery = entityManager.createQuery(query, warehouseImportDetailsClass);
        typedQuery.setParameter("warehouseImport", warehouseImport);
        return typedQuery.getResultList();
    }

    // Tìm WarehouseImportDetails theo Item
    public List<WarehouseImportDetails> findByItem(Item item) throws Exception {
        String query = "SELECT w FROM WarehouseImportDetails w WHERE w.item = :item";
        TypedQuery<WarehouseImportDetails> typedQuery = entityManager.createQuery(query, warehouseImportDetailsClass);
        typedQuery.setParameter("item", item);
        return typedQuery.getResultList();
    }
}

