package dao;

import jakarta.persistence.*;
import java.util.List;
import model.Payment;
public class PaymentDao implements GenericDao<Payment> {

    private Class<Payment> paymentClass;
    private EntityManager entityManager;

    public PaymentDao(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.paymentClass = Payment.class;
    }

    @Override
    public void setClass(Class<Payment> classToSet) {
        this.paymentClass = classToSet;
    }

    @Override
    public Payment findById(long id) throws Exception {
        // Truy vấn Payment theo ID
        return entityManager.find(paymentClass, id);
    }

    @Override
    public List<Payment> findAll() throws Exception {
        // Truy vấn tất cả Payment
        String query = "SELECT p FROM Payment p";
        TypedQuery<Payment> typedQuery = entityManager.createQuery(query, paymentClass);
        return typedQuery.getResultList();
    }

    @Override
    public boolean create(Payment entity) throws Exception {
        try {
            // Lưu Payment mới
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
    public boolean update(Payment entity) throws Exception {
        try {
            // Cập nhật Payment
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
    public boolean delete(Payment entity) throws Exception {
        try {
            // Xóa Payment
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
            // Xóa Payment theo ID
            Payment payment = findById(entityId);
            if (payment != null) {
                return delete(payment);
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

    // Tìm Payment theo Order ID
    public List<Payment> findByOrderId(long orderId) throws Exception {
        String query = "SELECT p FROM Payment p WHERE p.order.orderId = :orderId";
        TypedQuery<Payment> typedQuery = entityManager.createQuery(query, paymentClass);
        typedQuery.setParameter("orderId", orderId);
        return typedQuery.getResultList();
    }

    // Tìm Payment theo phương thức thanh toán
    public List<Payment> findByPaymentMethod(String paymentMethod) throws Exception {
        String query = "SELECT p FROM Payment p WHERE p.paymentMethod = :paymentMethod";
        TypedQuery<Payment> typedQuery = entityManager.createQuery(query, paymentClass);
        typedQuery.setParameter("paymentMethod", paymentMethod);
        return typedQuery.getResultList();
    }
}

