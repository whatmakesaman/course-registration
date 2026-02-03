package studio.thinkground.courseregistration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.thinkground.courseregistration.entity.Member;
import studio.thinkground.courseregistration.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void join(String loginId, String password, String name, String role) {

        // 중복 체크
        if (memberRepository.findByLoginId(loginId).isPresent()) {
            throw new RuntimeException("이미 존재합니다");
        }

        Member member = new Member();
        member.setLoginId(loginId);
        member.setPassword(bCryptPasswordEncoder.encode(password)); // ★ 암호화만 챙기세요
        member.setName(name); // setName인지 setStudentName인지 확인 필요
        member.setRole(role); // "ROLE_STUDENT" 등

        memberRepository.save(member);
    }
}