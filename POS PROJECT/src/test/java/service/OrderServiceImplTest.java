package service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.OrderDetail;
import dao.OrderDetailDao;
import service.OrderServiceImpl;

public class OrderServiceImplTest {

    // Mock of OrderDetailDao for testing without real database
    private OrderDetailDao mockOrderDao;

    // Service to be tested
    private OrderServiceImpl orderService;

    // Initialize the mock DAO and the service before each test
    @BeforeEach
    public void setUp() {
        mockOrderDao = mock(OrderDetailDao.class);
        orderService = new OrderServiceImpl(mockOrderDao);
    }

    // --- Success test cases ---

    // Test creating an order successfully
    @Test
    public void testCreateOrder() throws Exception {
        OrderDetail order = new OrderDetail();
        when(mockOrderDao.create(order)).thenReturn(true);

        boolean result = orderService.createOrder(order);

        assertTrue(result);
        verify(mockOrderDao).create(order);
    }

    // Test updating an order successfully
    @Test
    public void testUpdateOrder() throws Exception {
        OrderDetail order = new OrderDetail();
        when(mockOrderDao.update(order)).thenReturn(true);

        boolean result = orderService.updateOrder(order);

        assertTrue(result);
        verify(mockOrderDao).update(order);
    }

    // Test deleting an order by ID successfully
    @Test
    public void testDeleteOrderById() throws Exception {
        long orderId = 1L;
        when(mockOrderDao.deleteById(orderId)).thenReturn(true);

        boolean result = orderService.deleteOrderById(orderId);

        assertTrue(result);
        verify(mockOrderDao).deleteById(orderId);
    }

    // Test deleting an order object successfully
    @Test
    public void testDeleteOrder() throws Exception {
        OrderDetail order = new OrderDetail();
        when(mockOrderDao.delete(order)).thenReturn(true);

        boolean result = orderService.deleteOrder(order);

        assertTrue(result);
        verify(mockOrderDao).delete(order);
    }

    // Test retrieving an order by ID successfully
    @Test
    public void testGetOrderById() throws Exception {
        long orderId = 1L;
        OrderDetail order = new OrderDetail();
        when(mockOrderDao.findById(orderId)).thenReturn(order);

        OrderDetail result = orderService.getOrderById(orderId);

        assertNotNull(result);
        assertEquals(order, result);
        verify(mockOrderDao).findById(orderId);
    }

    // Test retrieving a paginated list of orders successfully
    @Test
    public void testGetAllOrders() throws Exception {
        List<OrderDetail> orders = Arrays.asList(new OrderDetail(), new OrderDetail());
        when(mockOrderDao.findAll(1, 10)).thenReturn(orders);

        List<OrderDetail> result = orderService.getAllOrders(1, 10);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(mockOrderDao).findAll(1, 10);
    }

    // Test retrieving orders by customer name successfully
    @Test
    public void testGetOrderByCustomerName() throws Exception {
        String customerName = "John Doe";
        List<OrderDetail> orders = Arrays.asList(new OrderDetail());
        when(mockOrderDao.findByCustomerName(customerName)).thenReturn(orders);

        List<OrderDetail> result = orderService.getOrderByCustomerName(customerName);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(mockOrderDao).findByCustomerName(customerName);
    }

    // --- Exception test cases ---

    // Test createOrder() throws an exception and returns false
    @Test
    public void testCreateOrderException() throws Exception {
        OrderDetail order = new OrderDetail();
        when(mockOrderDao.create(order)).thenThrow(new RuntimeException("DB error"));

        boolean result = orderService.createOrder(order);

        assertFalse(result);
        verify(mockOrderDao).create(order);
    }

    // Test updateOrder() throws an exception and returns false
    @Test
    public void testUpdateOrderException() throws Exception {
        OrderDetail order = new OrderDetail();
        when(mockOrderDao.update(order)).thenThrow(new RuntimeException("DB error"));

        boolean result = orderService.updateOrder(order);

        assertFalse(result);
        verify(mockOrderDao).update(order);
    }

    // Test deleteOrderById() throws an exception and returns false
    @Test
    public void testDeleteOrderByIdException() throws Exception {
        long orderId = 1L;
        when(mockOrderDao.deleteById(orderId)).thenThrow(new RuntimeException("DB error"));

        boolean result = orderService.deleteOrderById(orderId);

        assertFalse(result);
        verify(mockOrderDao).deleteById(orderId);
    }

    // Test deleteOrder() throws an exception and returns false
    @Test
    public void testDeleteOrderException() throws Exception {
        OrderDetail order = new OrderDetail();
        when(mockOrderDao.delete(order)).thenThrow(new RuntimeException("DB error"));

        boolean result = orderService.deleteOrder(order);

        assertFalse(result);
        verify(mockOrderDao).delete(order);
    }

    // Test getOrderById() throws an exception and returns null
    @Test
    public void testGetOrderByIdException() throws Exception {
        long orderId = 1L;
        when(mockOrderDao.findById(orderId)).thenThrow(new RuntimeException("DB error"));

        OrderDetail result = orderService.getOrderById(orderId);

        assertNull(result);
        verify(mockOrderDao).findById(orderId);
    }

    // Test getAllOrders() throws an exception and returns null
    @Test
    public void testGetAllOrdersException() throws Exception {
        when(mockOrderDao.findAll(1, 10)).thenThrow(new RuntimeException("DB error"));

        List<OrderDetail> result = orderService.getAllOrders(1, 10);

        assertNull(result);
        verify(mockOrderDao).findAll(1, 10);
    }

    // Test getOrderByCustomerName() throws an exception and returns null
    @Test
    public void testGetOrderByCustomerNameException() throws Exception {
        String customerName = "Jane Smith";
        when(mockOrderDao.findByCustomerName(customerName)).thenThrow(new RuntimeException("DB error"));

        List<OrderDetail> result = orderService.getOrderByCustomerName(customerName);

        assertNull(result);
        verify(mockOrderDao).findByCustomerName(customerName);
    }
}
