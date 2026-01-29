package studio.thinkground.courseregistration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.thinkground.courseregistration.dto.LectureDTO;
import studio.thinkground.courseregistration.entity.Lecture;
import studio.thinkground.courseregistration.repository.LectureRepository;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;

    public void createLecture(LectureDTO lectureDTO)
    {
        Lecture lecture=new Lecture(
                lectureDTO.getLectureName(),
                lectureDTO.getProfessor(),
                lectureDTO.getCredit(),
                lectureDTO.getGrade(),
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
}
