package dao;

import java.util.List;

import model.Payment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import util.HibernateUtil;

/**
 * PaymentDao is a DAO class used to manage Payment entities via Hibernate.
 * It provides CRUD operations and query methods such as finding by customer name.
 */
public class PaymentDao implements GenericDao<Payment> {

    private static final Logger Log = LogManager.getLogger(PaymentDao.class);

    private SessionFactory sessionFactory;
    private Class<Payment> Payment;

    public PaymentDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    /**
     * Sets the class type for generic operations (e.g., used in HQL).
     */
    public void setClass(Class<Payment> Payment) {
        this.Payment = Payment;
    }

    /**
     * Persists a new Payment entity to the database.
     */
    @Override
    public boolean create(Payment payment) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(payment);
            transaction.commit();
            Log.info("Payment persisted successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error while saving Payment", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Retrieves a Payment by its ID.
     */
    @Override
    public Payment findById(long id) throws Exception {
        Session session = sessionFactory.openSession();
        try {
            Payment payment = session.get(Payment.class, id);
            Log.info("Payment with id: " + id + " retrieved successfully");
            return payment;
        } catch (Exception e) {
            Log.error("Error while retrieving Payment", e);
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Retrieves a paginated list of all Payments.
     *
     * @param pageNumber page index starting from 1
     * @param pageSize   number of items per page
     */
    @Override
    public List<Payment> findAll(int pageNumber, int pageSize) throws Exception {
        Session session = null;
        try {
            session = sessionFactory.openSession();

            int offset = (pageNumber - 1) * pageSize;

            List<Payment> payments = session.createQuery("from Payment", Payment.class)
                                            .setFirstResult(offset)
                                            .setMaxResults(pageSize)
                                            .list();

            Log.info("All Payments retrieved successfully with pagination");
            return payments;
        } catch (Exception e) {
            Log.error("Error while retrieving all Payments with pagination", e);
            throw e;
        } finally {
            if (session != null)
                session.close();
        }
    }

    /**
     * Updates an existing Payment entity in the database.
     */
    @Override
    public boolean update(Payment payment) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(payment);
            transaction.commit();
            Log.info("Payment updated successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error while updating Payment", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Deletes a Payment by its ID.
     */
    @Override
    public boolean deleteById(long id) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Payment payment = session.get(Payment.class, id);
            session.delete(payment);
            transaction.commit();
            Log.info("Payment deleted successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error while deleting Payment by id", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Deletes a given Payment entity.
     */
    @Override
    public boolean delete(Payment payment) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(payment);
            transaction.commit();
            Log.info("Payment deleted successfully");
            return true;
        } catch (Exception e) {
            Log.error("Error while deleting Payment", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Retrieves a list of Payments by customer name.
     *
     * @param customerName the name of the customer associated with the payments
     */
    public List<Payment> findByCustomerName(String customerName) throws Exception {
        Session session = sessionFactory.openSession();
        try {
            String hql = "FROM Payment p WHERE p.customer.name = :customerName";
            List<Payment> payments = session.createQuery(hql)
                    .setParameter("customerName", customerName)
                    .list();
            Log.info("Payments retrieved successfully by customer name: " + customerName);
            return payments;
        } catch (Exception e) {
            Log.error("Error while retrieving Payments by customer name", e);
            throw e;
        } finally {
            session.close();
        }
    }
}
