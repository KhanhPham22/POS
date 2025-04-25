package model;

import java.util.Date;

public class Invoice extends BaseEntity {

    private OrderDetail order;
    private Customer customer; // Changed from 'user' to 'customer'
    private Employee employee;
    private Double totalPrice;
    private Double discount = 0.0; // Default is 0 if no discount
    private Double finalPrice;
    private String paymentMethod;
    private Date invoiceDay = new Date(); // Default to the current date
    private String status;

    // Default constructor
    public Invoice() {}

    // Constructor with all fields
    public Invoice(long id, OrderDetail order, Customer customer, Employee employee, 
                   Double totalPrice, Double discount, Double finalPrice, String paymentMethod, 
                   Date invoiceDay, String status) {
        setId(id); // Use BaseEntity's id
        this.order = order;
        this.customer = customer;
        this.employee = employee;
        this.totalPrice = totalPrice;
        this.discount = discount;
        this.finalPrice = finalPrice;
        this.paymentMethod = paymentMethod;
        this.invoiceDay = invoiceDay;
        this.status = status;
    }

    // Getter and Setter methods
    public OrderDetail getOrder() {
        return order;
    }

    public void setOrder(OrderDetail order) {
        this.order = order;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Date getInvoiceDay() {
        return invoiceDay;
    }

    public void setInvoiceDay(Date invoiceDay) {
        this.invoiceDay = invoiceDay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}