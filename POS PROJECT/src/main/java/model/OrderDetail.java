package model;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrderDetail extends BaseEntity {
    private Customer customer;
    private double totalAmount;
    private Date orderDate;
    private Set<OrderItem> items = new HashSet<>();


    // Getters and Setters
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Set<OrderItem> getItems() {
        return items;
    }

    public void setItems(Set<OrderItem> items) {
        this.items = items;
    }
    
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

}