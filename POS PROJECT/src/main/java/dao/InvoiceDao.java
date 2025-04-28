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

 // Lấy danh sách tất cả hóa đơn với phân trang
    public List<Invoice> findAll(int pageNumber, int pageSize) throws Exception {
        try (Session session = sessionFactory.openSession()) {
            int offset = pageNumber * pageSize; // Tính toán offset dựa trên pageNumber và pageSize
            List<Invoice> invoices = session.createQuery("FROM Invoice", Invoice.class)
                    .setFirstResult(offset)
                    .setMaxResults(pageSize) // Áp dụng phân trang
                    .list();
            Log.info("All invoices retrieved successfully with pagination");
            return invoices;
        } catch (Exception e) {
            Log.error("Error retrieving invoices with pagination", e);
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
    
 // Tìm hóa đơn theo tên nhân viên
    public List<Invoice> findByEmployeeName(String employeeName) throws Exception {
        try (Session session = sessionFactory.openSession()) {
            List<Invoice> invoices = session.createQuery(
                    "FROM Invoice WHERE employee.name = :employeeName", Invoice.class)
                    .setParameter("employeeName", employeeName)
                    .list();
            Log.info("Invoices with employee name '" + employeeName + "' retrieved successfully");
            return invoices;
        } catch (Exception e) {
            Log.error("Error retrieving invoices with employee name: " + employeeName, e);
            throw e;
        }
    }

    // Tìm hóa đơn theo tên khách hàng
    public List<Invoice> findByCustomerName(String customerName) throws Exception {
        try (Session session = sessionFactory.openSession()) {
            List<Invoice> invoices = session.createQuery(
                    "FROM Invoice WHERE customer.name = :customerName", Invoice.class)
                    .setParameter("customerName", customerName)
                    .list();
            Log.info("Invoices with customer name '" + customerName + "' retrieved successfully");
            return invoices;
        } catch (Exception e) {
            Log.error("Error retrieving invoices with customer name: " + customerName, e);
            throw e;
        }
    }

}
