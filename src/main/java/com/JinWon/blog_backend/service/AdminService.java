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
    // 관리자 로그인 검증
public boolean validateLogin(String id, String password) {
    System.out.println("=== 로그인 시도 ===");
    System.out.println("입력된 ID: " + id);
    System.out.println("입력된 비밀번호: " + password);
    
    Optional<Admin> admin = adminLoginRepository.findById(id);
    if (admin.isPresent()) {
        Admin foundAdmin = admin.get();
        System.out.println("찾은 관리자: " + foundAdmin.getId());
        System.out.println("저장된 비밀번호: " + foundAdmin.getPassword());
        
        // BCrypt로 비밀번호 검증
        boolean matches = passwordEncoder.matches(password, foundAdmin.getPassword());
        System.out.println("비밀번호 일치 여부: " + matches);
        return matches;
    } else {
        System.out.println("관리자를 찾을 수 없음");
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

    // 비밀번호 변경
    public boolean changePassword(String id, String currentPassword, String newPassword) {
        Optional<Admin> admin = adminLoginRepository.findById(id);
        if (admin.isPresent()) {
            Admin adminUser = admin.get();
        
            // 현재 비밀번호 확인
            if (passwordEncoder.matches(currentPassword, adminUser.getPassword())) {
                // 새 비밀번호를 BCrypt로 인코딩
                String encodedNewPassword = passwordEncoder.encode(newPassword);
                adminUser.setPassword(encodedNewPassword);
                
                adminLoginRepository.save(adminUser);
                return true;
            }
        }
        
        return false;
    }

    // 여기에 새로운 메서드 추가!
// 현재 비밀번호 확인 없이 새 비밀번호로 변경
public boolean changePasswordWithoutCurrent(String id, String newPassword) {
    Optional<Admin> admin = adminLoginRepository.findById(id);
    if (admin.isPresent()) {
        Admin adminUser = admin.get();
        
        // 새 비밀번호를 BCrypt로 인코딩
        String encodedNewPassword = passwordEncoder.encode(newPassword);
        adminUser.setPassword(encodedNewPassword);
        
        adminLoginRepository.save(adminUser);
        return true;
    }
    return false;
}

}
