package springsecurity.sercurity;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import springsecurity.model.StudentDetail;

import java.util.Date;

//đây là nơi xây dựng jwt và thao tác liên quan
@Component
public class JwtTokenProvider {

    private final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    //Đoạn JWT_SECRET này là bí mật, chỉ có phía server biết
    //Đoạn mã này sẽ đc mã hoá và là 1 trong 3 thành phần tạo lên token
    private final String JWT_SECRET = "SECRET";

    //Thời gian có hiệu lực của chuỗi jwt (7 ngày = 1000*60*60*24*7)
    private final long JWT_EXPIRATION = 604800000L;

    // Tạo ra jwt từ thông tin user
    public String generateToken(StudentDetail studentDetail){
        //tạo thời gian tồn tại của jwt
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        // Tạo chuỗi json web token từ username của user.
        return Jwts.builder()
                .setSubject(studentDetail.getUsername())        //lấy thông tin username đăng nhập
                .setIssuedAt(now)                               //thời gian bắt đầu tính
                .setExpiration(expiryDate)                      //thời gian có hiệu lực
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET) //mã hoá 1 phần mã bí mật trong token thay vì để secret
                .compact();
    }

    //lấy ngược thông tin người dùng ra từ token được đính kèm request gửi lên server
    public Claims getUserFromJwt(String token){
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    //lấy ra id từ Claims chứa thông tin user nhận được từ jwt
    public int getUserIdFromJwt(String token){
        Claims claims = getUserFromJwt(token);
        return Integer.parseInt(claims.getSubject());
    }
    //lấy ra username từ Claims chứa thông tin user nhận được từ jwt
    public String getUsernameFromJwt(String token){
        Claims claims = getUserFromJwt(token);
        return claims.getSubject();
    }

    //kiểm tra token từ request gửi lên có hợp lệ không
    public boolean validateToken(String authToken){
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        }catch (MalformedJwtException ex){
            logger.error("Invalid JWT token");
        }catch (ExpiredJwtException ex){
            logger.error("Expired JWT token");
        }catch (UnsupportedJwtException  ex){
            logger.error("Unsupported JWT token");
        }catch (IllegalArgumentException  ex){
            logger.error("JWT claims string is empty");
        }
        return false;
    }
























}
