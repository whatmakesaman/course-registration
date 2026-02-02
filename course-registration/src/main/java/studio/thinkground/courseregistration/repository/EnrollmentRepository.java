package studio.thinkground.courseregistration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import studio.thinkground.courseregistration.entity.Enrollment;
import studio.thinkground.courseregistration.entity.Lecture;
import studio.thinkground.courseregistration.entity.Member;
import studio.thinkground.courseregistration.entity.Student;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment,Long> {

    boolean existsByMemberAndLecture(Member member, Lecture lecture);
    List<Enrollment> findAllByMemberId(Long memberId);

}
