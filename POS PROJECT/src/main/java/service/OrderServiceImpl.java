package service;

import dao.OrderDetailDao;
import model.OrderDetail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class OrderServiceImpl implements OrderService {

    private static final Logger Log = LogManager.getLogger(OrderServiceImpl.class);
    private final OrderDetailDao orderDao;

    public OrderServiceImpl(OrderDetailDao orderDao) {
        this.orderDao =  orderDao;
        this.orderDao.setClass(OrderDetail.class);
    }

    @Override
    public boolean createOrder(OrderDetail order) {
        try {
            return orderDao.create(order);
        } catch (Exception e) {
            Log.error("Failed to create order", e);
            return false;
        }
    }

    @Override
    public boolean updateOrder(OrderDetail order) {
        try {
            return orderDao.update(order);
        } catch (Exception e) {
            Log.error("Failed to update order", e);
            return false;
        }
    }

    @Override
    public boolean deleteOrderById(long orderId) {
        try {
            return orderDao.deleteById(orderId);
        } catch (Exception e) {
            Log.error("Failed to delete order with ID: " + orderId, e);
            return false;
        }
    }

    @Override
    public boolean deleteOrder(OrderDetail order) {
        try {
            return orderDao.delete(order);
        } catch (Exception e) {
            Log.error("Failed to delete order", e);
            return false;
        }
    }

    @Override
    public OrderDetail getOrderById(long orderId) {
        try {
            return orderDao.findById(orderId);
        } catch (Exception e) {
            Log.error("Failed to retrieve order with ID: " + orderId, e);
            return null;
        }
    }

    @Override
    public List<OrderDetail> getAllOrders(int pageNumber, int pageSize) {
        try {
            return orderDao.findAll(pageNumber,pageSize);
        } catch (Exception e) {
            Log.error("Failed to retrieve all orders", e);
            return null;
        }
    }
    
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

