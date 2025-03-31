package model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "GiftVoucher")
public class GiftVoucher extends BaseEntity { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gift_voucher_id")
    private Long giftVoucherId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "voucher_name", nullable = false, length = 100)
    private String voucherName;

    @Column(name = "discount_status", nullable = false)
    private Boolean discountStatus = false;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "discount_type", length = 50)
    private String discountType;
}


