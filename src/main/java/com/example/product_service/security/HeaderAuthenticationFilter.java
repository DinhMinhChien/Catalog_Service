package com.example.product_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HeaderAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Lấy thông tin từ Header do API Gateway truyền xuống
        String username = request.getHeader("X-User-Name");
        String rolesHeader = request.getHeader("X-User-Roles");

        // 2. Nếu có username thì tiến hành tạo Security Context
        if (username != null && !username.isEmpty()) {
            List<GrantedAuthority> authorities = new ArrayList<>();

            if (rolesHeader != null && !rolesHeader.isEmpty()) {
                // Tách chuỗi "ROLE_USER,ROLE_ADMIN" thành mảng và map sang GrantedAuthority
                // Lưu ý: Gateway đã thêm sẵn chữ ROLE_ rồi nên ta dùng luôn
                authorities = Arrays.stream(rolesHeader.split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
            }

            // 3. Tạo đối tượng Authentication (không cần password nên để null)
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            // 4. Lưu vào Context của Spring Security
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 5. Cho phép Request đi tiếp vào Controller
        filterChain.doFilter(request, response);
    }
}
