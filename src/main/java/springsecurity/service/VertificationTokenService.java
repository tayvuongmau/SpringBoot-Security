package springsecurity.service;

import org.springframework.stereotype.Service;
import springsecurity.model.Student;
import springsecurity.model.VerificationToken;
import springsecurity.repository.VertificationTokenRepository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Calendar;

@Service
public class VertificationTokenService {
    private final VertificationTokenRepository vertificationTokenRepository;

    public VertificationTokenService(VertificationTokenRepository vertificationTokenRepository) {
        this.vertificationTokenRepository = vertificationTokenRepository;
    }

    //tim kiem VerificationToken thong qua token
    public VerificationToken findByToken(String token){
        return vertificationTokenRepository.findByToken(token);
    }

    //tim kiem VerificationToken theo Student
    public VerificationToken findByStudent(Student student){
        return vertificationTokenRepository.findByStudent(student);
    }

    //tạo mới VerificationToken khi có tài khoản mới được tạo cùng mã token kích hoạt tương ứng
    @Transactional
    public void save(Student student, String token){
        VerificationToken verificationToken = new VerificationToken(token,student);
        //set thời gian token có hiệu lực trong 24h kể từ lúc tạo mới tài khoản
        verificationToken.setExpiryDate(calculateExpiryDate(10));
        vertificationTokenRepository.save(verificationToken);
    }

    //tính toán thời gian token có hiệu lực theo thời gian là phút
    private Timestamp calculateExpiryDate(int expiryTimeMinutes){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, expiryTimeMinutes);
        return new Timestamp(cal.getTime().getTime());
    }



















}
