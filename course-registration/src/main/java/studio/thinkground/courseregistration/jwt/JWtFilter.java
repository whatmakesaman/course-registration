package studio.thinkground.courseregistration.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import studio.thinkground.courseregistration.dto.CustomUserDetails;
import studio.thinkground.courseregistration.entity.Member; // ★ 이것만 있으면 됨

import java.io.IOException;

@Component
public class JWtFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWtFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = null;
        // 1. 헤더에서 토큰 꺼내기
        String authorization = request.getHeader("Authorization");

        // 헤더 검사
        if (authorization != null && authorization.startsWith("Bearer ")) {
            token = authorization.split(" ")[1];
        }

        // 헤더에 없으면 쿠키 뒤지기
        if (token == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("Authorization")) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }

        // 토큰 없으면 패스
        if (token == null) {
            System.out.println("토큰이 없습니다");
            filterChain.doFilter(request, response);
            return;
        }

        // 4. 만료 확인
        if (jwtUtil.isExpired(token)) {
            System.out.println("token expired");
            filterChain.doFilter(request, response);
            return;
        }

        // 5. 토큰에서 정보 꺼내기
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        // ★ [수정] 역할 구분 없이 무조건 Member 객체 생성!
        Member member = new Member();
        member.setLoginId(username);
        member.setPassword("temppassword"); // 임시 비번 (어차피 검증용 아님)
        member.setRole(role); // "ROLE_ADMIN" or "ROLE_STUDENT" (String으로 들어감)

        // 이제 CustomUserDetails가 Member를 좋아합니다.
        CustomUserDetails customUserDetails = new CustomUserDetails(member);

        // 7. 스프링 시큐리티 세션에 등록
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        System.out.println("인증 성공: " + username + " (" + role + ")");
        filterChain.doFilter(request, response);
    }
}