package pl.envelo.melo.domain.hashtag;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.envelo.melo.exceptions.ArithmeticBadRequestException;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class HashtagDto {
    @NotBlank
    @Size(max = HashtagConst.MAX_CONTENT_LENGTH, min = HashtagConst.MIN_CONTENT_LENGTH, message = HashtagConst.INVALID_CONTENT)
    private String content;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HashtagDto that = (HashtagDto) o;
        return content.equalsIgnoreCase(that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content.toLowerCase());
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if (content.length() < 2 || content.length() > 50){
            throw new ArithmeticBadRequestException(HashtagConst.INVALID_CONTENT);
        }
        this.content = content;
    }
}
