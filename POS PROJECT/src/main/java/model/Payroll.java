package model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Payroll")
public class Payroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payrollId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    private Double baseSalary;
    private Double bonus;
    private Double commission;
    private Double totalSalary;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    // Getters and Setters
}

