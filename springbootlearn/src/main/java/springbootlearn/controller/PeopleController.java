package springbootlearn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PeopleController {
	@Autowired
	springbootlearn.repository.test1.PeopleRepository service1;
	
	@Autowired
	springbootlearn.repository.test2.StudentRepository service2;
	
	@RequestMapping("/rollback")
	public springbootlearn.entity.test1.People rollback(springbootlearn.entity.test1.People entity){
		return service1.save(entity);
	}
	
	@RequestMapping("/norollback")
	public springbootlearn.entity.test2.Student norollback(springbootlearn.entity.test2.Student entity){
		return service2.save(entity);
	}
}
