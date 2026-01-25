package studio.thinkground.courseregistration.entity;

import jakarta.persistence.*;
import lombok.*;
import studio.thinkground.courseregistration.domain.Role;

@Table(name="admin")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="admin_id",updatable = false)
    private Long adminId;

    @Column(name="login_id",nullable = false,unique = true)
    private String loginId;

    @Column(name="admin_name",nullable = false)
    private String name;

    @Column(name="password",nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Admin(String loginId, String name, String password, Role role) {
        this.loginId=loginId;
        this.name = name;
        this.password = password;
        this.role = role;
    }
}
