package com.JinWon.blog_backend.repository;

import com.JinWon.blog_backend.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {
    
    // 관리자 ID로 조회
    Optional<Admin> findById(String id);
    
    // 관리자 ID와 비밀번호로 로그인 확인
    Optional<Admin> findByIdAndPassword(String id, String password);
}