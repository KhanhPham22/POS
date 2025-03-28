package model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Owner")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ownerId;

    private String name;
    private String password;
    private String email;
    private String address;
    private String national;
    private String notes;
    private String phone;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    // Getters and Setters
}

