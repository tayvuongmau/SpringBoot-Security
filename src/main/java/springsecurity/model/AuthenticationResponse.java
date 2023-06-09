package springsecurity.model;

import java.io.Serializable;

//đây là class trả ra chuỗi jwt về cho người dùng khi đăng nhập thành công
public class AuthenticationResponse implements Serializable {
    private final String jwt;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }
}
