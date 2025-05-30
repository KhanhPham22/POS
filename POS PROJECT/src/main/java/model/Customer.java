package model;

import java.util.UUID;
import java.util.HashSet;
import java.util.Set;

public class Customer extends Person {

	private String customerNumber;
	private String description;
	private double points;
	private Set<Invoice> invoices = new HashSet<>();
	private Set<Payment> payments = new HashSet<>();
	private Set<GiftVoucher> giftVouchers = new HashSet<>();
	private Set<Feedback> feedback = new HashSet<>();
	private Set<OrderDetail> orderDetails = new HashSet<>();

	public Customer() {
	}

	public Customer(String firstName, String phone) {
		this.setPersonFirstName(firstName); // dùng method từ Person
		this.setPhone(phone); // dùng method từ Person
		this.points = 0.0;
	}

	// Tạo số customer tự động
	public void generateCustomerNumber() {
		this.customerNumber = "CUS-" + UUID.randomUUID().toString();
	}

	// Getter và Setter
	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public double getPoints() {
		return points;
	}

	public void setPoints(double points) {
		this.points = points;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// Getter/Setter cho tên và sđt kế thừa từ Person
	public String getCustomerFirstName() {
		return getPersonFirstName(); // dùng getter của Person
	}

	public void setCustomerFirstName(String firstName) {
		setPersonFirstName(firstName); // dùng setter của Person
	}

	public String getCustomerPhone() {
		return getPhone(); // dùng getter của Person
	}

	public void setCustomerPhone(String phone) {
		setPhone(phone); // dùng setter của Person
	}

	public Set<Invoice> getInvoices() {
		return invoices;
	}

	public void setInvoices(Set<Invoice> invoices) {
		this.invoices = invoices;
	}

	public Set<Payment> getPayments() {
		return payments;
	}

	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}

	public Set<GiftVoucher> getGiftVouchers() {
		return giftVouchers;
	}

	public void setGiftVouchers(Set<GiftVoucher> giftVouchers) {
		this.giftVouchers = giftVouchers;
	}

	public Set<Feedback> getFeedback() {
		return feedback;
	}

	public void setFeedback(Set<Feedback> feedback) {
		this.feedback = feedback;
	}

	public Set<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(Set<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}
}
