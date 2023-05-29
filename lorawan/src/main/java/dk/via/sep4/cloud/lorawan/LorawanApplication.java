package dk.via.sep4.cloud.lorawan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"dk.via.sep4.cloud.lorawan", "dk.via.sep4.cloud.data"})
public class LorawanApplication {
    public static void main(String[] args) {
        SpringApplication.run(LorawanApplication.class, args);
    }
}
