package sleuth.webmvc;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@EnableAutoConfiguration
@RestController
@CrossOrigin // So that javascript can be hosted elsewhere
@Import(HaystackConfig.class)
public class Frontend {

  @Autowired RestTemplate restTemplate;

  String backendBaseUrl = System.getProperty("spring.example.backendBaseUrl", "http://localhost:9000");

  static final AtomicInteger counter = new AtomicInteger();

  @RequestMapping("/") public String callBackend() {
    int count = counter.incrementAndGet();
    if (count % 5000 == 0){
      throw new IllegalArgumentException("Failing dude");
    }
    if (count % 1000 == 0){
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    return restTemplate.getForObject(backendBaseUrl + "/api", String.class);
  }

  @Bean RestTemplate restTemplate() {
    return new RestTemplate();
  }

  public static void main(String[] args) {
    SpringApplication.run(Frontend.class,
        "--spring.application.name=frontend",
        "--server.port=8081"
    );
  }
}
