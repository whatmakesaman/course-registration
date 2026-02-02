package studio.thinkground.courseregistration.service;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import studio.thinkground.courseregistration.dto.LectureDTO; // DTO import 필수
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.thinkground.courseregistration.entity.Enrollment;
import studio.thinkground.courseregistration.entity.Lecture;
import studio.thinkground.courseregistration.entity.Member;
import studio.thinkground.courseregistration.entity.Student;
import studio.thinkground.courseregistration.repository.EnrollmentRepository;
import studio.thinkground.courseregistration.repository.LectureRepository;
import studio.thinkground.courseregistration.repository.MemberRepository;
import studio.thinkground.courseregistration.repository.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor


public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;//학생,강의를 저장
    private final LectureRepository lectureRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void enroll(String loginId,Long lectureId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 없습니다."));
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("강의가 없습니다"));

        //DB(mysql)에 중복되는 강의가 있는지 검사
        boolean exists= enrollmentRepository.existsByMemberAndLecture(member,lecture);//member.getId , lecture.getId만 빼감
        if(exists)
        {
            throw new IllegalArgumentException("이미 신청한 강의입니다");
        }

        //수강 인원 초과
       if(lecture.getCurrentCount()>=lecture.getMax_student())
       {
           throw new IllegalArgumentException("정원 초과입니다."); // 서비스 로직 강제 종료
       }
       lecture.increaseCount();

       Enrollment enrollment=new Enrollment(lecture,member);

       enrollmentRepository.save(enrollment);


    }
    // 2. 내 신청 목록 조회 (Entity -> DTO 변환)
    @Transactional(readOnly = true)
    public List<LectureDTO> getMyLectures(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        return member.getEnrollments().stream()
                .map(enrollment -> {
                    Lecture l = enrollment.getLecture();
                    // Lecture 엔티티를 LectureDTO로 예쁘게 포장해서 반환
                    return LectureDTO.builder()
                            .id(l.getId())
                            .lectureName(l.getLectureName())
                            .professor(l.getProfessor())
                            .grade(l.getGrade())
                            .credit(l.getCredit())
                            .maxStudent(l.getMax_student()) // 혹시 getter가 getMax_student면 수정
                            .currentCount(l.getCurrentCount())
                            .time(l.getTime())
                            .build();
                })
                .collect(Collectors.toList());
    }

}
