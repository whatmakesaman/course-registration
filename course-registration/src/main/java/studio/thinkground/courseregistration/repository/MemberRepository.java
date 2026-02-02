package studio.thinkground.courseregistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import studio.thinkground.courseregistration.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByLoginId(String loginId);

}
