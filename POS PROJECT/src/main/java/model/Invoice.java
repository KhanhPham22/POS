package model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Invoice")
public class Invoice extends BaseEntity { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long invoiceId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderDetail order;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer; // ✅ Đổi tên từ `user` → `customer` để tránh nhầm lẫn

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "discount")
    private Double discount = 0.0; // ✅ Mặc định là 0 nếu không có giảm giá

    @Column(name = "final_price", nullable = false)
    private Double finalPrice;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "invoice_day", nullable = false, updatable = false)
    private Date invoiceDay = new Date(); // ✅ Mặc định là ngày xuất hóa đơn

    @Column(name = "status", length = 20)
    private String status;
}


