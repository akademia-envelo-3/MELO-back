package pl.envelo.melo.domain.hashtag;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class HashtagDto {
    @NotBlank
    @Size(max = 50, min = 2, message = "Wrong size")
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
}
