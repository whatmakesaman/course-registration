package studio.thinkground.courseregistration.controller;


import lombok.RequiredArgsConstructor;
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
    public String enroll(@RequestBody Map<String, String>params)
    {
        //프론트에서 보낸 강의 ID꺼내기(json)
        String lectureIdStr= params.get("lectureId");
        Long lectureId=Long.parseLong(lectureIdStr);
        //토큰에서 누가 로그인했는지 아이디 꺼내기
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        String loginId= auth.getName();
        //loginId로 진짜 회원 정보 찾기
        Member member=memberRepository.findByLoginId(loginId)
                .orElseThrow(()->new IllegalArgumentException("회원 정보를 찾을 수 없습니다"));


        //수강신청 서비스
        enrollmentService.enroll(loginId, lectureId);
        return "수강신청 완료";
    }


}
