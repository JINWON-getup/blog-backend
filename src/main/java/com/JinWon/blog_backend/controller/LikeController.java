package com.JinWon.blog_backend.controller;

import com.JinWon.blog_backend.entity.Like;
import com.JinWon.blog_backend.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
@CrossOrigin(originPatterns = "*", allowCredentials = "false")
public class LikeController {

    @Autowired
    private LikeService likeService;

    // 좋아요 추가
    @PostMapping
    public ResponseEntity<?> addLike(@RequestBody Map<String, Object> likeRequest) {
        try {
            Long userId = Long.valueOf(likeRequest.get("userId").toString());
            Long postId = Long.valueOf(likeRequest.get("postId").toString());
            
            System.out.println("LikeController.addLike() 호출됨");
            System.out.println("LikeService 주입 상태: " + (likeService != null ? "성공" : "실패"));
            
            if (likeService != null) {
                System.out.println("LikeService.addLike() 호출 시작");
                Like result = likeService.addLike(userId, postId);
                System.out.println("LikeService.addLike() 호출 완료");
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.badRequest().body("LikeService 주입 실패");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("좋아요 추가 중 오류: " + e.getMessage());
        }
    }

    // 좋아요 취소
    @DeleteMapping
    public ResponseEntity<?> removeLike(@RequestBody Map<String, Object> likeRequest) {
        try {
            Long userId = Long.valueOf(likeRequest.get("userId").toString());
            Long postId = Long.valueOf(likeRequest.get("postId").toString());
            
            boolean removed = likeService.removeLike(userId, postId);
            if (removed) {
                return ResponseEntity.ok().body("좋아요가 취소되었습니다.");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("좋아요 취소 중 오류: " + e.getMessage());
        }
    }

    // 게시글별 좋아요 조회
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<Like>> getLikesByPostId(@PathVariable Long postId) {
        List<Like> likes = likeService.getLikesByPostId(postId);
        return ResponseEntity.ok(likes);
    }

    // 사용자별 좋아요 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Like>> getLikesByUserId(@PathVariable Long userId) {
        List<Like> likes = likeService.getLikesByUserId(userId);
        return ResponseEntity.ok(likes);
    }

    // 게시글별 좋아요 수 조회
    @GetMapping("/post/{postId}/count")
    public ResponseEntity<Long> getLikeCountByPostId(@PathVariable Long postId) {
        Long count = likeService.getLikeCountByPostId(postId);
        return ResponseEntity.ok(count);
    }

    // 사용자별 좋아요 수 조회
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> getLikeCountByUserId(@PathVariable Long userId) {
        Long count = likeService.getLikeCountByUserId(userId);
        return ResponseEntity.ok(count);
    }

    // 특정 게시글에 특정 사용자가 좋아요를 눌렀는지 확인
    @GetMapping("/check")
    public ResponseEntity<Boolean> hasUserLikedPost(@RequestParam Long userId, @RequestParam Long postId) {
        boolean hasLiked = likeService.hasUserLikedPost(userId, postId);
        return ResponseEntity.ok(hasLiked);
    }
}
