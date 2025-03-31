package model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Supplier")
public class Supplier extends BaseEntity { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "contact_name", length = 100)
    private String contactName;

    @Column(name = "phone", nullable = false, unique = true, length = 20)
    private String phone;

    @Column(name = "email", unique = true, length = 150)
    private String email;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "tax_code", unique = true, length = 50)
    private String taxCode;

    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WarehouseImport> warehouseImports;
}


