package pl.envelo.melo.authorization.mailtoken;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.person.PersonRepository;
import pl.envelo.melo.domain.event.EventRepository;

@Service
@AllArgsConstructor
public class MailTokenService {
    private final MailTokenRepository mailTokenRepository;
    private final EventRepository eventRepository;
    private final PersonRepository personRepository;

    public MailToken generateToken(int eventId, int personId){
        MailToken token = new MailToken();
        token.setPerson(personRepository.findById(personId).get());
        token.setEvent(eventRepository.findById(eventId).get());
        mailTokenRepository.save(token);
        return token;
    }
}
