package com.JinWon.blog_backend.repository;

import com.JinWon.blog_backend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 게시글별 댓글 조회
    List<Comment> findByPostIdOrderByCreatedAtDesc(Long postId);

    // 사용자별 댓글 조회 (userId 기준)
    List<Comment> findByUserIdOrderByCreatedAtDesc(Long userId);

    // 게시글별 댓글 수 조회
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.postId = :postId")
    Long countByPostId(@Param("postId") Long postId);
}
