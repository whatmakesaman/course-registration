package studio.thinkground.courseregistration.jwt;

import com.fasterxml.jackson.databind.ObjectMapper; // ★ 추가
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import studio.thinkground.courseregistration.dto.CustomUserDetails;

import java.io.IOException; // ★ 추가
import java.util.Collection;
import java.util.Iterator;
import java.util.Map; // ★ 추가

public class loginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public loginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            // ★ [수정됨] JSON 데이터를 읽기 위해 ObjectMapper 사용
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> loginData = objectMapper.readValue(request.getInputStream(), Map.class);

            // HTML에서 보낸 키값("username", "password")과 일치해야 함
            String id = loginData.get("username");
            String password = loginData.get("password");

            System.out.println("====== [LoginFilter] 로그인 시도 ======");
            System.out.println("ID: " + id);
            System.out.println("PW: " + password);

            if (id == null || password == null) {
                throw new RuntimeException("ID 또는 비밀번호가 전달되지 않았습니다.");
            }

            // 스프링 시큐리티에게 검증 요청
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(id, password, null);

            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 로그인 성공 시 실행
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String id = customUserDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String token = jwtUtil.createJwt(id, role, 60*60*1000*10L);

        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("Role", role);

        System.out.println("로그인 성공! 헤더에 토큰 담음: " + token);
    }

    // 로그인 실패 시 실행
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
        System.out.println("로그인 실패: " + failed.getMessage());
    }
}