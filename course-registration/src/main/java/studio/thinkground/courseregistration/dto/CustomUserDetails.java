package studio.thinkground.courseregistration.dto;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import studio.thinkground.courseregistration.entity.Admin;
import studio.thinkground.courseregistration.entity.Student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



public class CustomUserDetails implements UserDetails {

    private   Student student;
    private   Admin admin;

    // ✅ [중요] 생성자 1: 학생용 (학생만 받음)
    public CustomUserDetails(Student student) {
        this.student = student;
    }

    // ✅ [중요] 생성자 2: 관리자용 (관리자만 받음)
    public CustomUserDetails(Admin admin) {
        this.admin = admin;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection=new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public  String getAuthority() {
                if (student != null) {
                    return student.getRole().toString();
                }
                if (admin != null) {
                    return admin.getRole().toString();
                }
                return null;
            }
        });
        return collection;
    }

    @Override
    public @Nullable String getPassword() {
        if(student!=null)
        {
            return student.getPassword();
        }
        if(admin!=null)
        {
            return admin.getPassword();
        }
        return null;
    }

    @Override
    public String getUsername() {
        if(student!=null)
        {
            return student.getStudentNumber();
        }
        if(admin!=null)
        {
            return admin.getLoginId();
        }
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
