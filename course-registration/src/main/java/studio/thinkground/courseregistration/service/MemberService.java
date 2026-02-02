package studio.thinkground.courseregistration.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import studio.thinkground.courseregistration.entity.Member;
import studio.thinkground.courseregistration.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void join(Member member)
    {
        //중복체크
        if(memberRepository.findByLoginId(member.getLoginId())!=null)
        {
            throw new RuntimeException("이미 존재합니다");
        }
        //암호화, 회원가입하고 로그인하려면
        String rawPassword= member.getPassword();
        String encPassword=bCryptPasswordEncoder.encode(rawPassword);
        member.setPassword(encPassword);

        memberRepository.save(member);
    }
    public Member login(String loginId,String password)
    {
        Member member=memberRepository.findByLoginId(loginId);

        if(member==null)
        {
            return null;
        }
        //비밀번호가 맞으면
        if(bCryptPasswordEncoder.matches(password,member.getPassword()))
        {
            return member;
        }
        return null;
    }
}
