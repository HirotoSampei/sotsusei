package jp.te4a.spring.boot.sotsusei.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	
	@GetMapping(path="login")
	String login() {
		return "login";
	}
}