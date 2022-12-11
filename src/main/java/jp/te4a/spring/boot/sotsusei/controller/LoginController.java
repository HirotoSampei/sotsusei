package jp.te4a.spring.boot.sotsusei.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jp.te4a.spring.boot.sotsusei.service.ImageService;

@Controller
public class LoginController {
	@Autowired
	ImageService imageService;
	
	@GetMapping(path="login")
	String login(Model model) {
		/*String base64Data = imageService.getlogoImage();
    	model.addAttribute("cat_logo_image","data:image/png;base64,"+base64Data);*/
		return "login";
	}
}