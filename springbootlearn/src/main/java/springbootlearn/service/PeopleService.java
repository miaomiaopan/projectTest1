package springbootlearn.service;

import springbootlearn.entity.test2.Student;

public interface PeopleService {
	public Student savePersonWithRollBack(Student people);

	public Student savePersonWithoutRollBack(Student people);
}
