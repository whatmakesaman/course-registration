package studio.thinkground.courseregistration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import studio.thinkground.courseregistration.dto.CustomUserDetails;
import studio.thinkground.courseregistration.entity.Admin;
import studio.thinkground.courseregistration.entity.Student;
import studio.thinkground.courseregistration.repository.AdminRepository;
import studio.thinkground.courseregistration.repository.StudentRepository;

import javax.sound.midi.Soundbank;

@Service
public class CustomUserDetailService implements UserDetailsService {
        private  final StudentRepository studentRepository;
        private  final AdminRepository adminRepository;

        // 생성자를 따로 만들시 다른 하나가 null이 생겨 오류
        public CustomUserDetailService(StudentRepository studentRepository,AdminRepository adminRepository)
        {
            this.studentRepository=studentRepository;
            this.adminRepository=adminRepository;
        }



        @Override
        public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException
        {
            System.out.println("로그인 시도: "+id);
            Student student=studentRepository.findByStudentNumber(id);
            if(student!=null)
            {
                System.out.println("학생 발견, 비밀번호: "+student.getPassword());
                return new CustomUserDetails(student);
            }
            else {
                System.out.println("학생 테이블에 없음");
            }
            Admin admin=adminRepository.findByLoginId(id);
            if(admin!=null)
            {
                System.out.println("관리자 발견");
                return new CustomUserDetails(admin);
            }
            System.out.println("아무것도 못찾음");
            throw new UsernameNotFoundException("error"+id);
        }

}
