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
    private final EventRepository eventRepository;
    private final MailTokenRepository mailTokenRepository;
    private final AddGuestToEventMapper addGuestToEventMapper;


    public ResponseEntity<?> insertOrDeleteGuest(int eventId, MailToken mailToken){
        if(!mailTokenRepository.existsById(mailToken.getToken())){
            return ResponseEntity.status(404).body("Token is not in database");
        }
        if(eventRepository.findById(eventId).isPresent()){
            Event event = eventRepository.findById(eventId).get();
            Person person = mailToken.getPerson();
            String email = person.getEmail();
            if(event.getMembers().contains(person)||event.getMembers().stream().anyMatch(person1 -> person1.getEmail().equals(email))){
                event.getMembers().remove(person);
                mailTokenRepository.delete(mailToken);
                if(event.getMembers().contains(person)||event.getMembers().stream().anyMatch(person1 -> person1.getEmail().equals(email))){
                    return ResponseEntity.status(400).body("Person is still on list");
                }
                if(mailTokenRepository.existsById(mailToken.getToken())){
                    return ResponseEntity.status(400).body("MailToken still exist");
                }
                return ResponseEntity.ok("Person removed successful");
            }
            event.getMembers().add(person);
            eventRepository.save(event);
            return ResponseEntity.ok(addGuestToEventMapper.toDto(person));
        }

        return ResponseEntity.status(404).body("Event does not exist");

    }
}
