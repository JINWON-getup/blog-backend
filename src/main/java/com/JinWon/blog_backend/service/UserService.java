package com.JinWon.blog_backend.service;

import com.JinWon.blog_backend.entity.User;
import com.JinWon.blog_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
        if (userRepository.existsByUserEmail(user.getUserEmail())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        // 전화번호 중복 체크
        if (userRepository.existsByPhoneNumber(user.getPhoneNumber())) {
            throw new RuntimeException("이미 존재하는 전화번호입니다.");
        }

        // 닉네임 중복 체크 추가 (null 체크 포함)
        if (user.getNickName() != null && userRepository.existsByNickName(user.getNickName())) {
            throw new RuntimeException("이미 존재하는 닉네임입니다.");
        }

        // 비밀번호 암호화
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
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
        if (!passwordEncoder.matches(password, user.getUserPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        
        // 로그인 성공 시 사용자 정보 반환 (save 불필요)
        return user;
    }

    // 로그아웃
    public void logoutUser(Long pid) {
        User user = findUserById(pid);
        // 로그아웃 로직 구현 (예: 세션 무효화, 토큰 삭제 등)
        // 현재는 사용자 존재 여부만 확인
    }

    // 유저 조회
    public Optional<User> getUserById(Long pid) {
        return userRepository.findById(pid);
    }
    
    // 유저 정보 조회
    public Optional<User> getUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }
    
    // 이메일로 사용자 조회 -> 비밀번호 찾기
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByUserEmail(email);
    }

    // 닉네임으로 사용자 조회
    public Optional<User> getUserByNickName(String nickName) {
        return userRepository.findByNickName(nickName);
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

        // 비밀번호가 변경된 경우에만 암호화
        if (userDetails.getUserPassword() != null && !userDetails.getUserPassword().isEmpty()) {
            user.setUserPassword(passwordEncoder.encode(userDetails.getUserPassword()));
        }

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
}