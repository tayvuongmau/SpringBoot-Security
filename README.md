**Đăng ký - Kích hoạt tài khoản bằng Gmail**

**1. Giới thiệu tổng quát**
Khi người dùng tiến hành đăng kí tài khoản để có thể truy cập vào các url chỉ được phép truy cập khi được cấp quyền (đăng kí tài khoản và đăng nhập thành công). Tài khoản người dùng mới khởi tạo sẽ mặc định có role là "ROLE_USER" và có trạng thái activated là false. Khi đăng kí thành công, phía server sẽ gửi tới email đăng kí của người dùng 1 đường link dùng để kích hoạt tài khoản (lúc đó trạng thái activated mới chuyển thành true). Link kích hoạt tài khoản sẽ có thời gian sử dụng trong 10 phút, sau 10 phút nếu người dùng không kích hoạt thì sẽ phải tạo mới lại tài khoản.
**2. Chuẩn bị**
Các thư viện cần có :
- validation-api
- hibernate-validator
- spring-boot-starter-mail
- mysql-connector-java (dùng database khác sẽ dùng thư viện khác)
- spring-boot-starter-data-jpa
- spring-boot-starter-web
Bảng tạo sẵn ở database (cũng có thể cấu hình trong configure khi chạy project sẽ tự động tạo bảng liên kết)

<img src="https://i.imgur.com/i1WRaih.png">

**3. Luồng hoạt động**

<img src="https://i.imgur.com/8NtJU8f.png">

**4. Xây dựng project**
Để có thể liên kết với database ta phải cấu hình phầnkeets nối tới database trong file cấu hình propertites

<img src="https://i.imgur.com/1eN47Hf.png">

Sau khi đã dựng được các bảng cần thiết dưới database, đầu tiên ta cần tạo các Entity liên kết với bảng trong database

<img src="https://i.imgur.com/mIhSHox.png">
<img src="https://i.imgur.com/o15mZ5P.png">

Tiếp theo ta sẽ tạo 1 Class DTO sẽ nhận dữ liệu từ form đăng kí khi người dùng tiếp hành đăng kí  tài khoản

<img src="https://i.imgur.com/ZSN33gM.png">

Tạo Class để cấu hình việc gửi mail và các thông số yêu cầu

<img src="https://i.imgur.com/InVemz1.png">

Ta tạo tiếp tới tầng Repository để thực hiện các truy vấn tới cơ sở dữ liệu

<img src="https://i.imgur.com/uW5L1FR.png">

Tại tầng Service nơi sẽ xử lí các logic :
Ở StudentService sẽ tiến hành các nghiệp vụ kiểm tra tồn tại và lưu tài khoản người dùng vừa tạo xuống database

<img src="https://i.imgur.com/3RPQXvg.png">
<img src="https://i.imgur.com/ItyIjab.png">

Ở VerificationTokenService sẽ tiến hành xử lí các thao tác như : kiếm tra tồn tại của dữ liệu VerificationToken khi biết token hoặc student, lưu mới VerificationToken khi đã có dữ liệu của student + token tương ứng, chuyển đổi thời giang dạng phút về kiểu Timestamp để đưa ra thời gian tồn tại của token

<img src="https://i.imgur.com/1fNaGLo.png">
<img src="https://i.imgur.com/j8BH3Bo.png">

Tại MailService sẽ tạo lên 1 form mail sẽ gửi đi như thế nào, gồm những phần gì

<img src="https://i.imgur.com/ZnntP1U.png">

Cuối cùng là xây dựng tầng Controller , nơi sẽ nhận request cùng dữ liệu đăng kí từ phía Client gửi lên, tại method /register sẽ tiến hành nhận dữ liệu gửi lên để valid và đăng kí tài khoản rồi lưu xuống database. Sau đó tầng service tương ứng bên dưới sẽ nhận thông tin đăng kí của người dùng để tiến hành kiểm tra và tạo ra token. Cuối cùng là tiến hành gửi mail chứa đường link kích hoạt tài khoản tới mail đăng kí của người dùng để người dùng nhấn vào link đó để kích hoạt tài khoản vừa đăng kí.

<img src="https://i.imgur.com/eR04I6j.png">

Tại method /activation sẽ xử lí thao tác kích hoạt tài khoản khi người dùng ấn vào link kích hoạt gửi trong mail

<img src="https://i.imgur.com/3kvpKBN.png">







**Xác thực– phân quyền với Spring Security + JWT**

**1. Giới thiệu**

**Spring Security** là một trong những phần quan trọng của Spring Framework, nó giúp chúng ta phân quyền và xác thực người dùng trước khi cho phép họ truy cập vào các tài nguyên phù hợp với vai trò của từng đối tượng tiến hành đăng nhập.
*Lưu ý : Nếu như ở phiên bản Spring Security 5 chúng ta cấu hình Security bằng cách extends class WebSecurityConfigAdapter, thì ở phiên bản mới nhất hiện tại class WebSecurityConfigAdapter đã không còn được Spring Boot hỗ trợ và bị xóa ra khỏi thư viện Spring Security 6.*
**JWT (JSON Web Token)** là một chuỗi mã hóa được gửi kèm trong Header của Client request có tác dụng giúp phía Server xác thực request người dùng có hợp lệ hay không. Mỗi khi người dùng tiến hành đăng nhập thành công sẽ được phía Server sinh ra một JWT trả lại phía Client sẽ được lưu ở session hoặc cookie trong một thời gian tuỳ thuộc phía Server quy định . JWT dưới dạng JSON bao gồm ba phần chính: Header, Payload và Signature.
- Header: Chứa thông tin về loại mã hóa và thuật toán được sử dụng để mã hóa JWT.
- Payload: Chứa các thông tin chứng thực (claims) như thông tin người dùng, quyền hạn, thông tin khác liên quan đến ứng dụng.
- Signature: Được tạo ra bằng cách ký (sign) Header và Payload sử dụng một khóa bí mật (secret key) hoặc một cặp khóa công khai, riêng tư.

**2. Chuẩn bị**
Các thư viện cần có:
- Spring Boot version 2.1.6.RELEASE
- Spring Security (tự cập nhật theo version của Spring Boot)
- Spring data Jpa
- Spring Web
- MySQL Driver ( dùng db khác sẽ tải driver tương ứng)
- io.jsonwebtoken.jwtt (lấy từ bên ngoài do trong spring boot không có, dùng để mã hoá thông tin thành  jwt)

**3. Luồng hoạt động**
<img src="https://i.imgur.com/uwceXgB.png">

**4. Xây dựng project**
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

