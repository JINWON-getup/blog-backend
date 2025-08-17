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
    
    @Autowired
    private UserService userService;
    
    // 댓글 생성 (포인트 지급 포함)
    public Comment createComment(Comment comment) {
        Comment savedComment = commentRepository.save(comment);
        
        // 댓글 작성자에게 포인트 지급
        if (comment.getAuthor() != null) {
            try {
                Long authorPid = comment.getAuthor();  // 이미 Long 타입이므로 파싱 불필요
                System.out.println("댓글 작성자 PID: " + authorPid);
                System.out.println("댓글 포인트 지급 시작...");
                
                if (userService != null) {
                    userService.addCommentPoint(authorPid);
                    System.out.println("댓글 포인트 지급 완료!");
                } else {
                    System.out.println("UserService가 null입니다!");
                }
            } catch (Exception e) {
                System.out.println("댓글 포인트 지급 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Author가 null입니다.");
        }
        
        return savedComment;
    }
    
    // 댓글 조회 (ID로)
    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }
    
    // 게시글별 댓글 조회
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId);
    }
    
    // 작성자별 댓글 조회
    public List<Comment> getCommentsByAuthor(Long author) {
        return commentRepository.findByAuthorOrderByCreatedAtDesc(author);
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
