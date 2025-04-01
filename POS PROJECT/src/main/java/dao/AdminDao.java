package dao;
import model.Admin;
import javax.persistence.TypedQuery;

import jakarta.persistence.EntityManager;

import java.util.List;

public class AdminDao implements GenericDao<Admin> {

    private Class<Admin> adminClass;
    private EntityManager entityManager;

    public AdminDao(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.adminClass = Admin.class;
    }

    @Override
    public void setClass(Class<Admin> classToSet) {
        this.adminClass = classToSet;
    }

    @Override
    public Admin findById(long id) throws Exception {
        // Truy vấn Admin theo ID
        return entityManager.find(adminClass, id);
    }

    @Override
    public List<Admin> findAll() throws Exception {
        // Truy vấn tất cả Admin
        String query = "SELECT a FROM Admin a";
        TypedQuery<Admin> typedQuery = (TypedQuery<Admin>) entityManager.createQuery(query, adminClass);
        return typedQuery.getResultList();
    }

    @Override
    public boolean create(Admin entity) throws Exception {
        try {
            // Lưu Admin mới
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
    public boolean update(Admin entity) throws Exception {
        try {
            // Cập nhật Admin
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
    public boolean delete(Admin entity) throws Exception {
        try {
            // Xóa Admin
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
            // Xóa Admin theo ID
            Admin admin = findById(entityId);
            if (admin != null) {
                return delete(admin);
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

    // Tìm Admin theo email (unique)
    public Admin findByEmail(String email) throws Exception {
        String query = "SELECT a FROM Admin a WHERE a.email = :email";
        TypedQuery<Admin> typedQuery = (TypedQuery<Admin>) entityManager.createQuery(query, adminClass);
        typedQuery.setParameter("email", email);
        List<Admin> results = typedQuery.getResultList();
        return results.isEmpty() ? null : results.get(0); // Trả về Admin đầu tiên nếu có
    }
}

