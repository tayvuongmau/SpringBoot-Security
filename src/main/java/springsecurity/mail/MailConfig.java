package springsecurity.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    @Bean
    public JavaMailSender getJavaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        //host mặc định khi dùng gmail
        mailSender.setHost("smtp.gmail.com");
        //ssl: 465, tls: 587
        mailSender.setPort(587);
        mailSender.setUsername("tvm1302@gmail.com");//mail dùng để gửi
        //đây là mã an toàn khi cài đặt trong gmail cho phép ứng dụng khác truy cập vào
        mailSender.setPassword("julbwejdocafkyko");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        // Cấu hình các thuộc tính khác của JavaMailSenderImpl (nếu cần)
        mailSender.setProtocol("smtp");
        mailSender.setDefaultEncoding("UTF-8");
        return mailSender;
    }


}
