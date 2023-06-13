package springsecurity.model;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

public class RegisterStudent {
    @NotNull(message = "Tên đăng nhập không được bỏ trống")
    @Column(unique = true)
    private String name;
    @NotNull(message = "Mật khẩu không được bỏ trống")
    private String password;
    @NotNull(message = "Tuổi không được bỏ trống")
    @Pattern(regexp = "^[0-9]+$", message = "Tuổi phải nhập số nguyên")
    private String age;
    @NotNull(message = "Họ tên không được bỏ trống")
    private String fullname;
    private String role = "ROLE_USER";
    @Email(message = "Email không đúng định dạng", regexp = "^[A-Za-z0-9]+[A-Za-z0-9]*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)$")
    @NotNull(message = "Email không hợp lệ")
    private String email;
    private boolean activated = false;

    public RegisterStudent() {
    }

    public RegisterStudent(String name, String password, String age, String fullname, String role, String email, boolean activated) {
        this.name = name;
        this.password = password;
        this.age = age;
        this.fullname = fullname;
        this.role = role;
        this.email = email;
        this.activated = activated;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getAge() {
        return age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public String getFullname() {
        return fullname;
    }
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public boolean isActivated() {
        return activated;
    }
    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
