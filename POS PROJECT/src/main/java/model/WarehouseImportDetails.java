package model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "WarehouseImportDetails")
public class WarehouseImportDetails extends BaseEntity { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "import_details_id")
    private Long importDetailsId;

    @ManyToOne
    @JoinColumn(name = "import_id", nullable = false)
    private WarehouseImport warehouseImport;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "batch_number", length = 50)
    private String batchNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "expiry_date")
    private Date expiryDate;
}


