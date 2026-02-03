package studio.thinkground.courseregistration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import studio.thinkground.courseregistration.dto.CustomUserDetails;
import studio.thinkground.courseregistration.entity.Member;
import studio.thinkground.courseregistration.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        System.out.println("로그인 시도 (Member 테이블 조회): " + loginId);

        // Member 테이블에서 아이디로 조회
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> {
                    System.out.println("❌ Member 테이블에서 찾을 수 없음: " + loginId);
                    return new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + loginId);
                });

        System.out.println("✅ Member 발견: " + member.getName() + " (" + member.getRole() + ")");

        // CustomUserDetails에 Member 객체를 담아서 리턴
        return new CustomUserDetails(member);
    }
}