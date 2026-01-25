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
        String id= joinDTO.getId();
        String password= joinDTO.getPassword();

        if(studentRepository.existsByStudentNumber(id))
        {
            return;
        }
        Student data=Student.builder()
                .studentNumber(id)
                .password(bCryptPasswordEncoder.encode(password))
                .name("bjh")
                .role(Role.ROLE_STUDENT)
                .build();
        studentRepository.save(data);
    }
    public void joinAdmin(JoinDTO joinDTO)
    {
        String id= joinDTO.getId();
        String password= joinDTO.getPassword();

        if(adminRepository.existsByLoginId(id)){
            return;
        }
        Admin data=Admin.builder()
                .loginId(id)
                .password(bCryptPasswordEncoder.encode(password))
                .role(Role.ROLE_ADMIN)
                .name("admin")
                .build();
        adminRepository.save(data);

    }
}
