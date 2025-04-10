package service;
import java.util.List;

import model.Payment;
public interface PaymentService {
	boolean createPayment(Payment payment);

	boolean deletePayment(long paymentId);

	boolean updatePayment(Payment payment) ;

	Payment getPayment(long paymentId) ;

	List<Payment> getAllPayments();
	
	List<Payment> getPaymentByCustomerName(String customerName);
}
