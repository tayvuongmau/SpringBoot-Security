Xác thực– phân quyền với Spring Security + JWT 
1. Giới thiệu 
Spring Security là một trong những phần quan trọng của Spring Framework, nó giúp chúng ta phân quyền và xác thực người dùng trước khi cho phép họ truy cập vào các tài nguyên phù hợp với vai trò của từng đối tượng tiến hành đăng nhập.
Lưu ý : Nếu như ở phiên bản Spring Security 5 chúng ta cấu hình Security bằng cách extends class WebSecurityConfigAdapter, thì ở phiên bản mới nhất hiện tại class WebSecurityConfigAdapter đã không còn được Spring Boot hỗ trợ và bị xóa ra khỏi thư viện Spring Security 6. 
JWT (JSON Web Token) là một chuỗi mã hóa được gửi kèm trong Header của Client request có tác dụng giúp phía Server xác thực request người dùng có hợp lệ hay không. Mỗi khi người dùng tiến hành đăng nhập thành công sẽ được phía Server sinh ra một JWT trả lại phía Client sẽ được lưu ở session hoặc cookie trong một thời gian tuỳ thuộc phía Server quy định . JWT dưới dạng JSON bao gồm ba phần chính: Header, Payload và Signature.
- Header: Chứa thông tin về loại mã hóa và thuật toán được sử dụng để mã hóa JWT.
- Payload: Chứa các thông tin chứng thực (claims) như thông tin người dùng, quyền hạn, thông tin khác liên quan đến ứng dụng.
- Signature: Được tạo ra bằng cách ký (sign) Header và Payload sử dụng một khóa bí mật (secret key) hoặc một cặp khóa công khai, riêng tư.

2. Chuẩn bị
Các thư viện cần có:
- Spring Boot version 2.1.6.RELEASE
- Spring Security (tự cập nhật theo version của Spring Boot)
- Spring data Jpa
- Spring Web
- MySQL Driver ( dùng db khác sẽ tải driver tương ứng)
- io.jsonwebtoken.jwtt (lấy từ bên ngoài do trong spring boot không có, dùng để mã hoá thông tin thành  jwt)

3. Luồng hoạt động
<img src="https://i.imgur.com/uwceXgB.png">

4. Xây dựng project
Tạo package Model có chứa class object, trong đó class Student tham chiếu tới bảng trong database
<img src="https://i.imgur.com/f2boRz9.png">

Mặc định trong Spring Security sẽ  sử dụng 1 đối tượng UserDetails để chứa toàn bộ thông tin người dùng, lên ta cần tạo 1 class chuyển các thông tin từ Student thành StudentDetail
<img src="https://i.imgur.com/oNJoC2V.png">
Ta có thêm 2 class , class AuthenticationRequest dùng để chứa thông tin đăng nhập từ Client gửi lên và class AuthenticationResponse dùng để chứa chuỗi Jwt trả về cho Client
<img src="https://i.imgur.com/ketdXlg.png">
<img src="https://i.imgur.com/rzgthZ4.png">

Khi người dùng đăng nhập thì Spring Security sẽ cần lấy các thông tin UserDetails hiện có để kiểm tra. Vì vậy, bạn cần tạo ra một class kế thừa lớp UserDetailsService mà Spring Security cung cấp để làm nhiệm vụ này. Nếu tồn tại tài khoản đăng nhập thì sẽ trả ra dữ liệu là StudentDetail để Security tiến hành xử lí các tác vụ tiếp theo, vì Security sẽ không làm việc với Object thông thường mà chỉ làm việc với Object UserDetails.
<img src="https://i.imgur.com/gvvguah.png">
Sau khi đã có được thông tin của người dùng, phía Server sẽ tiến hành mã hoá thông tin của người dùng thành chuỗi JWT, ta tiến hành tạo class JwtTokenProvider để triển khai các thao tác liên quan tới việc tạo ra JWT
<img src="https://i.imgur.com/276hhYM.png">
Ngoài ra tại class này ta sẽ triển khai đồng thời các method liên quan tới việc xác thực ngược lại token được gửi cùng request từ Client gửi lên, để kiểm tra xem token có đúng với của tài khoản tiến hành đăng nhập khi yêu cầu truy cập tới một Url nào đó không.
<img src="https://i.imgur.com/13IjXJz.png">

Tiếp theo ta cần tạo ra một class Filter có tên là JwtAuthenticationFilter, class Filter này sẽ có tác dụng kiểm tra token được đính kèm theo request được gửi lên từ Client trước khi request tới đích. Nó sẽ lấy Header Authorization ra và kiểm tra xem chuỗi JWT người dùng gửi lên có hợp lệ không.
Đầu tiên là thao tác lấy ra được mã token từ request : 
<img src="https://i.imgur.com/99kOyBq.png">
Tiếp theo là thao tác kiểm tra token (generate ngược lấy dữ liệu từ token gửi lên)
<img src="https://i.imgur.com/Wy8AVtb.png">
Ta tiến hành cấu hình Security và kích hoạt cho phép Security được phép kiểm tra xác thực và phân quyền truy cập cho bất kì tài khoản nào tiến hành đăng nhập. 
Lưu ý : vì đây là ví dụ lên sẽ không yêu cầu mật khẩu được mã hoá , thực tế sẽ phải có 1 method tiến hành mã hoá mật khẩu, và kiểm tra mật khẩu mã hoá đó!
<img src="https://i.imgur.com/Jh8M942.png">

Cuối cùng ta xây dựng Controller , nơi sẽ cấu hình các Url nào sẽ được quyền truy cập bởi các tài khoản có role nào , và quan trọng nhất là Url login sẽ trả ra chuỗi Jwt cho người dùng khi họ đăng nhập thành công.
Đầu tiên là method sẽ trả lại chuỗi token cho Client khi nhận được thông tin đăng nhập từ phía Client
<img src="https://i.imgur.com/Y3A24id.png">

Tiếp theo là các Url sẽ được truy cập sau khi xác thực và phân quyền truy cập thành công
<img src="https://i.imgur.com/EZaYMEQ.png">
Url có /admin chỉ có nhưng tài khoản có role là ROLE_ADMIN mới có quyền truy cập
Url có /user chỉ có nhưng tài khoản có role là ROLE_USER mới có quyền truy cập
Url / thì mọi tài khoản đăng nhập và xác thực thành công đều có thể truy cập và áp dụng cho mọi role

