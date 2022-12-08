package jp.te4a.spring.boot.sotsusei.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ImageController {
	@GetMapping("/Login")
	public String getImage(Model model){
		File fileImg = new File("../images/cat_logo.png");
		try {
			byte[] byteImg = Files.readAllBytes(fileImg.toPath());
			String base64Data = Base64.getEncoder().encodeToString(byteImg);
			model.addAttribute("cat_logo_image","data:image/png;base64,"+base64Data);
		}catch(IOException e) {
			return null;
		}
		return "image";
	}
}