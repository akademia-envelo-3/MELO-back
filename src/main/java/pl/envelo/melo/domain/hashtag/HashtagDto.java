package pl.envelo.melo.domain.hashtag;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class HashtagDto {
    @NotBlank
    @Size(max = 50, min = 2, message = "Wrong size")
    private String content;
}
