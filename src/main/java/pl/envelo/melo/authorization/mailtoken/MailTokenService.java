package pl.envelo.melo.authorization.mailtoken;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.envelo.melo.domain.event.EventRepository;

@Service
@AllArgsConstructor
public class MailTokenService {
    private final MailTokenRepository mailTokenRepository;
    private final EventRepository eventRepository;

    public MailToken genereteToken(int eventId, int personId){
        return null;
    }
}
