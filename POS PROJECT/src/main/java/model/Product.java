package model;

import java.util.List;
import java.util.Set;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;

public class Product extends BaseEntity {

    private String name;
    private BigDecimal price;
    private String imagePath;
    private BigDecimal discount ;
    private Integer quantity;
    private String size;
    private Integer status = 1; // Changed from Boolean to Integer to match NUMBER(1,0)
    private Category category;
    private Set<OrderDetail> orderDetails = new HashSet<>(); // Changed to Set
    private GiftVoucher giftVoucher;
    private String ean13;
    
    // Default Constructor
    public Product() {}

    // Constructor with all fields
    public Product(long id, String name, BigDecimal price, String imagePath, BigDecimal discount, 
                   Integer quantity, String size, Integer status, Category category, 
                   Set<OrderDetail> orderDetails, String ean13) {
        setId(id); // Use BaseEntity's id
        this.name = name;
        this.price = price;
        this.imagePath = imagePath;
        this.discount = discount;
        this.quantity = quantity;
        this.size = size;
        this.status = status != null ? status : 1; // Default to 1 (active)
        this.category = category;
        this.orderDetails = orderDetails != null ? orderDetails : new HashSet<>();
        this.ean13 = ean13;
    }

    // Getter and Setter Methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(Set<OrderDetail> orderDetails) { // Changed to Set
        this.orderDetails = orderDetails != null ? orderDetails : new HashSet<>();
    }
    
    public GiftVoucher getGiftVoucher() {
        return giftVoucher;
    }

    public void setGiftVoucher(GiftVoucher giftVoucher) {
        this.giftVoucher = giftVoucher;
    }
    
    public String getEan13() {
        return ean13;
    }

    public void setEan13(String ean13) {
        this.ean13 = ean13;
    }
}
