package com.JinWon.blog_backend.controller;

import com.JinWon.blog_backend.entity.User;
import com.JinWon.blog_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // 회원가입
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            
            User registeredUser = userService.registerUser(user);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "회원가입이 성공했습니다.");
            response.put("user", Map.of(
                    "pid", registeredUser.getId(),
                    "userId", registeredUser.getUserId(),
                    "email", registeredUser.getUserEmail(),
                    "phoneNumber", registeredUser.getPhoneNumber()
            ));

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginRequest) {
        try {
            String userId = loginRequest.get("userId");
            String password = loginRequest.get("password");

            if (userId == null || password == null) {
                return ResponseEntity.badRequest().body("아이디와 비밀번호를 입력해주세요.");
            }

            User user = userService.loginUser(userId, password);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "로그인이 성공했습니다.");

            // Map.of() 대신 HashMap 사용
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("pid", user.getId());
            userInfo.put("userId", user.getUserId());
            userInfo.put("email", user.getUserEmail());
            userInfo.put("phoneNumber", user.getPhoneNumber());
            userInfo.put("nickName", user.getNickName());

            response.put("user", userInfo);

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestBody Map<String, Long> logoutRequest) {
        try {
            Long pid = logoutRequest.get("pid");
            
            if (pid == null) {
                return ResponseEntity.badRequest().body("사용자 ID가 필요합니다.");
            }

            userService.logoutUser(pid);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "로그아웃이 완료되었습니다.");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 사용자 정보 조회
    @GetMapping("/{pid}")
    public ResponseEntity<?> getUserById(@PathVariable Long pid) {
        try {
            Optional<User> userOpt = userService.getUserById(pid);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);

                // Map.of() 대신 HashMap 사용
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("pid", user.getId());
                userInfo.put("userId", user.getUserId());
                userInfo.put("email", user.getUserEmail());
                userInfo.put("phoneNumber", user.getPhoneNumber());
                userInfo.put("nickName", user.getNickName());

                response.put("user", userInfo);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "사용자 조회 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 비밀번호 변경
    @PutMapping("/{pid}")
    public ResponseEntity<?> updateUser(@PathVariable Long pid, @RequestBody User userDetails) {
        try {
            User updatedUser = userService.updateUser(pid, userDetails);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "비밀번호가 성공적으로 변경되었습니다.");
            response.put("user", Map.of(
                    "pid", updatedUser.getId(),
                    "userId", updatedUser.getUserId(),
                    "email", updatedUser.getUserEmail(),
                    "phoneNumber", updatedUser.getPhoneNumber(),
                    "nickName", updatedUser.getNickName()
            ));

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 사용자 삭제
    @DeleteMapping("/{pid}")
    public ResponseEntity<?> deleteUser(@PathVariable Long pid) {
        try {
            userService.deleteUser(pid);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "사용자가 삭제되었습니다.");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "사용자 삭제 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 사용자 검색
    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestParam String keyword) {
        try {
            List<User> users = userService.searchUsers(keyword);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("users", users);
            response.put("count", users.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "사용자 검색 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 모든 사용자 조회
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("users", users);
            response.put("count", users.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "사용자 목록 조회 중 오류가 발생했습니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    // 회원탈퇴
    @PostMapping("/{pid}/withdraw")
    public ResponseEntity<?> withdrawUser(@PathVariable Long pid, @RequestBody Map<String, String> withdrawRequest) {
        try {
            String password = withdrawRequest.get("password");
            
            if (password == null || password.isEmpty()) {
                return ResponseEntity.badRequest().body("비밀번호를 입력해주세요.");
            }

            boolean withdrawn = userService.withdrawUser(pid, password);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "회원탈퇴가 완료되었습니다.");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}