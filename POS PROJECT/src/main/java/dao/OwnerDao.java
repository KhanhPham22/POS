package dao;

import jakarta.persistence.*;
import java.util.List;
import model.Owner;
public class OwnerDao implements GenericDao<Owner> {

    private Class<Owner> ownerClass;
    private EntityManager entityManager;

    public OwnerDao(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.ownerClass = Owner.class;
    }

    @Override
    public void setClass(Class<Owner> classToSet) {
        this.ownerClass = classToSet;
    }

    @Override
    public Owner findById(long id) throws Exception {
        // Truy vấn Owner theo ID
        return entityManager.find(ownerClass, id);
    }

    @Override
    public List<Owner> findAll() throws Exception {
        // Truy vấn tất cả Owner
        String query = "SELECT o FROM Owner o";
        TypedQuery<Owner> typedQuery = entityManager.createQuery(query, ownerClass);
        return typedQuery.getResultList();
    }

    @Override
    public boolean create(Owner entity) throws Exception {
        try {
            // Lưu Owner mới
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
    public boolean update(Owner entity) throws Exception {
        try {
            // Cập nhật Owner
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
    public boolean delete(Owner entity) throws Exception {
        try {
            // Xóa Owner
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
            // Xóa Owner theo ID
            Owner owner = findById(entityId);
            if (owner != null) {
                return delete(owner);
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

    // Tìm Owner theo email (unique)
    public Owner findByEmail(String email) throws Exception {
        String query = "SELECT o FROM Owner o WHERE o.email = :email";
        TypedQuery<Owner> typedQuery = entityManager.createQuery(query, ownerClass);
        typedQuery.setParameter("email", email);
        List<Owner> results = typedQuery.getResultList();
        return results.isEmpty() ? null : results.get(0); // Trả về Owner đầu tiên nếu có
    }

    // Tìm Owner theo phone (unique)
    public Owner findByPhone(String phone) throws Exception {
        String query = "SELECT o FROM Owner o WHERE o.phone = :phone";
        TypedQuery<Owner> typedQuery = entityManager.createQuery(query, ownerClass);
        typedQuery.setParameter("phone", phone);
        List<Owner> results = typedQuery.getResultList();
        return results.isEmpty() ? null : results.get(0); // Trả về Owner đầu tiên nếu có
    }
}

