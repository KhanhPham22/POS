package model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "OrderDetail")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    private Double discount;
    private String paymentMethod;
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    // Getters and Setters
}

