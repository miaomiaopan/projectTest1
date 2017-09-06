package springbootlearn.repository.test2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import springbootlearn.entity.test2.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long>{

}
