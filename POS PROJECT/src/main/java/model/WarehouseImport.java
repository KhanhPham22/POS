package model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "WarehouseImport")
public class WarehouseImport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long importId;

    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    private Double totalAmount;
    private String status;
    private String note;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @OneToMany(mappedBy = "warehouseImport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WarehouseImportDetails> importDetails;

    // Getters and Setters
}

