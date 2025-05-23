package service;

import dao.PaymentDao;
import model.Payment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

//Service implementation for managing Payment entities
public class PaymentServiceImpl implements PaymentService {

 // Logger for error tracking and debugging
 private static final Logger Log = LogManager.getLogger(PaymentServiceImpl.class);

 // Data access object for payment operations
 private final PaymentDao paymentDao;

 // Constructor injecting the DAO and setting the entity class
 public PaymentServiceImpl(PaymentDao paymentDao) {
     this.paymentDao = paymentDao;
     this.paymentDao.setClass(Payment.class);
 }

 // Create a new payment record
 @Override
 public boolean createPayment(Payment payment) {
     try {
         return paymentDao.create(payment);
     } catch (Exception e) {
         Log.error("Failed to create payment", e);
         return false;
     }
 }

 // Delete a payment by its ID
 @Override
 public boolean deletePayment(long paymentId) {
     try {
         return paymentDao.deleteById(paymentId);
     } catch (Exception e) {
         Log.error("Failed to delete payment with ID: " + paymentId, e);
         return false;
     }
 }

 // Update an existing payment
 @Override
 public boolean updatePayment(Payment payment) {
     try {
         return paymentDao.update(payment);
     } catch (Exception e) {
         Log.error("Failed to update payment", e);
         return false;
     }
 }

 // Retrieve a payment by its ID
 @Override
 public Payment getPayment(long paymentId) {
     try {
         return paymentDao.findById(paymentId);
     } catch (Exception e) {
         Log.error("Failed to get payment with ID: " + paymentId, e);
         return null;
     }
 }

 // Retrieve all payments with pagination support
 @Override
 public List<Payment> getAllPayments(int pageNumber, int pageSize) {
     try {
         return paymentDao.findAll(pageNumber, pageSize);
     } catch (Exception e) {
         Log.error("Failed to get all payments", e);
         return null;
     }
 }

 // Retrieve payments based on customer name
 @Override
 public List<Payment> getPaymentByCustomerName(String customerName) {
     try {
         return paymentDao.findByCustomerName(customerName);
     } catch (Exception e) {
         Log.error("Failed to get payments for customer name: " + customerName, e);
         return null;
     }
 }

}


