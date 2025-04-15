package model;

import java.util.Date;


public class Dashboard extends BaseEntity {

    private Double totalSalary;
    private Double totalRevenue;
    private Integer totalOrders;
    private Integer totalUsers;
    private Integer totalProducts;
    private Double dayRevenue;
    private Double monthRevenue;
    private Double yearRevenue;
    private Integer pendingOrders;
    private Integer totalFeedback;
    private Double averageRating;
    private Date timestamp = new Date(); // Default to current time

    private Store store; // Quan hệ với Store qua store_id
    private String storeName; // Snapshot tên store
    
    private Owner owner;
    
    // Default constructor
    public Dashboard() {}

    // Constructor with all fields
    public Dashboard(Long id, Double totalSalary, Double totalRevenue, Integer totalOrders,
                     Integer totalUsers, Integer totalProducts, Double dayRevenue, Double monthRevenue,
                     Double yearRevenue, Integer pendingOrders, Integer totalFeedback, Double averageRating,
                     Date timestamp, Store store, String storeName) {
        setId(id); // Use BaseEntity's id
        this.totalSalary = totalSalary;
        this.totalRevenue = totalRevenue;
        this.totalOrders = totalOrders;
        this.totalUsers = totalUsers;
        this.totalProducts = totalProducts;
        this.dayRevenue = dayRevenue;
        this.monthRevenue = monthRevenue;
        this.yearRevenue = yearRevenue;
        this.pendingOrders = pendingOrders;
        this.totalFeedback = totalFeedback;
        this.averageRating = averageRating;
        this.timestamp = timestamp;
        this.store = store;
        this.storeName = storeName;
    }

    

	
    public Double getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(Double totalSalary) {
        this.totalSalary = totalSalary;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Integer getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Integer getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(Integer totalUsers) {
        this.totalUsers = totalUsers;
    }

    public Integer getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(Integer totalProducts) {
        this.totalProducts = totalProducts;
    }

    public Double getDayRevenue() {
        return dayRevenue;
    }

    public void setDayRevenue(Double dayRevenue) {
        this.dayRevenue = dayRevenue;
    }

    public Double getMonthRevenue() {
        return monthRevenue;
    }

    public void setMonthRevenue(Double monthRevenue) {
        this.monthRevenue = monthRevenue;
    }

    public Double getYearRevenue() {
        return yearRevenue;
    }

    public void setYearRevenue(Double yearRevenue) {
        this.yearRevenue = yearRevenue;
    }

    public Integer getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(Integer pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public Integer getTotalFeedback() {
        return totalFeedback;
    }

    public void setTotalFeedback(Integer totalFeedback) {
        this.totalFeedback = totalFeedback;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    
    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}


