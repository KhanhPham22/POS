package dao;

import jakarta.persistence.*;
import model.Supplier;

import java.util.List;

public class SupplierDao implements GenericDao<Supplier> {

    private Class<Supplier> supplierClass;
    private EntityManager entityManager;

    public SupplierDao(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.supplierClass = Supplier.class;
    }

    @Override
    public void setClass(Class<Supplier> classToSet) {
        this.supplierClass = classToSet;
    }

    @Override
    public Supplier findById(long id) throws Exception {
        // Truy vấn Supplier theo ID
        return entityManager.find(supplierClass, id);
    }

    @Override
    public List<Supplier> findAll() throws Exception {
        // Truy vấn tất cả Supplier
        String query = "SELECT s FROM Supplier s";
        TypedQuery<Supplier> typedQuery = entityManager.createQuery(query, supplierClass);
        return typedQuery.getResultList();
    }

    @Override
    public boolean create(Supplier entity) throws Exception {
        try {
            // Lưu Supplier mới
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
    public boolean update(Supplier entity) throws Exception {
        try {
            // Cập nhật Supplier
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
    public boolean delete(Supplier entity) throws Exception {
        try {
            // Xóa Supplier
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
            // Xóa Supplier theo ID
            Supplier supplier = findById(entityId);
            if (supplier != null) {
                return delete(supplier);
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

    // Tìm Supplier theo tên (unique)
    public Supplier findByName(String name) throws Exception {
        String query = "SELECT s FROM Supplier s WHERE s.name = :name";
        TypedQuery<Supplier> typedQuery = entityManager.createQuery(query, supplierClass);
        typedQuery.setParameter("name", name);
        List<Supplier> results = typedQuery.getResultList();
        return results.isEmpty() ? null : results.get(0); // Trả về null nếu không tìm thấy
    }

    // Tìm Supplier theo mã số thuế (unique)
    public Supplier findByTaxCode(String taxCode) throws Exception {
        String query = "SELECT s FROM Supplier s WHERE s.taxCode = :taxCode";
        TypedQuery<Supplier> typedQuery = entityManager.createQuery(query, supplierClass);
        typedQuery.setParameter("taxCode", taxCode);
        List<Supplier> results = typedQuery.getResultList();
        return results.isEmpty() ? null : results.get(0); // Trả về null nếu không tìm thấy
    }

    // Lấy danh sách các Supplier có email cụ thể
    public List<Supplier> findByEmail(String email) throws Exception {
        String query = "SELECT s FROM Supplier s WHERE s.email = :email";
        TypedQuery<Supplier> typedQuery = entityManager.createQuery(query, supplierClass);
        typedQuery.setParameter("email", email);
        return typedQuery.getResultList();
    }
}

