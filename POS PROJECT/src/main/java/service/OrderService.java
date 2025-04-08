package service;

import model.OrderDetail;
import java.util.List;

public interface OrderService {
    boolean createOrder(OrderDetail order);

    boolean updateOrder(OrderDetail order);

    boolean deleteOrderById(long orderId);

    boolean deleteOrder(OrderDetail order);

    OrderDetail getOrderById(long orderId);

    List<OrderDetail> getAllOrders();
}

