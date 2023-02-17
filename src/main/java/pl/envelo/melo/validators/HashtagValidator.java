package pl.envelo.melo.validators;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.dto.NewEventDto;
import pl.envelo.melo.domain.hashtag.Hashtag;
import pl.envelo.melo.domain.hashtag.HashtagDto;
import pl.envelo.melo.domain.hashtag.HashtagRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@Component
@AllArgsConstructor
public class HashtagValidator {

    private HashtagRepository hashtagRepository;

    public Map<String, String> validateHashtagFromForm(Set<HashtagDto> hashtags) {
        String regex = "^(?!#)[^\\s]{1,50}$";
        Pattern pattern = Pattern.compile(regex);
        Map<String, String> errors = new HashMap<>();
        for (HashtagDto hashtag : hashtags) {
            hashtag.setContent(hashtag.getContent().trim());
            if (!pattern.matcher(hashtag.getContent()).matches()){
                errors.put("Hashtag error :" + "'" + hashtag.getContent() + "'", "The hashtag does not match the pattern; " +
                        "From the form, hashtag must not be preceded by # and contain whitespace; " +
                        "In the event name and description, the hashtag can only be preceded by one #");
            }
        }
        return errors;
    }

    public Map<String, String> validateIsHidden(Set<HashtagDto> hashtags) {
        Optional<Hashtag> hashtagToCheckIsHidden = Optional.empty();
        Map<String, String> errors = new HashMap<>();
        for (HashtagDto hashtag : hashtags) {
            hashtagToCheckIsHidden = hashtagRepository.findByContentIgnoreCase(hashtag.getContent());
            if (hashtagToCheckIsHidden.isPresent() && hashtagToCheckIsHidden.get().isHidden()) {
                errors.put("Hastag error : " + hashtag.getContent(), "Hashtag is hidden, cant be added to event set");
            }
        }
        return errors;
    }
}
