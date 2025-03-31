package model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Customer")
public class Customer extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; // ✅ Tránh lỗi khi serialize

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long customerId; 

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "phone", nullable = false, length = 20, unique = true)
    private String phone;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "national", length = 50)
    private String national;

    @Column(name = "points", nullable = false)
    private double points = 0.0; // ✅ Mặc định 0.0 khi khách mới đăng ký
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetail> orders;

    @Override
    public String toString() {
        return "Customer{id=" + customerId + ", name='" + name + "', phone='" + phone + "', points=" + points + "}";
    }
}
