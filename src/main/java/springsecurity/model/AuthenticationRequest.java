package springsecurity.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//đây là dto requestbody ở form đăng nhập lấy thông tin về username và password ng dùng nhập vào
public class AuthenticationRequest {
    private String name;
    private String password;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String name, String password) {
        this.name = name;
        this.password = password;
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
}
