package com.JinWon.blog_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "admin")
@Data
public class Admin {
    @Column
    private String adminName;

    @Id
    @Column
    private String adminId;

    @Column
    private String adminPassword;

    @Column
    private String adminEmail;
}