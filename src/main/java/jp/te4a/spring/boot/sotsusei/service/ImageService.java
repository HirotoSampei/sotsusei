package jp.te4a.spring.boot.sotsusei.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class ImageService {
    public Model getlogoImage(Model model){
        File fileImg = new File("src/main/resources/images/cat_logo.png");
		try {
			byte[] byteImg = Files.readAllBytes(fileImg.toPath());
			String base64Data = Base64.getEncoder().encodeToString(byteImg);
            return model.addAttribute("cat_logo_image","data:image/png;base64,"+base64Data);
		}catch(IOException e) {
			return null;
		}
    }

	public Model geticonImage(Model model){
        File fileImg = new File("src/main/resources/images/cat_icon.png");
		try {
			byte[] byteImg = Files.readAllBytes(fileImg.toPath());
			String base64Data = Base64.getEncoder().encodeToString(byteImg);
            return model.addAttribute("cat_icon_image","data:image/png;base64,"+base64Data);
		}catch(IOException e) {
			return null;
		}
    }
}
