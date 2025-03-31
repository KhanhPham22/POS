package model;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Product")
public class Product extends BaseEntity { 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "image", length = 255)
    private String image;

    @Column(name = "discount", nullable = false)
    private Double discount = 0.0; // ✅ Mặc định là 0 nếu không có giảm giá

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "size", length = 20)
    private String size;

    @Column(name = "status", nullable = false)
    private Boolean status = true; // ✅ Mặc định là true (còn bán)

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails;
}


