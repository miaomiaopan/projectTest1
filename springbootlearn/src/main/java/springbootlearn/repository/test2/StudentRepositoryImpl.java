package springbootlearn.repository.test2;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import springbootlearn.entity.test2.Student;

public class StudentRepositoryImpl implements StudentRepositoryCustom {

	@Autowired
	@Qualifier("entityManagerSecondary")
	EntityManager entityManager;

	@Override
	public List<Student> getAllTest() {
		Query query = entityManager.createQuery("from Student");
		return (List<Student>) (query.getResultList());
	}

}
