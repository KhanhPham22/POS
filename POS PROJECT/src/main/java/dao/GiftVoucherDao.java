package dao;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import model.GiftVoucher;
import model.Customer;
public class GiftVoucherDao implements GenericDao<GiftVoucher> {

    private Class<GiftVoucher> giftVoucherClass;
    private EntityManager entityManager;

    public GiftVoucherDao(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.giftVoucherClass = GiftVoucher.class;
    }

    @Override
    public void setClass(Class<GiftVoucher> classToSet) {
        this.giftVoucherClass = classToSet;
    }

    @Override
    public GiftVoucher findById(long id) throws Exception {
        // Truy vấn GiftVoucher theo ID
        return entityManager.find(giftVoucherClass, id);
    }

    @Override
    public List<GiftVoucher> findAll() throws Exception {
        // Truy vấn tất cả GiftVouchers
        String query = "SELECT gv FROM GiftVoucher gv";
        TypedQuery<GiftVoucher> typedQuery = entityManager.createQuery(query, giftVoucherClass);
        return typedQuery.getResultList();
    }

    @Override
    public boolean create(GiftVoucher entity) throws Exception {
        try {
            // Lưu GiftVoucher mới
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
    public boolean update(GiftVoucher entity) throws Exception {
        try {
            // Cập nhật GiftVoucher
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
    public boolean delete(GiftVoucher entity) throws Exception {
        try {
            // Xóa GiftVoucher
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
            // Xóa GiftVoucher theo ID
            GiftVoucher giftVoucher = findById(entityId);
            if (giftVoucher != null) {
                return delete(giftVoucher);
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

    // Tìm GiftVoucher theo khách hàng
    public List<GiftVoucher> findByCustomer(Customer customer) throws Exception {
        String query = "SELECT gv FROM GiftVoucher gv WHERE gv.customer = :customer";
        TypedQuery<GiftVoucher> typedQuery = entityManager.createQuery(query, giftVoucherClass);
        typedQuery.setParameter("customer", customer);
        return typedQuery.getResultList();
    }

    // Tìm GiftVoucher theo trạng thái giảm giá (discountStatus)
    public List<GiftVoucher> findByDiscountStatus(Boolean discountStatus) throws Exception {
        String query = "SELECT gv FROM GiftVoucher gv WHERE gv.discountStatus = :discountStatus";
        TypedQuery<GiftVoucher> typedQuery = entityManager.createQuery(query, giftVoucherClass);
        typedQuery.setParameter("discountStatus", discountStatus);
        return typedQuery.getResultList();
    }

    // Tìm GiftVoucher theo ngày hết hạn (endDate)
    public List<GiftVoucher> findByEndDate(Date endDate) throws Exception {
        String query = "SELECT gv FROM GiftVoucher gv WHERE gv.endDate = :endDate";
        TypedQuery<GiftVoucher> typedQuery = entityManager.createQuery(query, giftVoucherClass);
        typedQuery.setParameter("endDate", endDate);
        return typedQuery.getResultList();
    }
}

