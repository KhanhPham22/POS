package model;

import java.util.Date;

public class Dashboard extends BaseEntity {

    private Long dashboardId;
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
    private Long storeId; // New field for storeId

    // Default constructor
    public Dashboard() {}

    // Constructor with all fields
    public Dashboard(Long dashboardId, Double totalSalary, Double totalRevenue, Integer totalOrders, 
                     Integer totalUsers, Integer totalProducts, Double dayRevenue, Double monthRevenue, 
                     Double yearRevenue, Integer pendingOrders, Integer totalFeedback, Double averageRating, 
                     Date timestamp, Long storeId) {
        this.dashboardId = dashboardId;
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
        this.storeId = storeId; // Initialize storeId
    }

    // Getter and Setter methods for all fields
    public Long getDashboardId() {
        return dashboardId;
    }

    public void setDashboardId(Long dashboardId) {
        this.dashboardId = dashboardId;
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

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
}
