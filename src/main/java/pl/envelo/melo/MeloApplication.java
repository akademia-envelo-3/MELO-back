package pl.envelo.melo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MeloApplication {
    public static void main(String[] args) {
        SpringApplication.run(MeloApplication.class, args);
    }
}
