package com.teamteskboard.common.filter;

import static com.teamteskboard.common.utils.JwtUtil.BEARER_PREFIX;

import com.teamteskboard.common.config.SecurityUser;
import com.teamteskboard.common.enums.UserRoleEnum;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.teamteskboard.common.utils.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // JWT 검증이 필요 없는 경우
        String requestURI = request.getRequestURI(); // 요청 URI
        String method = request.getMethod(); // 요청 메서드
        if(method.equals("POST") && (requestURI.equals("/api/auth/login") || requestURI.equals("/api/users"))) {
            filterChain.doFilter(request,response); // 필터 통과
            return;
        }

        // JWT 토큰이 있는지 없는지 검사
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            log.info("JWT 토큰이 필요 합니다.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰이 필요 합니다.");
            return;
        }

        // JWT 토큰이 있다면 유효한 토큰인지 검사
        String jwt = authorizationHeader.substring(7);
        if (!jwtUtil.validateToken(jwt)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\": \"Unauthorized\"}");
        }

        // JWT 토큰에서 복호화 한 데이터 저장하기
        Long userId = jwtUtil.extractUserId(jwt); // 유저 ID
        String username = jwtUtil.extractUsername(jwt); // 유저 이름
        String auth = jwtUtil.extractRole(jwt); // 권한
        UserRoleEnum userRole = UserRoleEnum.valueOf(auth);

        SecurityUser user = new SecurityUser(userId, username,"", List.of(userRole::getRole));
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

        filterChain.doFilter(request, response); // 필터 통과

    }
}