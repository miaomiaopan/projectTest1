package springbootlearn.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import springbootlearn.entity.test1.People;
import springbootlearn.repository.test1.PeopleRepository;
import springbootlearn.service.PeopleService;

@Service
public class PeopleServiceImpl implements PeopleService {
	
	@Autowired
	PeopleRepository personRepositoy;

	@Transactional(rollbackFor=(IllegalArgumentException.class))
	public People savePersonWithRollBack(People entity) {
		People people = personRepositoy.save(entity);
		if(people.getName().equals("pmm")){
			throw new IllegalArgumentException("pmm已经存在，数据将回滚");
		}
		
		return people;
		
	}

	@Transactional(noRollbackFor=(IllegalArgumentException.class))
	public People savePersonWithoutRollBack(People entity) {
		People people = personRepositoy.save(entity);
		if(people.getName().equals("pmm")){
			throw new IllegalArgumentException("pmm已经存在，数据不会回滚");
		}
		
		return people;
	}

}
