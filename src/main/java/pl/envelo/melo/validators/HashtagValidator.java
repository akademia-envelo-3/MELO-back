package pl.envelo.melo.validators;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.domain.event.dto.NewEventDto;
import pl.envelo.melo.domain.hashtag.HashtagDto;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Component
@AllArgsConstructor
public class HashtagValidator {

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


}
