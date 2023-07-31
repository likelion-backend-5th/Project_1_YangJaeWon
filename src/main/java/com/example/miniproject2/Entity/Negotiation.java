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
    private int suggestedPrice;
    private String status;
    private String writer;
    private String password;

}
