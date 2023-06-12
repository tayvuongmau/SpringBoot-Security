package springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import springsecurity.model.Student;
import springsecurity.model.VerificationToken;

@Repository
public interface VertificationTokenRepository extends JpaRepository<VerificationToken,Integer> {
    VerificationToken findByToken(String token);
    VerificationToken findByStudent(Student student);
}
