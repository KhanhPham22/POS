package model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Admin")
public class Admin extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L; // Đảm bảo tương thích khi serialize

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long adminId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "national", length = 50)
    private String national;

    @Column(name = "address", length = 255)
    private String address;

   

 // Quan hệ với WarehouseImport (1 Admin có thể tạo nhiều WarehouseImport)
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WarehouseImport> warehouseImports;

    // Quan hệ với Dashboard (1 Admin có thể quản lý nhiều Dashboard)
    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Dashboard> dashboards;
    
    @Override
    public String toString() {
        return "Admin{id=" + adminId + ", name='" + name + "', email='" + email + "'}";
    }
}

