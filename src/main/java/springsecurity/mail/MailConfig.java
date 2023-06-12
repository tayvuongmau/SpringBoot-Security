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
        mailSender.setHost("smtp.gmail.com");//host mặc định khi dùng gmail
        mailSender.setPort(587);//ssl: 465, tls: 587
        mailSender.setUsername("tvm1302@gmail.com");//mail dùng để gửi
        mailSender.setPassword("julbwejdocafkyko");//đây là mã an toàn khi cài đặt trong gmail cho phép ứng dụng khác truy cập vào

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
