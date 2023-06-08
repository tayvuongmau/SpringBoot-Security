package springsecurity.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

// đây là object thông tin người dùng sẽ được đưa vào security
// sau khi thực hiện truy vấn thành công tài khoản từ db lên thông qua name
public class StudentDetail implements UserDetails {
    private String name;
    private String password;
    private int age;
    private List<GrantedAuthority> authorities;

    public StudentDetail() {
    }

    public StudentDetail(Student student) {
        this.name = student.getName();
        this.password = student.getPassword();
        this.age = student.getAge();
        //vì ví dụ bảng Student mỗi row đc fix cứng 1 role lên cần thao tác biến đổi một chút ở constructor
        this.authorities = Arrays.stream(student.getRole().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
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
