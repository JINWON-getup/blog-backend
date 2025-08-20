package com.JinWon.blog_backend.controller;

import com.JinWon.blog_backend.entity.Post;
import com.JinWon.blog_backend.service.PostService;
import com.JinWon.blog_backend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/post")
@CrossOrigin(originPatterns = "*", allowCredentials = "false")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;  // fallback용

    // 게시글 생성
    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        try {
            // 필수 필드 검증
            if (post.getUserId() == null) {
                return ResponseEntity.badRequest().body("userId는 필수입니다.");
            }
            if (post.getUserType() == null || post.getUserType().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("userType은 필수입니다. (USER 또는 ADMIN)");
            }
            if (!post.getUserType().equals("USER") && !post.getUserType().equals("ADMIN")) {
                return ResponseEntity.badRequest().body("userType은 USER 또는 ADMIN이어야 합니다.");
            }
            if (post.getNickName() == null || post.getNickName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("nickName은 필수입니다.");
            }
            if (post.getTitle() == null || post.getTitle().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("title은 필수입니다.");
            }
            if (post.getCategory() == null || post.getCategory().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("category는 필수입니다.");
            }
            if (post.getBoardType() == null || post.getBoardType().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("boardType은 필수입니다.");
            }
            
            Post result = postService.createPost(post);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("게시글 생성 실패: " + e.getMessage());
        }
    }

    // 게시글 목록 조회 (보드별 필터링 가능)
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(@RequestParam(required = false) String boardType) {

        if (boardType != null) {
            return ResponseEntity.ok(postService.getPostsByBoardType(boardType));
        }
        return ResponseEntity.ok(postService.getAllPosts());
    }

    // 게시글 조회 (단일)
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id) {
        Optional<Post> post = postService.getPostById(id);
        return post.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 게시글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Long id, @RequestBody Post postDetails) {
        Optional<Post> post = postService.getPostById(id);
        if (post.isPresent()) {
            Post existingPost = post.get();
            existingPost.setTitle(postDetails.getTitle());
            existingPost.setContent(postDetails.getContent());
            existingPost.setId(postDetails.getId());
            existingPost.setCategory(postDetails.getCategory());
            existingPost.setTags(postDetails.getTags());
            existingPost.setBoardType(postDetails.getBoardType());
            Post updatedPost = postService.updatePost(id, postDetails);  // 올바른 메서드 호출
            return ResponseEntity.ok(updatedPost);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        Optional<Post> post = postService.getPostById(id);
        if (post.isPresent()) {
            postService.deletePost(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
