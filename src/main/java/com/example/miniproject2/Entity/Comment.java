package com.example.miniproject2.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    @Column(name = "item_id")
    private Long itemId;

    @ManyToOne
    private Item item;

    private String writer;
    private String password;
    private String content;
    private String reply;
    @ManyToOne
    private User user;

}
