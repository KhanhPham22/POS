package model;

import java.util.List;

import java.util.ArrayList;

public class Product extends BaseEntity {

    private String name;
    private Double price;
    private String imagePath;
    private Double discount = 0.0;
    private Integer quantity;
    private String size;
    private Boolean status = true;
    private Category category;
    private List<OrderDetail> orderDetails = new ArrayList<>();

    // Default Constructor
    public Product() {}

    // Constructor with all fields
    public Product(Long id, String name, Double price, String imagePath, Double discount, 
                   Integer quantity, String size, Boolean status, Category category, 
                   List<OrderDetail> orderDetails) {
        setId(id); // Use BaseEntity's id
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.discount = discount;
        this.quantity = quantity;
        this.size = size;
        this.status = status;
        this.category = category;
        this.orderDetails = orderDetails != null ? orderDetails : new ArrayList<>();
    }

    // Getter and Setter Methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}
