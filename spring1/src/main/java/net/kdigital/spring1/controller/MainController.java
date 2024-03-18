package net.kdigital.spring1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller  // Spring Container 관리하에 들어감 : 객체를 생성하고, 소멸하는 객체의 라이프사이클
public class MainController {
	
	@GetMapping({"", "/"})
	public String index() {
		System.out.println("index() 메소드에 도착함");
		
		return "index";  // src/main/resources/templates/index.html을 의미함
	}

}
