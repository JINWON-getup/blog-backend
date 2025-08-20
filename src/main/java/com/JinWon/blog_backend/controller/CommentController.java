package com.JinWon.blog_backend.controller;

import com.JinWon.blog_backend.entity.Comment;
import com.JinWon.blog_backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(originPatterns = "*", allowCredentials = "false")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // 모든 댓글 조회
    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        List<Comment> comments = commentService.getAllComments();
        return ResponseEntity.ok(comments);
    }

    // 댓글 생성
    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody Comment comment) {
        try {
            // 필수 필드 검증
            if (comment.getUserId() == null) {
                return ResponseEntity.badRequest().body("userId는 필수입니다.");
            }
            if (comment.getUserType() == null || comment.getUserType().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("userType은 필수입니다. (USER 또는 ADMIN)");
            }
            if (!comment.getUserType().equals("USER") && !comment.getUserType().equals("ADMIN")) {
                return ResponseEntity.badRequest().body("userType은 USER 또는 ADMIN이어야 합니다.");
            }
            if (comment.getContent() == null || comment.getContent().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("content는 필수입니다.");
            }
            if (comment.getPostId() == null) {
                return ResponseEntity.badRequest().body("postId는 필수입니다.");
            }
            
            Comment result = commentService.createComment(comment);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("댓글 생성 중 오류: " + e.getMessage());
        }
    }

    // 게시글별 댓글 조회
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 조회 (ID로)
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable Long id) {
        Optional<Comment> comment = commentService.getCommentById(id);
        return comment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 댓글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody Comment commentDetails) {
        Comment updatedComment = commentService.updateComment(id, commentDetails);
        if (updatedComment != null) {
            return ResponseEntity.ok(updatedComment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        boolean deleted = commentService.deleteComment(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 게시글별 댓글 수 조회
    @GetMapping("/post/{postId}/count")
    public ResponseEntity<Long> getCommentCountByPostId(@PathVariable Long postId) {
        Long count = commentService.getCommentCountByPostId(postId);
        return ResponseEntity.ok(count);
    }
}
