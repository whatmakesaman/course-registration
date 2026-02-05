package studio.thinkground.courseregistration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.thinkground.courseregistration.dto.LectureDTO;
import studio.thinkground.courseregistration.entity.Enrollment;
import studio.thinkground.courseregistration.entity.Lecture;
import studio.thinkground.courseregistration.entity.Member;
import studio.thinkground.courseregistration.repository.EnrollmentRepository;
import studio.thinkground.courseregistration.repository.LectureRepository;
import studio.thinkground.courseregistration.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final LectureRepository lectureRepository;
    private final MemberRepository memberRepository;

    // 최대 학점 정의
    private static final int MAX_CREDIT = 21;

    @Transactional
    public void enroll(String loginId, Long lectureId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 없습니다."));
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("강의가 없습니다"));

        // 1. 중복 신청 검사( *****jmeter 테스트를 위하여 잠시 주석 처리*********)
//        boolean exists = enrollmentRepository.existsByMemberAndLecture(member, lecture);
//        if (exists) {
//            throw new IllegalArgumentException("이미 신청한 강의입니다");
//        }

        // 2. 수강 인원 초과 검사
        if (lecture.getCurrentCount() >= lecture.getMax_student()) {
            throw new IllegalArgumentException("정원 초과입니다.");
        }

//        // 3. 학점 초과 검사 **********jmeter 테스트를 위하여 잠시 주석 처리*********
//        int currentTotalCredit = 0;
//        List<Enrollment> myEnrollments = member.getEnrollments(); // Member 엔티티 수정 필요
//
//        for (Enrollment enrollment : myEnrollments) {
//            currentTotalCredit += enrollment.getLecture().getCredit();
//        }
//
//        if (currentTotalCredit + lecture.getCredit() > MAX_CREDIT) {
//            throw new IllegalArgumentException("신청 가능 학점을 초과했습니다");
//        }

        // 4. 저장
        lecture.increaseCount(); // 수강 인원 증가

        //*********jmeter를 위해 잠시 주석*********
//        Enrollment enrollment = new Enrollment(lecture, member); // Enrollment 생성
//        enrollmentRepository.save(enrollment);
    }

    // 내 신청 목록 조회
    @Transactional(readOnly = true)
    public List<LectureDTO> getMyLectures(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));

        List<LectureDTO> dtoList = new ArrayList<>();

        for (Enrollment enrollment : member.getEnrollments()) {
            Lecture l = enrollment.getLecture();

            // DTO 변환 (필드명은 본인 LectureDTO와 맞춰주세요)
            LectureDTO dto = LectureDTO.builder()
                    .id(l.getId())
                    .lectureName(l.getLectureName())
                    .professor(l.getProfessor())
                    .grade(l.getGrade())
                    .credit(l.getCredit())
                    .maxStudent(l.getMax_student())
                    .currentCount(l.getCurrentCount())
                    .time(l.getTime())
                    .build();

            dtoList.add(dto);
        }
        return dtoList;
    }

    // 수강 취소
    @Transactional
    public void cancel(String loginId, Long lectureId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("회원 없음"));
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("강의 없음"));

        Enrollment enrollment = enrollmentRepository.findByMemberAndLecture(member, lecture)
                .orElseThrow(() -> new IllegalArgumentException("신청 내역이 없습니다"));

        lecture.decreaseCount(); // 인원 감소
        enrollmentRepository.delete(enrollment); // 내역 삭제
    }
}