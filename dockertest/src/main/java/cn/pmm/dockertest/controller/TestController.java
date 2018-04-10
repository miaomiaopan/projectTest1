package cn.pmm.dockertest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author panmiaomiao
 *
 * @date 2018年4月2日
 */
@RestController
@RequestMapping("/test")
public class TestController {

	@RequestMapping("/hello")
	public String hello() throws Exception {

		return "hello world!";
	}
}
