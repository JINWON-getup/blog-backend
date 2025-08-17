package com.JinWon.blog_backend.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class User {

    // 기본 회원 정보       
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Boolean isActive = true;

    // 포인트 시스템
    @Column(nullable = false)
    private Integer point = 0;

    @Column(nullable = false)
    private Integer level = 1;

    @Column(nullable = false)
    private Integer experience = 0;

    @Column(nullable = false)
    private Integer totalPost = 0;

    @Column(nullable = false)
    private Integer totalComment = 0;

    @Column(nullable = false)
    private Integer totalLike = 0;

    @Column
    private LocalDateTime lastLogin;

    @Column(nullable = false)
    private Integer consecutiveLogin = 0;

    // 관계 설정 - CASCADE 삭제
    @OneToMany(mappedBy = "user", cascade = jakarta.persistence.CascadeType.REMOVE)
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = jakarta.persistence.CascadeType.REMOVE)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = jakarta.persistence.CascadeType.REMOVE)
    private List<Like> likes;

    // 레벨업 시스템
    public boolean checkLevelUp() {
        int requiredExp = mathRequiredExp(level + 1);
        if (experience >= requiredExp) {
            level++;
            experience -= requiredExp;
            return true;
        }
        return false;
    }

    // 다음 레벨까지 필요한 경험치 계산
    private int mathRequiredExp(int nextLevel) {
        // 최대 레벨 100 제한
        if (nextLevel > 100) {
            return Integer.MAX_VALUE;  // 레벨업 불가
        }
        return nextLevel * 100;
    }

    // 포인트 추가
    public void addPoint(int value) {
        this.point += value;
        this.experience += value;
        checkLevelUp();
    }

    // 포인트 차감
    public boolean subtractPoint(int value) {
        if (this.point >= value) {
            this.point -= value;
            return true;
        }
        return false;
    }

    // 게시글 작성 시 포인트 지급
    public void addPostPoint() {
        addPoint(50);
        totalPost++;
    }

    // 댓글 작성 시 포인트 지급
    public void addCommentPoint() {
        addPoint(10);
        totalComment++;
    }

    // 좋아요 받았을 때 포인트 지급
    public void addLikePoint() {
        addPoint(5);
        totalLike++;
    }

    // 일일 로그인 체크 및 포인트 지급
    public boolean checkDailyLogin() {
        LocalDateTime now = LocalDateTime.now();
        
        if (lastLogin == null || 
            lastLogin.toLocalDate().isBefore(now.toLocalDate())) {
            
            lastLogin = now;
            consecutiveLogin++;
            
            // 연속 로그인 보너스 (최대 50점)
            int bonusPoints = Math.min(consecutiveLogin * 5, 50);
            int totalPoints = 20 + bonusPoints;
            
            addPoint(totalPoints);
            return true;
        }
        
        return false;
    }
}
