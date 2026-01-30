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
        System.out.println("====== ⏳ 테스트 데이터 초기화 시작 ======");

        // 1. 학생 생성
        Student student = new Student();
        // [중요] DB 에러 방지를 위해 필수값(학번 등)을 꼭 넣어야 합니다.
        // (빨간 줄이 뜨면 본인의 Entity 변수명에 맞춰서 setter를 수정하세요!)
        student.setStudentNumber("202112222");
        student.setName("테스트학생"); // 이름 필드가 있다면 주석 해제
         student.setPassword("1234");  // 비밀번호 필드가 있다면 주석 해제

        studentRepository.save(student);

        // 🚨 [Postman 입력용] 진짜 ID 확인
        System.out.println("👉 생성된 학생 ID (studentId): " + student.getStudentId());


        // 2. 강의 생성
        Lecture lecture = new Lecture();
        // [중요] 수강신청 로직(정원 체크) 테스트를 위해 값 설정 필수
        lecture.setMax_student(30);  // 최대 정원 30명
        lecture.setCurrentCount(0);  // 현재 인원 0명

        lectureRepository.save(lecture);

        // 🚨 [Postman 입력용] 진짜 ID 확인
        System.out.println("👉 생성된 강의 ID (lectureId): " + lecture.getId());

        System.out.println("====== 🏁 테스트 데이터 준비 완료 ======");
    }
}