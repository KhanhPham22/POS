package model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "GiftVoucher")
public class GiftVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long giftVoucherId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String voucherName;
    private Boolean discountStatus;
    private Date startDate;
    private Date endDate;
    private String discountType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    // Getters and Setters
}

