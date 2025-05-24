package service;

import dao.OrderDetailDao;
import model.OrderDetail;
import model.OrderItem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Transaction;

import java.util.List;

public class OrderServiceImpl implements OrderService {

    // Logger for logging errors and important events
    private static final Logger Log = LogManager.getLogger(OrderServiceImpl.class);

    // DAO for performing CRUD operations on OrderDetail
    private final OrderDetailDao orderDao;

    // Constructor initializes DAO and sets the entity class to OrderDetail
    public OrderServiceImpl(OrderDetailDao orderDao) {
        this.orderDao =  orderDao;
        this.orderDao.setClass(OrderDetail.class);
    }

    // Create a new order, return true if successful, false if an exception occurs
    @Override
    public boolean createOrder(OrderDetail order) {
        try {
            // Ensure all order items have their order reference set
            for (OrderItem item : order.getItems()) {
                if (item.getOrder() == null) {
                    item.setOrder(order);
                }
            }
            return orderDao.create(order);
        } catch (Exception e) {
            Log.error("Failed to create order", e);
            return false;
        }
    }

    // Update an existing order, return true if successful, false if an exception occurs
    @Override
    public boolean updateOrder(OrderDetail order) {
        try {
            return orderDao.update(order);
        } catch (Exception e) {
            Log.error("Failed to update order", e);
            return false;
        }
    }

    // Delete an order by its ID, return true if successful, false if an exception occurs
    @Override
    public boolean deleteOrderById(long orderId) {
        try {
            return orderDao.deleteById(orderId);
        } catch (Exception e) {
            Log.error("Failed to delete order with ID: " + orderId, e);
            return false;
        }
    }

    // Delete an order by passing the order object, return true if successful, false otherwise
    @Override
    public boolean deleteOrder(OrderDetail order) {
        try {
            return orderDao.delete(order);
        } catch (Exception e) {
            Log.error("Failed to delete order", e);
            return false;
        }
    }

    // Retrieve an order by its ID. Returns the order object if found, null otherwise
    @Override
    public OrderDetail getOrderById(long orderId) {
        try {
            return orderDao.findById(orderId);
        } catch (Exception e) {
            Log.error("Failed to retrieve order with ID: " + orderId, e);
            return null;
        }
    }

    // Retrieve all orders with pagination. Returns a list or null if an error occurs
    @Override
    public List<OrderDetail> getAllOrders(int pageNumber, int pageSize) {
        try {
            return orderDao.findAll(pageNumber, pageSize);
        } catch (Exception e) {
            Log.error("Failed to retrieve all orders", e);
            return null;
        }
    }
    
    // Retrieve orders based on customer name. Returns a list or null if an error occurs
    @Override
    public List<OrderDetail> getOrderByCustomerName(String customerName) {
        try {
            return orderDao.findByCustomerName(customerName);
        } catch (Exception e) {
            Log.error("Failed to retrieve orders for customer name: " + customerName, e);
            return null;
        }
    }

}


