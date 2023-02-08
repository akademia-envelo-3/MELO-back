package pl.envelo.melo.authorization.person;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.mailtoken.MailToken;
import pl.envelo.melo.authorization.mailtoken.MailTokenRepository;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.EventRepository;
import pl.envelo.melo.mappers.AddGuestToEventMapper;

@Service
@AllArgsConstructor
public class PersonService {

    private final PersonRepository personRepository;
}
