package com.JinWon.blog_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "admin")
@Data
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String adminId;  // 로그인용 아이디 (예: "admin")

    @Column(nullable = false)
    private String adminName;

    @Column(nullable = false)
    private String adminPassword;

    @Column(nullable = false)
    private String adminEmail;
}