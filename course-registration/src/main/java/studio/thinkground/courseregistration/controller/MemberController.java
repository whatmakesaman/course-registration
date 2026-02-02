package studio.thinkground.courseregistration.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import studio.thinkground.courseregistration.entity.Member;
import studio.thinkground.courseregistration.jwt.JWTUtil;
import studio.thinkground.courseregistration.repository.MemberRepository;
import studio.thinkground.courseregistration.service.MemberService;
import jakarta.servlet.http.HttpServletResponse; // ✅ 여기 들어있습니다
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JWTUtil jwtUtil;

    @PostMapping("/api/join")
    public String join(@RequestBody Member member)//requestbody 프론트엔드에서 보낸 JSON 데이터(아이디, 비번, 이름, 역할)를 Member 객체로 쏙 넣음
    {
        memberService.join(member);
        return "회원가입 성공";
    }

    @PostMapping("/api/login") //프론트에서 보낸 json을 map으로 받음
    public Map<String, Object> login(@RequestBody Map<String, String> loginData,HttpServletResponse response) {
        String loginId = loginData.get("loginId");
        String password = loginData.get("password");

        Member member=memberService.login(loginId,password);
        Map<String,Object> responseMap=new HashMap<>();

        if(member!=null)
        {
            Long expiredMs=1000*60*60L;
            String token=jwtUtil.createJwt(member.getLoginId(), member.getRole(),expiredMs);

            //쿠키 생성 및 설정
            Cookie cookie = new Cookie("Authorization", token); // 쿠키 이름, 값(토큰)
            cookie.setHttpOnly(true);  // 자바스크립트로 접근 불가 (보안 필수!)
            cookie.setPath("/");       // 모든 페이지에서 쿠키 사용 가능
            cookie.setMaxAge(60 * 60); // 쿠키 수명: 1시간 (토큰과 동일하게 맞춤)

            // [추가] 2. 응답에 쿠키 태워 보내기
            response.addCookie(cookie);


            //프론트로 json 넘김
            responseMap.put("message","성공");
            responseMap.put("token",token);
            responseMap.put("role",member.getRole());
            responseMap.put("name",member.getName());
        }
        else {
            responseMap.put("message","실패");
        }
        return responseMap;
    }
}
