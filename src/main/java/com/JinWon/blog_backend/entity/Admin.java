package com.JinWon.blog_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Admin {
    @Column
    private String adminName;

    @Id
    @Column
    private String id;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String role;
}