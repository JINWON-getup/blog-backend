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
    public Post createPost(@RequestBody Post post) {
        System.out.println("PostController.createPost() 호출됨");
        System.out.println("PostService 주입 상태: " + (postService != null ? "성공" : "실패"));
        
        if (postService != null) {
            System.out.println("PostService.createPost() 호출 시작");
            Post result = postService.createPost(post);
            System.out.println("PostService.createPost() 호출 완료");
            return result;
        } else {
            System.out.println("PostService가 null입니다! Repository 직접 사용");
            // 임시로 Repository 직접 사용
            return postRepository.save(post);
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
            existingPost.setAuthor(postDetails.getAuthor());
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
