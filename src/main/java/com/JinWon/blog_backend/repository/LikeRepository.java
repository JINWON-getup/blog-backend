package com.JinWon.blog_backend.repository;

import com.JinWon.blog_backend.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    
    // 게시글별 좋아요 조회
    List<Like> findByPostId(Long postId);
    
    // 사용자별 좋아요 조회
    List<Like> findByUserId(Long userId);
    
    // 특정 게시글에 특정 사용자가 좋아요를 눌렀는지 확인
    Optional<Like> findByPostIdAndUserId(Long postId, Long userId);
    
    // 게시글별 좋아요 수 조회
    @Query("SELECT COUNT(l) FROM Like l WHERE l.postId = :postId")
    Long countByPostId(@Param("postId") Long postId);
    
    // 사용자별 좋아요 수 조회
    @Query("SELECT COUNT(l) FROM Like l WHERE l.userId = :userId")
    Long countByUserId(@Param("userId") Long userId);
}
