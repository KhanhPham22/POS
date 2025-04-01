package dao;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

import model.Customer;
import model.Invoice;
public class InvoiceDao implements GenericDao<Invoice> {

    private Class<Invoice> invoiceClass;
    private EntityManager entityManager;

    public InvoiceDao(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.invoiceClass = Invoice.class;
    }

    @Override
    public void setClass(Class<Invoice> classToSet) {
        this.invoiceClass = classToSet;
    }

    @Override
    public Invoice findById(long id) throws Exception {
        // Truy vấn Invoice theo ID
        return entityManager.find(invoiceClass, id);
    }

    @Override
    public List<Invoice> findAll() throws Exception {
        // Truy vấn tất cả Invoice
        String query = "SELECT i FROM Invoice i";
        TypedQuery<Invoice> typedQuery = entityManager.createQuery(query, invoiceClass);
        return typedQuery.getResultList();
    }

    @Override
    public boolean create(Invoice entity) throws Exception {
        try {
            // Lưu Invoice mới
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
    public boolean update(Invoice entity) throws Exception {
        try {
            // Cập nhật Invoice
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
    public boolean delete(Invoice entity) throws Exception {
        try {
            // Xóa Invoice
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
            // Xóa Invoice theo ID
            Invoice invoice = findById(entityId);
            if (invoice != null) {
                return delete(invoice);
            }
            return false;
        } catch (Exception e) {
            throw e;
        }
    }

    // Tìm Invoice theo khách hàng
    public List<Invoice> findByCustomer(Customer customer) throws Exception {
        String query = "SELECT i FROM Invoice i WHERE i.customer = :customer";
        TypedQuery<Invoice> typedQuery = entityManager.createQuery(query, invoiceClass);
        typedQuery.setParameter("customer", customer);
        return typedQuery.getResultList();
    }

    // Tìm Invoice theo trạng thái
    public List<Invoice> findByStatus(String status) throws Exception {
        String query = "SELECT i FROM Invoice i WHERE i.status = :status";
        TypedQuery<Invoice> typedQuery = entityManager.createQuery(query, invoiceClass);
        typedQuery.setParameter("status", status);
        return typedQuery.getResultList();
    }

    // Tìm Invoice theo ngày xuất hóa đơn
    public List<Invoice> findByInvoiceDay(Date invoiceDay) throws Exception {
        String query = "SELECT i FROM Invoice i WHERE i.invoiceDay = :invoiceDay";
        TypedQuery<Invoice> typedQuery = entityManager.createQuery(query, invoiceClass);
        typedQuery.setParameter("invoiceDay", invoiceDay);
        return typedQuery.getResultList();
    }

    // Tìm Invoice theo phương thức thanh toán
    public List<Invoice> findByPaymentMethod(String paymentMethod) throws Exception {
        String query = "SELECT i FROM Invoice i WHERE i.paymentMethod = :paymentMethod";
        TypedQuery<Invoice> typedQuery = entityManager.createQuery(query, invoiceClass);
        typedQuery.setParameter("paymentMethod", paymentMethod);
        return typedQuery.getResultList();
    }
}

