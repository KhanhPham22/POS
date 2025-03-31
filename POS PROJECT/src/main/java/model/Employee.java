package model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Employee")
public class Employee extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; // Đảm bảo tương thích khi serialize

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "national", length = 50)
    private String national;

    @Column(name = "address", length = 255)
    private String address;

    

    @Override
    public String toString() {
        return "Employee{id=" + employeeId + ", name='" + name + "', email='" + email + "'}";
    }
}


