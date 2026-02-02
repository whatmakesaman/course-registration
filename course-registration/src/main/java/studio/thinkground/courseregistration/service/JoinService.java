package studio.thinkground.courseregistration.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import studio.thinkground.courseregistration.domain.Role;
import studio.thinkground.courseregistration.dto.JoinDTO;
import studio.thinkground.courseregistration.entity.Admin;
import studio.thinkground.courseregistration.entity.Student;
import studio.thinkground.courseregistration.repository.AdminRepository;
import studio.thinkground.courseregistration.repository.StudentRepository;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public void joinStudent(JoinDTO joinDTO)
    {
        String id= joinDTO.getLoginId();
        String password= joinDTO.getPassword();
        String name= joinDTO.getName();

        if(studentRepository.existsByStudentNumber(id))
        {
            throw new RuntimeException("이미 존재하는 계정입니다");
        }
        //DB에 넣을 객체 생성
        Student data=Student.builder()
                .studentNumber(id)
                .password(bCryptPasswordEncoder.encode(password))
                .name(name)
                .role(Role.ROLE_STUDENT)
                .build();
        studentRepository.save(data);
    }
    public void joinAdmin(JoinDTO joinDTO)
    {
        String id= joinDTO.getLoginId();
        String password= joinDTO.getPassword();
        String name= joinDTO.getName();
        if(adminRepository.existsByLoginId(id)){
            throw new RuntimeException("이미 존재하는 계정입니다");
        }
        Admin data=Admin.builder()
                .loginId(id)
                .password(bCryptPasswordEncoder.encode(password))
                .role(Role.ROLE_ADMIN)
                .name(name)
                .build();
        adminRepository.save(data);

    }
}
