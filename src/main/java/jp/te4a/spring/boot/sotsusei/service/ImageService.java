package jp.te4a.spring.boot.sotsusei.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

import org.springframework.stereotype.Service;

@Service
public class ImageService {
    public String getlogoImage(){
        File fileImg = new File("src/main/resources/images/cat_logo.png");
		try {
			byte[] byteImg = Files.readAllBytes(fileImg.toPath());
			String base64Data = Base64.getEncoder().encodeToString(byteImg);
            return base64Data;
		}catch(IOException e) {
			return null;
		}
    }
}
