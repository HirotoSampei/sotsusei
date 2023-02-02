package jp.te4a.spring.boot.sotsusei;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CompApplication {
  public static void main(String[] args) {
    SpringApplication.run(CompApplication.class, args);
  }
}
