package dao;

import model.Customer;
import model.Feedback;
import jakarta.persistence.*;
import java.util.List;

public class FeedbackDao implements GenericDao<Feedback> {

    private Class<Feedback> feedbackClass;
    private EntityManager entityManager;

    public FeedbackDao(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.feedbackClass = Feedback.class;
    }

    @Override
    public void setClass(Class<Feedback> classToSet) {
        this.feedbackClass = classToSet;
    }

    @Override
    public Feedback findById(long id) throws Exception {
        // Truy vấn Feedback theo ID
        return entityManager.find(feedbackClass, id);
    }

    @Override
    public List<Feedback> findAll() throws Exception {
        // Truy vấn tất cả Feedback
        String query = "SELECT f FROM Feedback f";
        TypedQuery<Feedback> typedQuery = entityManager.createQuery(query, feedbackClass);
        return typedQuery.getResultList();
    }

    @Override
    public boolean create(Feedback entity) throws Exception {
        try {
            // Lưu Feedback mới
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
    public boolean update(Feedback entity) throws Exception {
        try {
            // Cập nhật Feedback
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
    public boolean delete(Feedback entity) throws Exception {
        try {
            // Xóa Feedback
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
            // Xóa Feedback theo ID
            Feedback feedback = findById(entityId);
            if (feedback != null) {
                return delete(feedback);
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

    // Tìm Feedback theo customer
    public List<Feedback> findByCustomer(Customer customer) throws Exception {
        String query = "SELECT f FROM Feedback f WHERE f.customer = :customer";
        TypedQuery<Feedback> typedQuery = entityManager.createQuery(query, feedbackClass);
        typedQuery.setParameter("customer", customer);
        return typedQuery.getResultList();
    }

    // Tìm Feedback theo rating
    public List<Feedback> findByRating(Integer rating) throws Exception {
        String query = "SELECT f FROM Feedback f WHERE f.rating = :rating";
        TypedQuery<Feedback> typedQuery = entityManager.createQuery(query, feedbackClass);
        typedQuery.setParameter("rating", rating);
        return typedQuery.getResultList();
    }
}

