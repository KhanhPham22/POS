package dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import model.Invoice;
import util.HibernateUtil;

public class InvoiceDao {

    private static final Logger Log = LogManager.getLogger(InvoiceDao.class);

    private SessionFactory sessionFactory;

    public InvoiceDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    // Tạo mới hóa đơn
    public boolean create(Invoice invoice) throws Exception {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(invoice);
            transaction.commit();
            Log.info("Invoice saved successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error saving invoice", e);
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    // Tìm hóa đơn theo ID
    public Invoice findById(Long id) throws Exception {
        try (Session session = sessionFactory.openSession()) {
            Invoice invoice = session.get(Invoice.class, id);
            Log.info("Invoice with id " + id + " retrieved successfully");
            return invoice;
        } catch (Exception e) {
            Log.error("Error retrieving invoice with id: " + id, e);
            throw e;
        }
    }

    // Cập nhật hóa đơn
    public boolean update(Invoice invoice) throws Exception {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(invoice);
            transaction.commit();
            Log.info("Invoice updated successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error updating invoice", e);
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    // Xóa hóa đơn theo ID
    public boolean deleteById(Long id) throws Exception {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Invoice invoice = session.get(Invoice.class, id);
            if (invoice != null) {
                session.delete(invoice);
                transaction.commit();
                Log.info("Invoice with id " + id + " deleted successfully");
                return true;
            }
            return false;
        } catch (Exception e) {
            Log.error("Error deleting invoice with id: " + id, e);
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    // Xóa trực tiếp đối tượng Invoice
    public boolean delete(Invoice invoice) throws Exception {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.delete(invoice);
            transaction.commit();
            Log.info("Invoice deleted successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error deleting invoice", e);
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }

    // Lấy danh sách tất cả hóa đơn
    public List<Invoice> findAll() throws Exception {
        try (Session session = sessionFactory.openSession()) {
            List<Invoice> invoices = session.createQuery("FROM Invoice", Invoice.class).list();
            Log.info("All invoices retrieved successfully");
            return invoices;
        } catch (Exception e) {
            Log.error("Error retrieving all invoices", e);
            throw e;
        }
    }

    // Tìm hóa đơn theo phương thức thanh toán
    public List<Invoice> findByPaymentMethod(String paymentMethod) throws Exception {
        try (Session session = sessionFactory.openSession()) {
            List<Invoice> invoices = session.createQuery(
                    "FROM Invoice WHERE paymentMethod = :paymentMethod", Invoice.class)
                    .setParameter("paymentMethod", paymentMethod)
                    .list();
            Log.info("Invoices with payment method '" + paymentMethod + "' retrieved successfully");
            return invoices;
        } catch (Exception e) {
            Log.error("Error retrieving invoices with payment method: " + paymentMethod, e);
            throw e;
        }
    }

    // Tìm hóa đơn theo trạng thái
    public List<Invoice> findByStatus(String status) throws Exception {
        try (Session session = sessionFactory.openSession()) {
            List<Invoice> invoices = session.createQuery(
                    "FROM Invoice WHERE status = :status", Invoice.class)
                    .setParameter("status", status)
                    .list();
            Log.info("Invoices with status '" + status + "' retrieved successfully");
            return invoices;
        } catch (Exception e) {
            Log.error("Error retrieving invoices with status: " + status, e);
            throw e;
        }
    }

    // Tìm hóa đơn theo ngày lập
    public List<Invoice> findByInvoiceDate(java.util.Date date) throws Exception {
        try (Session session = sessionFactory.openSession()) {
            List<Invoice> invoices = session.createQuery(
                    "FROM Invoice WHERE DATE(invoiceDay) = :date", Invoice.class)
                    .setParameter("date", date)
                    .list();
            Log.info("Invoices with date '" + date + "' retrieved successfully");
            return invoices;
        } catch (Exception e) {
            Log.error("Error retrieving invoices by date: " + date, e);
            throw e;
        }
    }
}
