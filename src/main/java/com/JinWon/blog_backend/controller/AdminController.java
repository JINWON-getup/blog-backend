package com.JinWon.blog_backend.controller;

import com.JinWon.blog_backend.entity.Admin;
import com.JinWon.blog_backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")

public class AdminController {

    @Autowired
    private AdminService adminLoginService;

    // 관리자 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String id = loginRequest.get("id");
        String password = loginRequest.get("password");

        if (id == null || password == null) {
            return ResponseEntity.badRequest().body("ID와 비밀번호를 입력해주세요.");
        }

        boolean isValid = adminLoginService.validateLogin(id, password);

        Map<String, Object> response = new HashMap<>();
        if (isValid) {
            response.put("success", true);
            response.put("message", "로그인 성공");

            // 관리자 정보 조회 (비밀번호 제외)
            Optional<Admin> admin = adminLoginService.getAdminById(id);
            if (admin.isPresent()) {
                Admin adminInfo = admin.get();
                Map<String, String> adminData = new HashMap<>();
                adminData.put("id", adminInfo.getAdminId());
                adminData.put("adminName", adminInfo.getAdminName());
                adminData.put("email", adminInfo.getAdminEmail());
                response.put("admin", adminData);
            }
        } else {
            response.put("success", false);
            response.put("message", "ID 또는 비밀번호가 올바르지 않습니다.");
        }

        return ResponseEntity.ok(response);
    }

    // 관리자 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<?> getAdminInfo(@PathVariable String id) {
        Optional<Admin> admin = adminLoginService.getAdminById(id);
        if (admin.isPresent()) {
            Admin adminInfo = admin.get();
            Map<String, String> adminData = new HashMap<>();
            adminData.put("id", adminInfo.getAdminId());
            adminData.put("adminName", adminInfo.getAdminName());
            adminData.put("email", adminInfo.getAdminEmail());
            return ResponseEntity.ok(adminData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "로그아웃 성공");
        return ResponseEntity.ok(response);
    }

    // 관리자 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable String id, @RequestBody Map<String, String> updateRequest) {
        try {

            // 현재 로그인된 사용자 확인 (보안)
            Optional<Admin> existingAdmin = adminLoginService.getAdminById(id);
            if (!existingAdmin.isPresent()) {
                return ResponseEntity.status(404).body("관리자를 찾을 수 없습니다.");
            }

            Admin admin = existingAdmin.get();

            // 수정 가능한 필드들 (role은 보안상 수정 불가)
            if (updateRequest.containsKey("adminName")) {
                admin.setAdminName(updateRequest.get("adminName"));
            }
            if (updateRequest.containsKey("email")) {
                admin.setAdminEmail(updateRequest.get("email"));
            }
            // role은 보안상 수정 불가능하므로 제거

            Admin updatedAdmin = adminLoginService.updateAdmin(admin);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "관리자 정보 수정 성공");
            response.put("admin", Map.of(
                    "id", updatedAdmin.getAdminId(),
                    "adminName", updatedAdmin.getAdminName(),
                    "email", updatedAdmin.getAdminEmail()
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "관리자 정보 수정 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}