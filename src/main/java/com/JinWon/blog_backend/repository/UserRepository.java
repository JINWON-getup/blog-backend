package com.JinWon.blog_backend.repository;

import com.JinWon.blog_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    // userId로 사용자 찾기 (로그인 용도)
    Optional<User> findByUserId(String userId);

    // email로 사용자 찾기 (비밀번호 찾기 용도)
    Optional<User> findByEmail(String email);

    // userId 중복 체크
    boolean existsByUserId(String userId);
    
    // 이메일 중복 체크
    boolean existsByEmail(String email);

    // 전화번호 중복 체크
    boolean existsByPhoneNumber(String phoneNumber);

    // 사용자 검색 (userId, 이메일, 주소로 검색)
    @Query("SELECT u FROM User u WHERE u.userId LIKE CONCAT('%', :keyword, '%') OR u.email LIKE CONCAT('%', :keyword, '%')")
    List<User> searchUsers(@Param("keyword") String keyword);
}
