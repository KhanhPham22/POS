package dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import model.OrderDetail;
import util.HibernateUtil;

public class OrderDetailDao implements GenericDao<OrderDetail> {

    private static final Logger Log = LogManager.getLogger(OrderDetailDao.class);

    private SessionFactory sessionFactory;
    private Class<OrderDetail> OrderDetail;

    public OrderDetailDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public void setClass(Class<OrderDetail> OrderDetail) {
        this.OrderDetail = OrderDetail;
    }

    @Override
    public boolean create(OrderDetail orderDetail) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(orderDetail);
            transaction.commit();
            Log.info("OrderDetail persisted in database successfully");
            return true;
        } catch (Exception e) {
            Log.error("Database error while persisting OrderDetail", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public OrderDetail findById(long id) throws Exception {
        Session session = sessionFactory.openSession();
        try {
            OrderDetail orderDetail = session.get(OrderDetail.class, id);
            Log.info("OrderDetail with id: " + id + " retrieved successfully from database");
            return orderDetail;
        } catch (Exception e) {
            Log.error("Database error while retrieving OrderDetail with id: " + id, e);
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public List<OrderDetail> findAll() throws Exception {
        Session session = sessionFactory.openSession();
        try {
            List<OrderDetail> orderDetails = session.createQuery("from OrderDetail", OrderDetail.class).list();
            Log.info("All OrderDetails retrieved successfully from database");
            return orderDetails;
        } catch (Exception e) {
            Log.error("Error while retrieving OrderDetails from database", e);
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean update(OrderDetail orderDetail) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(orderDetail);
            transaction.commit();
            Log.info("OrderDetail updated successfully in database");
            return true;
        } catch (Exception e) {
            Log.error("Database error while updating OrderDetail", e);
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
            OrderDetail orderDetail = session.get(OrderDetail.class, id);
            session.delete(orderDetail);
            transaction.commit();
            Log.info("OrderDetail with id: " + id + " deleted successfully from database");
            return true;
        } catch (Exception e) {
            Log.error("Database error while deleting OrderDetail with id: " + id, e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public boolean delete(OrderDetail orderDetail) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(orderDetail);
            transaction.commit();
            Log.info("OrderDetail deleted successfully from database");
            return true;
        } catch (Exception e) {
            Log.error("Database error while deleting OrderDetail", e);
            if (transaction != null) transaction.rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
