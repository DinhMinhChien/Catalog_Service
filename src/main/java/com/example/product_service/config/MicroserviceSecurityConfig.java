package com.example.product_service.config;

import com.example.product_service.security.HeaderAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // BẮT BUỘC có để dùng được @PreAuthorize
public class MicroserviceSecurityConfig {

    private final HeaderAuthenticationFilter headerAuthenticationFilter;

    public MicroserviceSecurityConfig(HeaderAuthenticationFilter headerAuthenticationFilter) {
        this.headerAuthenticationFilter = headerAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Tắt CSRF cho API
                // Chạy chế độ không lưu trạng thái (Stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Các API nội bộ hoặc công khai thì cấu hình ở đây (tuỳ chọn)
                        .requestMatchers("/v1/categories/**").permitAll()
                        .requestMatchers("/v1/products/**").permitAll()
                        // Còn lại bắt buộc phải có thông tin xác thực (đã được nhét từ Filter)
                        .anyRequest().authenticated()
                )
                // Chèn cái Filter đọc Header của chúng ta vào TRƯỚC Filter xác thực mặc định
                .addFilterBefore(headerAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
