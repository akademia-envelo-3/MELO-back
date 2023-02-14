package pl.envelo.melo;

import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import pl.envelo.melo.domain.attachment.uploadmultiple.FilesStorageService;
import pl.envelo.melo.domain.event.EditEventNotificationHandler;

@SpringBootApplication
public class MeloApplication implements CommandLineRunner {

    @Resource
    FilesStorageService storageService;

    public static void main(String[] args) {
        SpringApplication.run(MeloApplication.class, args);
    }

    @Override
    public void run(String... arg) throws Exception {
    storageService.deleteAll();
        storageService.init();
    }
}
