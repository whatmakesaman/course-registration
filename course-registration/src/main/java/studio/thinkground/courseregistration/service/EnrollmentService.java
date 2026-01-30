package studio.thinkground.courseregistration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.thinkground.courseregistration.entity.Enrollment;
import studio.thinkground.courseregistration.entity.Lecture;
import studio.thinkground.courseregistration.entity.Student;
import studio.thinkground.courseregistration.repository.EnrollmentRepository;
import studio.thinkground.courseregistration.repository.LectureRepository;
import studio.thinkground.courseregistration.repository.StudentRepository;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;//학생,강의를 저장
    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public void enroll(Long studentId,Long lectureId) {
        Student student = studentRepository.findById(studentId) // student테이블로 가서 id가 붙은 것을 찾음
                .orElseThrow(() -> new IllegalArgumentException("학생이 없습니다"));
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("강의가 없습니다"));

        //DB에 중복되는 강의가 있는지 검사
        boolean exists= enrollmentRepository.existsByStudentAndLecture(student,lecture);
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

       Enrollment enrollment=new Enrollment(lecture,student);

       enrollmentRepository.save(enrollment);


    }


}
