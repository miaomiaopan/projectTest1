package springbootlearn.service;

import springbootlearn.entity.test1.People;

public interface PeopleService {
	public People savePersonWithRollBack(People people);

	public People savePersonWithoutRollBack(People people);
}
