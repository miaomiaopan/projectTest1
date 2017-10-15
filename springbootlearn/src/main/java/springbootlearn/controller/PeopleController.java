package springbootlearn.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springbootlearn.entity.test1.People;
import springbootlearn.entity.test2.Student;
import springbootlearn.repository.test1.PeopleRepository;
import springbootlearn.repository.test2.StudentRepository;

@RestController
public class PeopleController {
	@Autowired
	PeopleRepository peopleRepository;
	
	@Autowired
	StudentRepository studentRepository;
	
	@RequestMapping("/rollback")
	public People rollback(People entity){
		return peopleRepository.save(entity);
	}
	
//	@RequestMapping("/norollback")
//	public Student norollback(Student entity){
//		return studentRepository.save(entity);
//	}
	
	@RequestMapping("/test1")
	public void test1(){
		List<People> people = peopleRepository.getAllTest();
		List<Student> student = studentRepository.getAllTest();
		
		for(People p : people){
			System.out.println(p.getName());
		}
		
		for(Student p : student){
			System.out.println(p.getName());
		}
		
	} 
	
}
