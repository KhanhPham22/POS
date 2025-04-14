package model;

public class OrderDetail extends BaseEntity {

    private Customer customer;
    private Product product;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    private Double discount = 0.0;
    private String paymentMethod;
    private String status;

    // Default Constructor
    public OrderDetail() {}

    // Constructor with all fields
    public OrderDetail(Long id, Customer customer, Product product, Integer quantity, 
                       Double unitPrice, Double totalPrice, Double discount, 
                       String paymentMethod, String status) {
        setId(id); // Use BaseEntity's id
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.discount = discount;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    // Getter and Setter Methods
    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}