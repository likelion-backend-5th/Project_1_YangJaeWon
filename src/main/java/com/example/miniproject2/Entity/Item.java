package com.example.miniproject2.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    private String title;
    private String description;
    private String image_url;
    private int min_price_wanted;
    private String status;
    private String writer;
    private String password;
    @ManyToOne
    private User user;

}
