package dao;

import model.Category;
import jakarta.persistence.*;
import java.util.List;

public class CategoryDao implements GenericDao<Category> {

    private Class<Category> categoryClass;
    private EntityManager entityManager;

    public CategoryDao(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.categoryClass = Category.class;
    }

    @Override
    public void setClass(Class<Category> classToSet) {
        this.categoryClass = classToSet;
    }

    @Override
    public Category findById(long id) throws Exception {
        // Truy vấn Category theo ID
        return entityManager.find(categoryClass, id);
    }

    @Override
    public List<Category> findAll() throws Exception {
        // Truy vấn tất cả Category
        String query = "SELECT c FROM Category c";
        TypedQuery<Category> typedQuery = entityManager.createQuery(query, categoryClass);
        return typedQuery.getResultList();
    }

    @Override
    public boolean create(Category entity) throws Exception {
        try {
            // Lưu Category mới
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
    public boolean update(Category entity) throws Exception {
        try {
            // Cập nhật Category
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
    public boolean delete(Category entity) throws Exception {
        try {
            // Xóa Category
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
            // Xóa Category theo ID
            Category category = findById(entityId);
            if (category != null) {
                return delete(category);
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

    // Tìm Category theo tên (unique)
    public Category findByName(String name) throws Exception {
        String query = "SELECT c FROM Category c WHERE c.name = :name";
        TypedQuery<Category> typedQuery = entityManager.createQuery(query, categoryClass);
        typedQuery.setParameter("name", name);
        List<Category> results = typedQuery.getResultList();
        return results.isEmpty() ? null : results.get(0); // Trả về Category đầu tiên nếu có
        	
}
}