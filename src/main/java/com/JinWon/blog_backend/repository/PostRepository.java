package com.JinWon.blog_backend.repository;

import com.JinWon.blog_backend.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // boardType으로 게시글 필터링
    List<Post> findByBoardType(String boardType);
}
