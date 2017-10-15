package springbootlearn.repository.test1;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import springbootlearn.entity.test1.People;

public class PeopleRepositoryImpl implements PeopleRepositoryCustom{
	
	@Autowired
	@Qualifier("entityManagerPrimary")
    EntityManager entityManager;
	
	@Override
	public List<People> getAllTest() {
		Query query = entityManager.createQuery("from People");
		return (List<People>)(query.getResultList());
	}

}
