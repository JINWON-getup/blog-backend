package com.JinWon.blog_backend.service;

import com.JinWon.blog_backend.entity.Post;
import com.JinWon.blog_backend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@Service
public class PostService {
    
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private UserService userService;
    
    // 게시글 생성
    public Post createPost(Post post) {
        Post savedPost = postRepository.save(post);
        
        // UserService 의존성 주입 상태 확인
        System.out.println("UserService 주입 상태: " + (userService != null ? "성공" : "실패"));
        
        // 게시글 작성자에게 포인트 지급
        if (post.getAuthor() != null) {
            try {
                Long authorPid = post.getAuthor();  // 이미 Long 타입이므로 파싱 불필요
                System.out.println("게시글 작성자 PID: " + authorPid);
                System.out.println("포인트 지급 시작...");
                
                if (userService != null) {
                    userService.addPostPoint(authorPid);
                    System.out.println("포인트 지급 완료!");
                } else {
                    System.out.println("UserService가 null입니다!");
                }
            } catch (Exception e) {
                System.out.println("포인트 지급 중 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Author가 null입니다.");
        }
        
        return savedPost;
    }
    
    // 게시글 조회 (ID로)
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }
    
    // 모든 게시글 조회
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    
    // 게시글 수정
    public Post updatePost(Long id, Post postDetails) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            Post existingPost = post.get();
            existingPost.setTitle(postDetails.getTitle());
            existingPost.setContent(postDetails.getContent());
            existingPost.setAuthor(postDetails.getAuthor());
            existingPost.setCategory(postDetails.getCategory());
            existingPost.setTags(postDetails.getTags());
            existingPost.setBoardType(postDetails.getBoardType());
            return postRepository.save(existingPost);
        }
        return null;
    }
    
    // 게시글 삭제
    public boolean deletePost(Long id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // 보드 타입별 게시글 조회
    public List<Post> getPostsByBoardType(String boardType) {
        return postRepository.findByBoardType(boardType);
    }
}
