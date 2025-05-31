package dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import model.OrderDetail;
import model.OrderItem;
import model.Product;
import util.HibernateUtil;

/**
 * OrderDetailDao is a DAO class for performing CRUD operations
 * and custom queries on OrderDetail entities using Hibernate.
 */
public class OrderDetailDao implements GenericDao<OrderDetail> {

    private static final Logger Log = LogManager.getLogger(OrderDetailDao.class);

    private SessionFactory sessionFactory;
    private Class<OrderDetail> OrderDetail;

    // Constructor initializes the Hibernate session factory
    public OrderDetailDao() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    /**
     * Sets the entity class used in generic DAO operations.
     */
    @Override
    public void setClass(Class<OrderDetail> OrderDetail) {
        this.OrderDetail = OrderDetail;
    }

    /**
     * Persists a new OrderDetail entity to the database.
     */
    @Override
    public boolean create(OrderDetail orderDetail) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            for (OrderItem item : orderDetail.getItems()) {
                if (item.getProduct() == null || item.getProduct().getId() <= 0) {
                    Log.error("Invalid product in OrderItem: " + (item.getProduct() == null ? "null" : item.getProduct().getId()));
                    throw new IllegalStateException("OrderItem contains invalid product");
                }
                // Merge the product to ensure it's managed
                item.setProduct((Product) session.merge(item.getProduct()));
                Log.info("Merged Product for OrderItem: ID=" + item.getProduct().getId() + ", Name=" + item.getProduct().getName());
            }
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

    /**
     * Retrieves an OrderDetail entity by its ID.
     */
    @Override
    public OrderDetail findById(long id) throws Exception {
        Session session = sessionFactory.openSession();
        try {
            OrderDetail orderDetail = session.get(OrderDetail.class, id); // Fetch entity by ID
            Log.info("OrderDetail with id: " + id + " retrieved successfully from database");
            return orderDetail;
        } catch (Exception e) {
            Log.error("Database error while retrieving OrderDetail with id: " + id, e);
            throw e;
        } finally {
            session.close();
        }
    }

    /**
     * Retrieves a paginated list of OrderDetail records.
     *
     * @param pageNumber the current page number (starting from 1)
     * @param pageSize   the number of records per page
     */
    @Override
    public List<OrderDetail> findAll(int pageNumber, int pageSize) throws Exception {
        Session session = sessionFactory.openSession();
        try {
            int offset = (pageNumber - 1) * pageSize;
            
            // Modified query to join fetch the customer
            List<OrderDetail> orderDetails = session.createQuery(
                "SELECT DISTINCT o FROM OrderDetail o LEFT JOIN FETCH o.customer", OrderDetail.class)
                .setFirstResult(offset)
                .setMaxResults(pageSize)
                .list();
                
            return orderDetails;
        } catch (Exception e) {
            Log.error("Error while retrieving OrderDetails with pagination from database", e);
            throw e;
        } finally {
            if (session != null)
                session.close();
        }
    }

    /**
     * Updates an existing OrderDetail entity in the database.
     */
    @Override
    public boolean update(OrderDetail orderDetail) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(orderDetail); // Update the OrderDetail object
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

    /**
     * Deletes an OrderDetail entity by its ID.
     */
    @Override
    public boolean deleteById(long id) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            OrderDetail orderDetail = session.get(OrderDetail.class, id); // Find entity by ID
            session.delete(orderDetail); // Delete it
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

    /**
     * Deletes a given OrderDetail entity.
     */
    @Override
    public boolean delete(OrderDetail orderDetail) throws Exception {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(orderDetail); // Delete the object
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

    /**
     * Retrieves all OrderDetails for a given customer name.
     *
     * @param customerName the name of the customer
     * @return list of matching OrderDetails
     */
    public List<OrderDetail> findByCustomerName(String customerName) throws Exception {
        Session session = sessionFactory.openSession();
        try {
            String hql = "SELECT DISTINCT o FROM OrderDetail o LEFT JOIN FETCH o.customer " +
                         "WHERE LOWER(o.customer.personFirstName) LIKE :name OR " +
                         "LOWER(o.customer.personMiddleName) LIKE :name OR " +
                         "LOWER(o.customer.personLastName) LIKE :name";
            
            List<OrderDetail> results = session.createQuery(hql, OrderDetail.class)
                .setParameter("name", "%" + customerName.toLowerCase() + "%")
                .list();
                
            return results;
        } catch (Exception e) {
            Log.error("Database error while retrieving OrderDetails for customer name: " + customerName, e);
            throw e;
        } finally {
            session.close();
        }
    }
    
    public int getItemCountForOrder(long orderId) throws SQLException {
        Session session = sessionFactory.openSession();
        try {
            // Using HQL to count items for an order
            String hql = "SELECT COUNT(*) FROM OrderItem oi WHERE oi.order.id = :orderId";
            Long count = (Long) session.createQuery(hql)
                                     .setParameter("orderId", orderId)
                                     .uniqueResult();
            return count != null ? count.intValue() : 0;
        } finally {
            session.close();
        }
    }
}
