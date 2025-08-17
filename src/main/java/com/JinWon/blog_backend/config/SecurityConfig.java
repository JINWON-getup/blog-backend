package com.JinWon.blog_backend.config;

import com.JinWon.blog_backend.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    
    @Autowired
    private AdminRepository adminRepository;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                // AdminLogin 테이블에서 사용자 정보 조회
                return adminRepository.findById(username)
                    .map(admin -> User.builder()
                        .username(admin.getId())
                        .password(admin.getPassword())
                        .roles(admin.getRole() != null ? admin.getRole() : "ADMIN")
                        .build())
                    .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));
            }
        };
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.and())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/admin/login").permitAll()      // 로그인 API 허용
                .requestMatchers("/api/admin/logout").permitAll()     // 로그아웃 API 허용
                .requestMatchers("/api/admin/*/password").permitAll() // 비밀번호 변경 API 허용
                .requestMatchers("/api/admin/*/reset-password").permitAll() // 비밀번호 초기화 API 허용
                .requestMatchers("/api/admin/*").permitAll()          // 관리자 정보 조회/수정 API 허용
                .requestMatchers("/api/users/register").permitAll()   // 사용자 등록 API 허용
                .requestMatchers("/api/users/login").permitAll()      // 사용자 로그인 API 허용
                .requestMatchers("/api/users/logout").permitAll()     // 사용자 로그아웃 API 허용
                .requestMatchers("/api/users/**").permitAll()         // 기타 사용자 API 허용
                .requestMatchers("/api/comments/**").permitAll()      // 댓글 API 허용
                .requestMatchers("/api/likes/**").permitAll()         // 좋아요 API 허용
                .requestMatchers("/api/test/**").permitAll()          // 테스트 API 허용
                .requestMatchers("/api/post").permitAll()             // 게시글 목록 조회 허용
                .requestMatchers("/api/post/*").permitAll()           // 게시글 상세 조회 허용
                .anyRequest().authenticated()                         // 나머지는 인증 필요
            )
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}