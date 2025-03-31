package model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Dashboard")
public class Dashboard extends BaseEntity { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dashboard_id")
    private Long dashboardId;

    @Column(name = "total_salary")
    private Double totalSalary;

    @Column(name = "total_revenue")
    private Double totalRevenue;

    @Column(name = "total_orders")
    private Integer totalOrders;

    @Column(name = "total_users")
    private Integer totalUsers;

    @Column(name = "total_products")
    private Integer totalProducts;

    @Column(name = "day_revenue")
    private Double dayRevenue;

    @Column(name = "month_revenue")
    private Double monthRevenue;

    @Column(name = "year_revenue")
    private Double yearRevenue;

    @Column(name = "pending_orders")
    private Integer pendingOrders;

    @Column(name = "total_feedback")
    private Integer totalFeedback;

    @Column(name = "average_rating")
    private Double averageRating;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp")
    private Date timestamp = new Date(); // ✅ Mặc định là thời gian hiện tại

}


