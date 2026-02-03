package studio.thinkground.courseregistration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import studio.thinkground.courseregistration.entity.Lecture;
import studio.thinkground.courseregistration.entity.Student;
import studio.thinkground.courseregistration.repository.LectureRepository;
import studio.thinkground.courseregistration.repository.StudentRepository;

@Component
@RequiredArgsConstructor
public class DataInit implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final LectureRepository lectureRepository;

    @Override
    public void run(String... args) throws Exception {

        // 1. 학생 데이터 초기화 (학생 데이터가 하나도 없을 때만 실행)
        if (studentRepository.count() == 0) {
            System.out.println("====== 👨‍🎓 학생 데이터 초기화 시작 ======");
            // createStudent("20241234","testStudent");
            // createStudent("20245678", "김철수");
        } else {
            System.out.println("====== 👨‍🎓 학생 데이터가 이미 존재합니다. (SKIP) ======");
        }

        // 2. 강의 데이터 초기화 (강의 데이터가 하나도 없을 때만 실행)
        if (lectureRepository.count() == 0) {
            System.out.println("====== 📚 강의 데이터 초기화 시작 ======");
            createLecture("소프트웨어공학", "조강명", 3, 3, "금(6-8) 14:30~17:20", 40);
            createLecture("디지털신호처리", "정성택", 3, 3, "월(3-4) 11:30~13:20", 34);
            createLecture("모바일프로그래밍", "최우진", 3, 3, "금(5-8) 13:30~17:20", 34);
            createLecture("모바일프로그래밍", "백지훈", 3, 3, "금(1-4) 09:30~13:20", 34);
            createLecture("마이크로프로세서응용", "최종필", 3, 3, "월(6-7) 14:30~16:20", 34);
            createLecture("네트워크프로그래밍", "이보경", 3, 3, "화(7-8) 15:30~17:20", 34);
            createLecture("네트워크프로그래밍", "정의훈", 3, 3, "목(7-8) 15:30~17:20", 34);
            createLecture("운영체제", "오세훈", 2, 3, "화(9-11) 17:25~19:55", 40);
            createLecture("알고리즘", "방영철", 2, 3, "수(9-11) 17:25~19:55", 40);
            createLecture("4차산업혁명시대와법", "한광수", 1, 2, "금(6-8) 14:30~17:20", 50);
            System.out.println("====== 🏁 강의 데이터 초기화 완료 ======");
        } else {
            System.out.println("====== 📚 강의 데이터가 이미 존재합니다. (SKIP) ======");
        }
    }

    private void createStudent(String studentNumber, String name) {
        Student student = new Student();
        student.setStudentNumber(studentNumber);
        student.setName(name);
        studentRepository.save(student);
    }

    private void createLecture(String title, String professor, int grade, int credit, String time, int max) {
        Lecture lecture = new Lecture();
        lecture.setLectureName(title);
        lecture.setProfessor(professor);
        lecture.setGrade(grade);
        lecture.setCredit(credit);
        lecture.setTime(time);
        lecture.setMax_student(max); // 혹시 Setter 이름이 setMaxStudent면 수정하세요
        lecture.setCurrentCount(0);

        lectureRepository.save(lecture);
    }
}