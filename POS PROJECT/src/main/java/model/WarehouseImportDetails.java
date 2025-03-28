package model;

import jakarta.persistence.*;

@Entity
@Table(name = "WarehouseImportDetails")
public class WarehouseImportDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long importDetailsId;

    @ManyToOne
    @JoinColumn(name = "import_id", nullable = false)
    private WarehouseImport warehouseImport;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    private String batchNumber;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date expiryDate;

    // Getters and Setters
}

