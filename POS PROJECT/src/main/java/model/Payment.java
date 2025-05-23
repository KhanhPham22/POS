package model;

import java.util.Date;

public class Payment extends BaseEntity {

    private String paymentMethod;
    private Double amount;
    private String status;
    private OrderDetail order;
    private Customer customer;
    private Date paymentDate;

    // Default Constructor
    public Payment() {}

    // Constructor with all fields
    public Payment(long id, String paymentMethod, Double amount, String status, 
                   OrderDetail order, Customer customer, Date paymentDate) {
        setId(id); // Use BaseEntity's id
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.status = status;
        this.order = order;
        this.customer = customer;
        this.paymentDate = paymentDate;
    }

    // Getter and Setter Methods
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
}