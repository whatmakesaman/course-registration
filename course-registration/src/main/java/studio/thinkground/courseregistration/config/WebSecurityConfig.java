package studio.thinkground.courseregistration.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Repository;
import studio.thinkground.courseregistration.jwt.loginFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    public WebSecurityConfig(AuthenticationConfiguration authenticationConfiguration)
    {
        this.authenticationConfiguration=authenticationConfiguration;
    }

    //암호화
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception
   {
       return configuration.getAuthenticationManager();
   }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //csrf disable
        http.csrf((auth) -> auth.disable());
        //form로그인 방식 disable
        http.formLogin((auth)-> auth.disable());
        //http basic 인증 방식 disable
        http.httpBasic((auth)->auth.disable());

        //경로별 인가
        http.authorizeHttpRequests((auth)->auth
                //누구나 접속 가능한 페이지
                .requestMatchers("/login","/","/join**").permitAll()//join은 나중에 수정
                //관리자만 접속 가능한 페이지
                    .requestMatchers("/admin").hasRole("ADMIN")
                    .requestMatchers("/student").hasRole("STUDENT")
                //그 외 모든 요청, 로그인을 한 상태
                .anyRequest().authenticated());

        http.addFilterAt(new loginFilter(authenticationManager(authenticationConfiguration)), UsernamePasswordAuthenticationFilter.class);

        //토큰 사용하기 위한 세션 설정
        http.sessionManagement((session)->session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();

    }
}



