package studio.thinkground.courseregistration.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import studio.thinkground.courseregistration.entity.Lecture;
import studio.thinkground.courseregistration.entity.Member;
import studio.thinkground.courseregistration.repository.MemberRepository;
import studio.thinkground.courseregistration.service.EnrollmentService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;
    private final MemberRepository memberRepository;

    @PostMapping("/enrollment")
    public ResponseEntity<String> enroll(@RequestBody Map<String, String>params) {
        try {
            //프론트에서 보낸 강의 ID꺼내기(json)
            String lectureIdStr = params.get("lectureId");
            Long lectureId = Long.parseLong(lectureIdStr);
            //토큰에서 누가 로그인했는지 아이디 꺼내기
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String loginId = auth.getName();

            //수강신청 서비스
            enrollmentService.enroll(loginId, lectureId);
            return ResponseEntity.ok("수강신청 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/enrollment")
    public ResponseEntity<String> cancel(@RequestBody Map<String,String> params) {
        try {
            String lectureIdStr = params.get("lectureId");
            Long lectureId = Long.parseLong(lectureIdStr);

            //로그인한 사람 ID
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String loginId = auth.getName();

            //취소
            enrollmentService.cancel(loginId, lectureId);

            return ResponseEntity.ok("수강취소 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
