package springbootlearn;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@SpringBootTest
public class Test2 extends AbstractTestNGSpringContextTests {

    @Test
    public void test1() throws Exception {
        System.out.println("run  test1");
    }

    @Test
    public void test2() throws Exception {
        System.out.println("run  test2");
    }

}