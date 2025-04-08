package service;

import dao.PaymentDao;
import model.Payment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class PaymentServiceImpl implements PaymentService {

    private static final Logger Log = LogManager.getLogger(PaymentServiceImpl.class);
    private final PaymentDao paymentDao;

    public PaymentServiceImpl() {
        this.paymentDao = new PaymentDao();
        this.paymentDao.setClass(Payment.class);
    }

    @Override
    public boolean createPayment(Payment payment) {
        try {
            return paymentDao.create(payment);
        } catch (Exception e) {
            Log.error("Failed to create payment", e);
            return false;
        }
    }

    @Override
    public boolean deletePayment(long paymentId) {
        try {
            return paymentDao.deleteById(paymentId);
        } catch (Exception e) {
            Log.error("Failed to delete payment with ID: " + paymentId, e);
            return false;
        }
    }

    @Override
    public boolean updatePayment(Payment payment) {
        try {
            return paymentDao.update(payment);
        } catch (Exception e) {
            Log.error("Failed to update payment", e);
            return false;
        }
    }

    @Override
    public Payment getPayment(long paymentId) {
        try {
            return paymentDao.findById(paymentId);
        } catch (Exception e) {
            Log.error("Failed to get payment with ID: " + paymentId, e);
            return null;
        }
    }

    @Override
    public List<Payment> getAllPayments() {
        try {
            return paymentDao.findAll();
        } catch (Exception e) {
            Log.error("Failed to get all payments", e);
            return null;
        }
    }
}

