package model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long invoiceId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderDetail order;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private Double totalPrice;
    private Double discount;
    private Double finalPrice;
    private String paymentMethod;

    @Temporal(TemporalType.TIMESTAMP)
    private Date invoiceDay;

    private String status;

    // Getters and Setters
}

