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

    // email로 사용자 찾기 (비밀번호 찾기 용도) - 필드명 userEmail 기준
    Optional<User> findByUserEmail(String userEmail);

    // userId 중복 체크
    boolean existsByUserId(String userId);
    
    // 이메일 중복 체크 - 필드명 userEmail 기준
    boolean existsByUserEmail(String userEmail);

    // 전화번호 중복 체크
    boolean existsByPhoneNumber(String phoneNumber);

    // 닉네임 중복 체크
    boolean existsByNickName(String nickName);

    // 닉네임으로 사용자 찾기
    Optional<User> findByNickName(String nickName);

    // 사용자 검색 (userId로만 검색)
    @Query("SELECT u FROM User u WHERE u.userId LIKE CONCAT('%', :keyword, '%')")
    List<User> searchUsers(@Param("keyword") String keyword);
}
