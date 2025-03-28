package model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    private String name;
    private String password;
    private String gender;
    private Integer age;
    private String national;
    private String address;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    // Getters and Setters
}

