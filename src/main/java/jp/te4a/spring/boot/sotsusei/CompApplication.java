package jp.te4a.spring.boot.sotsusei;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@SpringBootApplication
public class CompApplication {
  public static void main(String[] args) {
    SpringApplication.run(CompApplication.class, args);
  }
}
