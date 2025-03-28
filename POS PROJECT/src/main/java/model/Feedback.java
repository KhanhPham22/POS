package model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String type;
    private String content;
    private Integer rating;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    // Getters and Setters
}

