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

    // 댓글 생성
    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody Comment comment) {
        try {
            System.out.println("CommentController.createComment() 호출됨");
            System.out.println("CommentService 주입 상태: " + (commentService != null ? "성공" : "실패"));
            
            if (commentService != null) {
                System.out.println("CommentService.createComment() 호출 시작");
                Comment result = commentService.createComment(comment);
                System.out.println("CommentService.createComment() 호출 완료");
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body("CommentService 주입 실패");
            }
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
