package pl.envelo.melo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.envelo.melo.domain.event.EditEventNotificationHandler;

@SpringBootApplication
public class MeloApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeloApplication.class, args);
    }
}
