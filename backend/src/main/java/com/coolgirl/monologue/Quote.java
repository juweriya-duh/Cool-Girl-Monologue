package com.coolgirl.monologue;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
public class Quote {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) public Long id;
    @NotBlank @Column(length = 1500) public String text;
    @NotBlank public String author;
    public String source;
    @Column(length = 1000) public String imageUrl;
    public String status = "DRAFT";      // DRAFT | PUBLISHED
    public boolean mine = false;          // true = written by me
    public LocalDateTime createdAt = LocalDateTime.now();

    static Quote of(String t, String a, String s, String img, String st, boolean mine) {
        Quote q = new Quote(); q.text = t; q.author = a; q.source = s; q.imageUrl = img; q.status = st; q.mine = mine; return q;
    }
}
