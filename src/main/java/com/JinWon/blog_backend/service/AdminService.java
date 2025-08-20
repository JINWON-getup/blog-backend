package com.JinWon.blog_backend.service;

import com.JinWon.blog_backend.entity.Admin;
import com.JinWon.blog_backend.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminLoginRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 관리자 로그인 검증
    public boolean validateLogin(String id, String password) {
        Optional<Admin> admin = adminLoginRepository.findById(id);
        if (admin.isPresent()) {
            Admin foundAdmin = admin.get();
            // BCrypt로 비밀번호 검증
            boolean matches = passwordEncoder.matches(password, foundAdmin.getAdminPassword());
            return matches;
        } else {
            return false;
        }
    }

    // 관리자 정보 조회
    public Optional<Admin> getAdminById(String id) {
        return adminLoginRepository.findById(id);
    }

    // 관리자 정보 수정
    public Admin updateAdmin(Admin admin) {
        return adminLoginRepository.save(admin);
    }

    // 비밀번호 변경 (현재 비밀번호 확인 후)
    public boolean changePassword(String id, String currentPassword, String newPassword) {
        Optional<Admin> admin = adminLoginRepository.findById(id);
        if (admin.isPresent()) {
            Admin adminUser = admin.get();

            // 현재 비밀번호 확인
            if (passwordEncoder.matches(currentPassword, adminUser.getAdminPassword())) {
                // 새 비밀번호를 BCrypt로 인코딩
                String encodedNewPassword = passwordEncoder.encode(newPassword);
                adminUser.setAdminPassword(encodedNewPassword);

                adminLoginRepository.save(adminUser);
                return true;
            }
        }
        return false;
    }
}