package jp.te4a.spring.boot.sotsusei.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class ImageService {

	public void getImage(Model model){
		File logofileImg = new File("src/main/resources/images/cat_logo.png");
		File iconfileImg = new File("src/main/resources/images/cat_icon.png");
		File popfileImg = new File("src/main/resources/images/people.png");
		try {
			byte[] logobyteImg = Files.readAllBytes(logofileImg.toPath());
			byte[] iconbyteImg = Files.readAllBytes(iconfileImg.toPath());
			byte[] popbyteImg = Files.readAllBytes(popfileImg.toPath());
			String logobase64Data = Base64.getEncoder().encodeToString(logobyteImg);
			String iconbase64Data = Base64.getEncoder().encodeToString(iconbyteImg);
			String popbase64Data = Base64.getEncoder().encodeToString(popbyteImg);
         	model.addAttribute("cat_logo_image","data:image/png;base64,"+logobase64Data);
			model.addAttribute("cat_icon_image","data:image/png;base64,"+iconbase64Data);
			model.addAttribute("people_image","data:image/png;base64,"+popbase64Data);
		}catch(IOException e) {
		}
	}
}
