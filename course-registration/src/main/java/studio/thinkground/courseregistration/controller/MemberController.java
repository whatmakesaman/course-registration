package studio.thinkground.courseregistration.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import studio.thinkground.courseregistration.service.MemberService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // Member 객체 대신 Map으로 받아서, 서비스의 바뀐 메서드(String 4개 받는 것)를 호출해야 합니다.
    @PostMapping("/api/join")
    public ResponseEntity<String> join(@RequestBody Map<String, String> params) {
        try {
            System.out.println("회원가입 요청 들어옴: " + params.get("loginId")); // 로그 확인용

            memberService.join(
                    params.get("loginId"),  // 학번 또는 관리자ID
                    params.get("password"), // 비밀번호
                    params.get("name"),     // 이름
                    params.get("role")      // 역할 (ROLE_STUDENT 등)
            );
            return ResponseEntity.ok("회원가입 성공");

        } catch (RuntimeException e) {
            // "이미 존재하는 아이디입니다" 같은 에러 메시지를 프론트로 보냄
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}