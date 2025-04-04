package dao;

import java.util.List;

import model.Payment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import util.HibernateUtil;

public class PaymentDao implements GenericDao<Payment> {

    private static final Logger Log = LogManager.getLogger(PaymentDao.class);

    private SessionFactory sessionFactory;
    private Class<Payment> Payment;

    public PaymentDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public void setClass(Class<Payment> Payment) {
        this.Payment = Payment;
    }

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

    @Override
    public List<Payment> findAll() throws Exception {
        Session session = sessionFactory.openSession();
        try {
            List<Payment> payments = session.createQuery("from Payment", Payment.class).list();
            Log.info("All Payments retrieved successfully");
            return payments;
        } catch (Exception e) {
            Log.error("Error while retrieving all Payments", e);
            throw e;
        } finally {
            session.close();
        }
    }

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
}
