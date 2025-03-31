package model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Payroll")
public class Payroll extends BaseEntity { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payroll_id")
    private Long payrollId;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "base_salary", nullable = false)
    private Double baseSalary;

    @Column(name = "bonus")
    private Double bonus = 0.0; // ✅ Mặc định là 0 nếu không có thưởng

    @Column(name = "commission")
    private Double commission = 0.0; // ✅ Mặc định là 0 nếu không có hoa hồng

    @Column(name = "total_salary", nullable = false)
    private Double totalSalary;
}


