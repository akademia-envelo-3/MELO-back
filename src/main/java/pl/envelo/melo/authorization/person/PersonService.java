package pl.envelo.melo.authorization.person;

import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.person.dto.AddGuestToEventDto;
import pl.envelo.melo.domain.event.EventRepository;

@Service
public class PersonService {

    private PersonRepository personRepository;
    private EventRepository eventRepository;

    public PersonService(PersonRepository personRepository, EventRepository eventRepository){
        this.personRepository = personRepository;
        this.eventRepository = eventRepository;
    }

    public AddGuestToEventDto insertGuest(AddGuestToEventDto addGuestToEventDto){
        return null;
    }
}
