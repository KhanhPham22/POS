package model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private String paymentMethod;
    private Double amount;
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderDetail order;

    // Getters and Setters
}

