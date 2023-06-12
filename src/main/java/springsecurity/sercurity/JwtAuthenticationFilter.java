package springsecurity.sercurity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import springsecurity.model.StudentDetail;
import springsecurity.service.MyUserDetailService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//Đây là nơi kiểm tra request của người dùng gửi lên trước khi tới nơi xử lí request
//Nó sẽ lấy Header Authorization ra và kiểm tra xem chuỗi JWT người dùng gửi lên có hợp lệ không
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenProvider tokenProvider;
    private MyUserDetailService userDetailService;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, MyUserDetailService userDetailService) {
        this.tokenProvider = tokenProvider;
        this.userDetailService = userDetailService;
    }

    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    //lấy chuỗi jwt từ header  trong request
    private String getJwtFromRequest(HttpServletRequest request){
        //lấy ra nguyên 1 chuỗi String ở hearder , là giá trị của key "Authorization"
        String bearerToken = request.getHeader("Authorization");
        // Kiểm tra xem header Authorization có chứa thông tin jwt không
        //chuỗi không null và bắt đầu bằng Bearer
        if (bearerToken!=null && bearerToken.startsWith("Bearer ")){
            //từ chuỗi lấy được từ hearder cắt lấy đoạn jwt bắt đầu từ vị trí thứ 7 (bỏ qua phần "Bearer ")
            logger.info("Lấy được đoạn mã token thành công");
            return bearerToken.substring(7);
        }
//        logger.error("Không tồn tại token nào");
        return "Không tồn tại token nào";
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                                    throws ServletException, IOException {
        try {
            // Lấy jwt từ request
            String jwt = getJwtFromRequest(request);
            if (jwt != null && tokenProvider.validateToken(jwt)){
                //lấy username từ jwt
                String username = tokenProvider.getUsernameFromJwt(jwt);
                StudentDetail studentDetail = userDetailService.loadUserByUsername(username);
                if (studentDetail != null){
                    //nếu người dùng hợp lệ , set thông tin cho Security Context (chứa all thông tin người dùng)
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(studentDetail, null, studentDetail.getAuthorities());
                    authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.info("Thông tin người dùng hợp lệ");
                }
            }
        }catch (Exception e){
            logger.error("Đưa thông tin người dùng vào Authentication tất bại");
        }
        filterChain.doFilter(request,response);
    }
}
