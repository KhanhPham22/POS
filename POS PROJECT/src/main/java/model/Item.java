package model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Item")
public class Item extends BaseEntity { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "unit", length = 20)
    private String unit;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}


