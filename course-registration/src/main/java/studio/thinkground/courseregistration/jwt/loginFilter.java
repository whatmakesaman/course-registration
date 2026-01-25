package studio.thinkground.courseregistration.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
 //                              세션에서 사용하는 기능, '로그인을 검사하는 타이밍'은 같아서 사용
public class loginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public loginFilter(AuthenticationManager authenticationManager)
    {
        this.authenticationManager=authenticationManager;
    }

    @Override                                   //서버 뜯어서 확인,              서버가 채워서 보냄
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
    {
        String id=obtainUsername(request);
        String password=obtainPassword(request);

        //username과 password를 검증하기 위해 토큰에 담음
        UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(id,password,null);
        //토큰을 확인해서 보낼지 말지
        return authenticationManager.authenticate(authToken);
    }
    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        System.out.println("success");
    }
    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        System.out.println("fail");
    }
}
