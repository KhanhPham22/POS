package dao;

import jakarta.persistence.*;
import java.util.List;
import model.Item;
public class ItemDao implements GenericDao<Item> {

    private Class<Item> itemClass;
    private EntityManager entityManager;

    public ItemDao(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.itemClass = Item.class;
    }

    @Override
    public void setClass(Class<Item> classToSet) {
        this.itemClass = classToSet;
    }

    @Override
    public Item findById(long id) throws Exception {
        // Truy vấn Item theo ID
        return entityManager.find(itemClass, id);
    }

    @Override
    public List<Item> findAll() throws Exception {
        // Truy vấn tất cả Item
        String query = "SELECT i FROM Item i";
        TypedQuery<Item> typedQuery = entityManager.createQuery(query, itemClass);
        return typedQuery.getResultList();
    }

    @Override
    public boolean create(Item entity) throws Exception {
        try {
            // Lưu Item mới
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
    public boolean update(Item entity) throws Exception {
        try {
            // Cập nhật Item
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
    public boolean delete(Item entity) throws Exception {
        try {
            // Xóa Item
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
            // Xóa Item theo ID
            Item item = findById(entityId);
            if (item != null) {
                return delete(item);
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

    // Tìm Item theo tên
    public List<Item> findByName(String name) throws Exception {
        String query = "SELECT i FROM Item i WHERE i.name = :name";
        TypedQuery<Item> typedQuery = entityManager.createQuery(query, itemClass);
        typedQuery.setParameter("name", name);
        return typedQuery.getResultList();
    }

    // Tìm Item theo loại
    public List<Item> findByType(String type) throws Exception {
        String query = "SELECT i FROM Item i WHERE i.type = :type";
        TypedQuery<Item> typedQuery = entityManager.createQuery(query, itemClass);
        typedQuery.setParameter("type", type);
        return typedQuery.getResultList();
    }
}

