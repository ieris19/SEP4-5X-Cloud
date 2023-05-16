package dk.via.sep4.cloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"dk.via.sep4.cloud.web", "dk.via.sep4.cloud.lorawan", "dk.via.sep4.cloud.data"})
public class CloudApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudApplication.class, args);
    }
}
