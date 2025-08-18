package com.JinWon.blog_backend.service;

import com.JinWon.blog_backend.entity.Comment;
import com.JinWon.blog_backend.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    
    @Autowired
    private CommentRepository commentRepository;

    // 댓글 생성
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    // 댓글 조회 (ID로)
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }
    
    // 게시글별 댓글 조회
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId);
    }

    // 사용자별 댓글 조회 (userId 기준)
    public List<Comment> getCommentsByUserId(Long userId) {
        return commentRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    
    // 댓글 수정
    public Comment updateComment(Long id, Comment commentDetails) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isPresent()) {
            Comment existingComment = comment.get();
            existingComment.setContent(commentDetails.getContent());
            return commentRepository.save(existingComment);
        }
        return null;
    }
    
    // 댓글 삭제
    public boolean deleteComment(Long id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // 게시글별 댓글 수 조회
    public Long getCommentCountByPostId(Long postId) {
        return commentRepository.countByPostId(postId);
    }
}
