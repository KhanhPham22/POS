package service;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Payment;
import dao.PaymentDao;
import service.PaymentServiceImpl;

public class PaymentServiceImplTest {
	private PaymentDao mockPaymentDao;
    private PaymentServiceImpl paymentService;

    @BeforeEach
    public void setUp() {
        mockPaymentDao = mock(PaymentDao.class);
        paymentService = new PaymentServiceImpl(mockPaymentDao);
    }
    @Test
    public void testCreatePayment() throws Exception {
        Payment payment = new Payment();
        when(mockPaymentDao.create(payment)).thenReturn(true);

        boolean result = paymentService.createPayment(payment);

        assertTrue(result);
        verify(mockPaymentDao).create(payment);
    }

    @Test
    public void testDeletePayment() throws Exception {
        long id = 1L;
        when(mockPaymentDao.deleteById(id)).thenReturn(true);

        boolean result = paymentService.deletePayment(id);

        assertTrue(result);
        verify(mockPaymentDao).deleteById(id);
    }

    @Test
    public void testUpdatePayment() throws Exception {
        Payment payment = new Payment();
        when(mockPaymentDao.update(payment)).thenReturn(true);

        boolean result = paymentService.updatePayment(payment);

        assertTrue(result);
        verify(mockPaymentDao).update(payment);
    }

    @Test
    public void testGetPayment() throws Exception {
        long id = 1L;
        Payment payment = new Payment();
        when(mockPaymentDao.findById(id)).thenReturn(payment);

        Payment result = paymentService.getPayment(id);

        assertNotNull(result);
        assertEquals(payment, result);
        verify(mockPaymentDao).findById(id);
    }

//    @Test
//    public void testGetAllPayments() throws Exception {
//        List<Payment> payments = Arrays.asList(new Payment(), new Payment());
//        when(mockPaymentDao.findAll()).thenReturn(payments);
//
//        List<Payment> result = paymentService.getAllPayments();
//
//        assertNotNull(result);
//        assertEquals(2, result.size());
//        verify(mockPaymentDao).findAll();
//    }

    @Test
    public void testGetPaymentByCustomerName() throws Exception {
        String customerName = "John Doe";
        List<Payment> payments = Arrays.asList(new Payment());
        when(mockPaymentDao.findByCustomerName(customerName)).thenReturn(payments);

        List<Payment> result = paymentService.getPaymentByCustomerName(customerName);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(mockPaymentDao).findByCustomerName(customerName);
    }

    // --- Exception Tests ---

    @Test
    public void testCreatePaymentException() throws Exception {
        Payment payment = new Payment();
        when(mockPaymentDao.create(payment)).thenThrow(new RuntimeException("DB error"));

        boolean result = paymentService.createPayment(payment);

        assertFalse(result);
        verify(mockPaymentDao).create(payment);
    }

    @Test
    public void testDeletePaymentException() throws Exception {
        long id = 1L;
        when(mockPaymentDao.deleteById(id)).thenThrow(new RuntimeException("DB error"));

        boolean result = paymentService.deletePayment(id);

        assertFalse(result);
        verify(mockPaymentDao).deleteById(id);
    }

    @Test
    public void testUpdatePaymentException() throws Exception {
        Payment payment = new Payment();
        when(mockPaymentDao.update(payment)).thenThrow(new RuntimeException("DB error"));

        boolean result = paymentService.updatePayment(payment);

        assertFalse(result);
        verify(mockPaymentDao).update(payment);
    }

    @Test
    public void testGetPaymentException() throws Exception {
        long id = 1L;
        when(mockPaymentDao.findById(id)).thenThrow(new RuntimeException("DB error"));

        Payment result = paymentService.getPayment(id);

        assertNull(result);
        verify(mockPaymentDao).findById(id);
    }

//    @Test
//    public void testGetAllPaymentsException() throws Exception {
//        when(mockPaymentDao.findAll()).thenThrow(new RuntimeException("DB error"));
//
//        List<Payment> result = paymentService.getAllPayments();
//
//        assertNull(result);
//        verify(mockPaymentDao).findAll();
//    }

    @Test
    public void testGetPaymentByCustomerNameException() throws Exception {
        String customerName = "Jane Smith";
        when(mockPaymentDao.findByCustomerName(customerName)).thenThrow(new RuntimeException("DB error"));

        List<Payment> result = paymentService.getPaymentByCustomerName(customerName);

        assertNull(result);
        verify(mockPaymentDao).findByCustomerName(customerName);
    }
}
