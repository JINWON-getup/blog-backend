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
    public boolean validateLogin(String adminId, String password) {
        Optional<Admin> admin = adminLoginRepository.findByAdminId(adminId);
        if (admin.isPresent()) {
            Admin foundAdmin = admin.get();
            // 평문 비밀번호 비교 (개발 모드)
            return password.equals(foundAdmin.getAdminPassword());
        } else {
            return false;
        }
    }

    // 관리자 정보 조회 (adminId로)
    public Optional<Admin> getAdminByAdminId(String adminId) {
        return adminLoginRepository.findByAdminId(adminId);
    }

    // 관리자 정보 조회 (Long id로)
    public Optional<Admin> getAdminById(Long id) {
        return adminLoginRepository.findById(id);
    }

    // 관리자 정보 수정
    public Admin updateAdmin(Admin admin) {
        return adminLoginRepository.save(admin);
    }

    // 비밀번호 변경 (현재 비밀번호 확인 후)
    public boolean changePassword(Long id, String currentPassword, String newPassword) {
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