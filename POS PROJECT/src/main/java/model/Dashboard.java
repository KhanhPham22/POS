package model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Dashboard")
public class Dashboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dashboardId;

    private Double totalSalary;
    private Double totalRevenue;
    private Integer totalOrders;
    private Integer totalUsers;
    private Integer totalProducts;
    private Double dayRevenue;
    private Double monthRevenue;
    private Double yearRevenue;
    private Integer pendingOrders;
    private Integer totalFeedback;
    private Double averageRating;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    // Getters and Setters
}

