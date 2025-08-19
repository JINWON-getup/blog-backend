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

    // 게시글 생성
    public Post createPost(Post post) {
        return postRepository.save(post);
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
            existingPost.setId(postDetails.getId());
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
    
    // 사용자 ID로 게시글 조회
    public List<Post> getPostsByUserId(Long userId) {
        return postRepository.findByUserId(userId);
    }
}
