package springsecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import springsecurity.model.Student;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByName(String name);

    @Query(value = "select u from Student u where u.name = :name and u.password = :password")
    Student findStudentByNameAndPassword(@Param("name") String name, @Param("password") String password);
}
