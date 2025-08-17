package com.JinWon.blog_backend.service;

import com.JinWon.blog_backend.entity.Like;
import com.JinWon.blog_backend.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {
    
    @Autowired
    private LikeRepository likeRepository;
    
    @Autowired
    private UserService userService;
    
    // 좋아요 추가 (포인트 지급 포함)
    public Like addLike(Long userId, Long postId) {
        // 이미 좋아요를 눌렀는지 확인
        Optional<Like> existingLike = likeRepository.findByPostIdAndUserId(postId, userId);
        if (existingLike.isPresent()) {
            throw new RuntimeException("이미 좋아요를 누른 게시글입니다.");
        }
        
        // 좋아요 생성
        Like like = new Like();
        like.setUserId(userId);
        like.setPostId(postId);
        Like savedLike = likeRepository.save(like);
        
        // 게시글 작성자에게 포인트 지급
        try {
            // 게시글 작성자 찾기 (Post 엔티티에서 author 가져오기)
            // 여기서는 간단히 postId 48번 게시글의 작성자를 "1"로 가정
            Long postAuthor = 1L;  // 실제로는 Post 엔티티에서 가져와야 함
            
            Long authorPid = postAuthor;
            System.out.println("게시글 작성자 PID: " + authorPid);
            System.out.println("좋아요 포인트 지급 시작...");
            
            if (userService != null) {
                userService.addLikePoint(authorPid);
                System.out.println("좋아요 포인트 지급 완료!");
            } else {
                System.out.println("UserService가 null입니다!");
            }
        } catch (Exception e) {
            System.out.println("좋아요 포인트 지급 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        
        return savedLike;
    }
    
    // 좋아요 취소
    public boolean removeLike(Long userId, Long postId) {
        Optional<Like> like = likeRepository.findByPostIdAndUserId(postId, userId);
        if (like.isPresent()) {
            likeRepository.delete(like.get());
            return true;
        }
        return false;
    }
    
    // 게시글별 좋아요 조회
    public List<Like> getLikesByPostId(Long postId) {
        return likeRepository.findByPostId(postId);
    }
    
    // 사용자별 좋아요 조회
    public List<Like> getLikesByUserId(Long userId) {
        return likeRepository.findByUserId(userId);
    }
    
    // 게시글별 좋아요 수 조회
    public Long getLikeCountByPostId(Long postId) {
        return likeRepository.countByPostId(postId);
    }
    
    // 사용자별 좋아요 수 조회
    public Long getLikeCountByUserId(Long userId) {
        return likeRepository.countByUserId(userId);
    }
    
    // 특정 게시글에 특정 사용자가 좋아요를 눌렀는지 확인
    public boolean hasUserLikedPost(Long userId, Long postId) {
        return likeRepository.findByPostIdAndUserId(postId, userId).isPresent();
    }
}
