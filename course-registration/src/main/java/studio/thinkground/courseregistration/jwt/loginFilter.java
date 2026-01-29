package studio.thinkground.courseregistration.jwt;

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

import java.util.Collection;
import java.util.Iterator;

public class loginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public loginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // 1. 요청에서 아이디, 비번 추출
        String id = obtainUsername(request);
        String password = obtainPassword(request);

        // 2. 스프링 시큐리티에게 검증 요청
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(id, password, null);

        return authenticationManager.authenticate(authToken);
    }

    // 로그인 성공 시 실행
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        // 1. 유저 정보 꺼내기
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String id = customUserDetails.getUsername();

        // 2. 역할(Role) 꺼내기
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 3. 토큰 생성 (10시간 유효)
        String token = jwtUtil.createJwt(id, role, 60*60*1000*10L);

        response.addHeader("Authorization", "Bearer " + token);

        //학생,관리자 로그인이 다르니 역할 구분
        response.addHeader("Role",role);
        // 로그 확인용 (콘솔에 찍힙니다)
        System.out.println("로그인 성공! 헤더에 토큰 담음: " + token);
    }

    // 로그인 실패 시 실행
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401);
        System.out.println("로그인 실패: " + failed.getMessage());
    }
}