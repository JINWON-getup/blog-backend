package com.JinWon.blog_backend.repository;

import com.JinWon.blog_backend.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    // adminId로 관리자 찾기 (로그인용)
    Optional<Admin> findByAdminId(String adminId);
}