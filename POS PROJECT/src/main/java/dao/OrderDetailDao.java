package dao;

import jakarta.persistence.*;
import java.util.List;
import model.OrderDetail;
public class OrderDetailDao implements GenericDao<OrderDetail> {

    private Class<OrderDetail> orderDetailClass;
    private EntityManager entityManager;

    public OrderDetailDao(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.orderDetailClass = OrderDetail.class;
    }

    @Override
    public void setClass(Class<OrderDetail> classToSet) {
        this.orderDetailClass = classToSet;
    }

    @Override
    public OrderDetail findById(long id) throws Exception {
        // Truy vấn OrderDetail theo ID
        return entityManager.find(orderDetailClass, id);
    }

    @Override
    public List<OrderDetail> findAll() throws Exception {
        // Truy vấn tất cả OrderDetail
        String query = "SELECT o FROM OrderDetail o";
        TypedQuery<OrderDetail> typedQuery = entityManager.createQuery(query, orderDetailClass);
        return typedQuery.getResultList();
    }

    @Override
    public boolean create(OrderDetail entity) throws Exception {
        try {
            // Lưu OrderDetail mới
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
    public boolean update(OrderDetail entity) throws Exception {
        try {
            // Cập nhật OrderDetail
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
    public boolean delete(OrderDetail entity) throws Exception {
        try {
            // Xóa OrderDetail
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
            // Xóa OrderDetail theo ID
            OrderDetail orderDetail = findById(entityId);
            if (orderDetail != null) {
                return delete(orderDetail);
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

    // Tìm OrderDetail theo customer
    public List<OrderDetail> findByCustomerId(Long customerId) throws Exception {
        String query = "SELECT o FROM OrderDetail o WHERE o.customer.customerId = :customerId";
        TypedQuery<OrderDetail> typedQuery = entityManager.createQuery(query, orderDetailClass);
        typedQuery.setParameter("customerId", customerId);
        return typedQuery.getResultList();
    }

    // Tìm OrderDetail theo product
    public List<OrderDetail> findByProductId(Long productId) throws Exception {
        String query = "SELECT o FROM OrderDetail o WHERE o.product.productId = :productId";
        TypedQuery<OrderDetail> typedQuery = entityManager.createQuery(query, orderDetailClass);
        typedQuery.setParameter("productId", productId);
        return typedQuery.getResultList();
    }

    // Tìm OrderDetail theo trạng thái
    public List<OrderDetail> findByStatus(String status) throws Exception {
        String query = "SELECT o FROM OrderDetail o WHERE o.status = :status";
        TypedQuery<OrderDetail> typedQuery = entityManager.createQuery(query, orderDetailClass);
        typedQuery.setParameter("status", status);
        return typedQuery.getResultList();
    }
}

