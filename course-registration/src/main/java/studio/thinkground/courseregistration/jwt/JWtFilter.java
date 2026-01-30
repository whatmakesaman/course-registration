package studio.thinkground.courseregistration.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import studio.thinkground.courseregistration.domain.Role;
import studio.thinkground.courseregistration.dto.CustomUserDetails;
import studio.thinkground.courseregistration.entity.Admin;
import studio.thinkground.courseregistration.entity.Student;

import java.io.IOException;
@Component
public class JWtFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWtFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. 헤더에서 토큰 꺼내기
        String authorization = request.getHeader("Authorization");

        // 2. 토큰이 없거나, "Bearer "로 시작하지 않으면 통과 (로그인 안 된 상태)
        // [수정] "Bearer" -> "Bearer " (띄어쓰기 추가)
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            System.out.println("token null or invalid header");
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("authorization valid");

        // 3. 토큰만 추출 ("Bearer " 제거)
        String token = authorization.split(" ")[1];

        // 4. 만료 확인
        if (jwtUtil.isExpired(token)) {
            System.out.println("token expired");
            filterChain.doFilter(request, response);
            return;
        }

        // 5. 토큰에서 정보 꺼내기
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        // 6. 역할에 따라 객체 생성 (핵심 로직!)
        CustomUserDetails customUserDetails = null;

        if (role.equals("ROLE_ADMIN")) {
            Admin admin = new Admin();
            admin.setLoginId(username); // Admin 엔티티에 setLoginId가 있어야 함
            admin.setPassword("temppassword");
            admin.setRole(Role.ROLE_ADMIN); // Role Enum 사용

            // CustomUserDetails에 Admin을 받는 생성자가 있어야 함
            customUserDetails = new CustomUserDetails(admin);
        } else {
            Student student = new Student();
            student.setStudentNumber(username); // Student 엔티티에 setStudentNumber가 있어야 함
            student.setPassword("temppassword");
            student.setRole(Role.ROLE_STUDENT); // Role Enum 사용

            // CustomUserDetails에 Student를 받는 생성자가 있어야 함
            customUserDetails = new CustomUserDetails(student);
        }

        // 7. 스프링 시큐리티 세션에 등록
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        System.out.println("최종 권한 확인: " + authToken.getAuthorities());

        filterChain.doFilter(request, response);
    }
}