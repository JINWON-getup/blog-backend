package com.JinWon.blog_backend.service;

import com.JinWon.blog_backend.entity.User;
import com.JinWon.blog_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User findUserById(Long pid) {
        return userRepository.findById(pid)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    // 회원 가입
    public User registerUser(User user) {
        // userId 중복 체크
        if (userRepository.existsByUserId(user.getUserId())) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }
        
        // 이메일 중복 체크
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        // 전화번호 중복 체크
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new RuntimeException("이미 존재하는 전화번호입니다.");
        }

        // 비밀번호 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 초기값 설정
        user.setPoint(0);
        user.setLevel(1);
        user.setExperience(0);
        user.setTotalPost(0);
        user.setTotalComment(0);
        user.setTotalLike(0);
        user.setLastLogin(null);
        user.setConsecutiveLogin(0);

        return userRepository.save(user);
    }

    // 로그인
    public User loginUser(String userId, String password) {
        Optional<User> userOpt = userRepository.findByUserId(userId);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        User user = userOpt.get();

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        // 비활성 사용자 체크
        if (!user.getIsActive()) {
            throw new RuntimeException("비활성화된 계정입니다.");
        }

        // 일일 로그인 포인트
        user.checkDailyLogin();

        return userRepository.save(user);
    }

    // 로그아웃
    public void logoutUser(Long pid) {
        User user = findUserById(pid);
        
        // 로그아웃 시 특별한 처리가 필요한 경우 여기에 추가
        // 예: 세션 무효화, 로그아웃 시간 기록 등
        
        // 현재는 단순히 사용자 존재 여부만 확인
        // 실제 프로덕션에서는 JWT 토큰 무효화나 세션 관리 로직이 필요
    }

    // 유저 조회
    public Optional<User> getUserById(Long pid) {
        return userRepository.findById(pid);
    }
    
    // 유저 정보 조회
    public Optional<User> getUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // 모든 유저 조회
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 유저 정보 수정
    public User updateUser(Long pid, User userDetails) {
        User user = userRepository.findById(pid)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + pid));

        // 전화번호 중복 체크 (자신 제외)
        if (!user.getPhoneNumber().equals(userDetails.getPhoneNumber()) &&
                userRepository.existsByPhoneNumber(userDetails.getPhoneNumber())) {
            throw new RuntimeException("이미 존재하는 전화번호입니다.");
        }

        // 수정 가능한 필드만 업데이트
        if (userDetails.getPhoneNumber() != null) {
            user.setPhoneNumber(userDetails.getPhoneNumber());
        }

        if (userDetails.getAddress() != null) {
            user.setAddress(userDetails.getAddress());
        }

        // 비밀번호가 변경된 경우에만 암호화
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        return userRepository.save(user);
    }

    // 사용자 비활성화
    public User deactivateUser(Long pid) {
        User user = userRepository.findById(pid)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다. ID: " + pid));

        user.setIsActive(false);
        return userRepository.save(user);
    }

    // 사용자 완전 삭제
    public void deleteUser(Long pid) {
        userRepository.deleteById(pid);
    }

    // 사용자 검색
    public List<User> searchUsers(String keyword) {
        return userRepository.searchUsers(keyword);
    }

    // 게시글 작성 시 포인트 지급 (간소화)
    public void addPostPoint(Long pid) {
        User user = findUserById(pid);  // 공통 메서드 사용
        user.addPostPoint();
        userRepository.save(user);
    }

    // 댓글 작성 시 포인트 지급 (간소화)
    public void addCommentPoint(Long pid) {
        User user = findUserById(pid);  // 공통 메서드 사용
        user.addCommentPoint();
        userRepository.save(user);
    }

    // 좋아요 받았을 때 포인트 지급 (간소화)
    public void addLikePoint(Long pid) {
        User user = findUserById(pid);  // 공통 메서드 사용
        user.addLikePoint();
        userRepository.save(user);
    }
            
    // 포인트 차감 (간소화)
    public boolean subtractUserPoint(Long pid, int amount) {
        User user = findUserById(pid);  // 공통 메서드 사용

        boolean success = user.subtractPoint(amount);
        if (success) {
            userRepository.save(user);
        }
        return success;
    }
}
























