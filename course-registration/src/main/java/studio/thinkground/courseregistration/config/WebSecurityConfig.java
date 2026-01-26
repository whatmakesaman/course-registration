package studio.thinkground.courseregistration.config;

import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import studio.thinkground.courseregistration.jwt.JWTUtil;
import studio.thinkground.courseregistration.jwt.loginFilter;
import studio.thinkground.courseregistration.jwt.JWtFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;


    public WebSecurityConfig(AuthenticationConfiguration authenticationConfiguration,JWTUtil jwtUtil)
    {
        this.authenticationConfiguration=authenticationConfiguration;
        this.jwtUtil=jwtUtil;
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

        http.cors((corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                CorsConfiguration configuration = new CorsConfiguration();

                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                configuration.setAllowedMethods(Collections.singletonList("*"));
                configuration.setAllowCredentials(true);
                configuration.setAllowedHeaders(Collections.singletonList("*"));
                configuration.setMaxAge(3600L);

                configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                return configuration;
            }
        })));



        //csrf disable
        http.csrf((auth) -> auth.disable());
        //form로그인 방식 disable
        http.formLogin((auth)-> auth.disable());
        //http basic 인증 방식 disable
        http.httpBasic((auth)->auth.disable());

        //경로별 인가
        http.authorizeHttpRequests((auth)->auth
                //누구나 접속 가능한 페이지
                .requestMatchers("/login","/","/join/**").permitAll()//join은 나중에 수정
                //관리자만 접속 가능한 페이지
                    .requestMatchers("/admin/**").hasRole("ADMIN") //** 추가해야 자유롭게 사용 가능
                    .requestMatchers("/student/**").hasRole("STUDENT")
                //그 외 모든 요청, 로그인을 한 상태
                .anyRequest().authenticated());

        http.addFilterBefore(new JWtFilter(jwtUtil), loginFilter.class);
        http.addFilterAt(new loginFilter(authenticationManager(authenticationConfiguration),jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //토큰 사용하기 위한 세션 설정
        http.sessionManagement((session)->session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();

    }
}



