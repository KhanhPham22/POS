package model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Payment")
public class Payment extends BaseEntity { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "status", length = 20)
    private String status;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderDetail order;
}


