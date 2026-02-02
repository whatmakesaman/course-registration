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

        String token=null;
        // 1. 헤더에서 토큰 꺼내기
        String authorization = request.getHeader("Authorization");

      //헤더 검사
        if(authorization!=null && authorization.startsWith("Bearer "))
        {
            token=authorization.split(" ")[1];
        }

        //헤더에 토큰이 없으면 쿠키 뒤지기
        if(token==null)
        {
            Cookie[] cookies= request.getCookies();
            if(cookies!=null)
            {
                for(Cookie cookie:cookies)
                {
                    if(cookie.getName().equals("Authorization"))
                    {
                        token= cookie.getValue();
                        break;
                    }
                }
            }
        }
       //헤더도 없고, 쿠키도 없을 시 >>>>> 로그인 안 한 사용자
        if(token==null)
        {
            System.out.println("토큰이 없습니다");
            filterChain.doFilter(request,response);
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
        System.out.println("인증 성공: " + username + " (" + role + ")");
        filterChain.doFilter(request, response);
    }
}