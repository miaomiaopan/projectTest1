package springbootlearn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import springbootlearn.entity.test1.People;
import springbootlearn.repository.test1.PeopleRepository;

@SpringBootTest
public class test extends AbstractTestNGSpringContextTests {

    @Autowired
    private PeopleRepository service;
    
    @Test
    public void testHttpPost() throws Exception {
    	People people = new People();
    	people.setAge(13);
    	people.setName("测试数据");
    	service.save(people);
    }

    @Test
    public void testHttpGet() throws Exception {

    }

    @Test
    public void testHttpsPost() throws Exception {
    }

    @Test
    public void testHttpsGet() throws Exception {
    }

}