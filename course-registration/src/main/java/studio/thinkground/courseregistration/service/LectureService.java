package studio.thinkground.courseregistration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import studio.thinkground.courseregistration.dto.LectureDTO;
import studio.thinkground.courseregistration.entity.Lecture;
import studio.thinkground.courseregistration.entity.Member;
import studio.thinkground.courseregistration.repository.EnrollmentRepository;
import studio.thinkground.courseregistration.repository.LectureRepository;
import studio.thinkground.courseregistration.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LectureService {

    private final LectureRepository lectureRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final MemberRepository memberRepository;

    public void createLecture(@RequestBody LectureDTO lectureDTO)
    {
        Lecture lecture=new Lecture(
                lectureDTO.getGrade(),
                lectureDTO.getLectureName(),
                lectureDTO.getCredit(),
                lectureDTO.getProfessor(),
                lectureDTO.getTime(),
                lectureDTO.getMaxStudent()
        );
        lectureRepository.save(lecture);
    }
    @Transactional // 자동으로 DB 업데이트
    public void increase(Long lectureId)
    {
        Lecture lecture=lectureRepository.findById(lectureId)
                        .orElseThrow(()->new IllegalArgumentException("강의를 찾을 수 없다"));
        lecture.increaseCount();
    }
    // 2. 강의 목록 조회 , 강의 목록을 보고 어떤 것을 추가해야 할 지, 동료가 추가한 것과 중복이 될 수 있으니
    @Transactional(readOnly = true)//조회만 하면 되므로
    public List<LectureDTO> getAllLectures() {
        return lectureRepository.findAll().stream()
                .map(LectureDTO::new) // Lecture -> LectureDTO 변환
                .collect(Collectors.toList());
    }
}
