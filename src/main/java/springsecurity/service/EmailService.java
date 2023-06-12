package springsecurity.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import springsecurity.model.Student;
import springsecurity.model.VerificationToken;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {
    private VertificationTokenService vertificationTokenService;
    private JavaMailSender javaMailSender;

    public EmailService(VertificationTokenService vertificationTokenService, JavaMailSender javaMailSender) {
        this.vertificationTokenService = vertificationTokenService;
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(Student student){
        //tìm kiếm vertificationToken qua student
        VerificationToken verificationToken = vertificationTokenService.findByStudent(student);
        //kiểm tra nếu student đã có token
        if (verificationToken != null){
            //lấy ra token của student
            String token = verificationToken.getToken();
            //gửi mail link xác thực
            try {
                MimeMessage message =  javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                String contextMail = "Thank you for signing up. Please click on the link to verify your email address"+
                        "http://localhost:8080/activation?token="+token;
                helper.setTo(student.getEmail());
                helper.setSubject("Email address vertification");
                helper.setText(contextMail);
                javaMailSender.send(message);
            }catch (MessagingException e){
                e.printStackTrace();
            }
        }
    }
























}
