package model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "WarehouseImport")
public class WarehouseImport extends BaseEntity { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "import_id")
    private Long importId;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "note", length = 255)
    private String note;

    @OneToMany(mappedBy = "warehouseImport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WarehouseImportDetails> importDetails;
}


