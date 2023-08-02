package com.example.miniproject2.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Negotiation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "negotiation_id")
    private Long id;
    //@Column(name = "item_id")
    private Long itemId;
    //@Column(name = "suggested_price")
    @Column(nullable = false)
    private int suggestedPrice;
    private String status;
    @Column(nullable = false)
    private String writer;
    @Column(nullable = false)
    private String password;
    @ManyToOne
    private User user;

}
