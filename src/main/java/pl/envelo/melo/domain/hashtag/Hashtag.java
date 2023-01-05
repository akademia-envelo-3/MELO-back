package pl.envelo.melo.domain.hashtag;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Hashtag {
    @Id
    @GeneratedValue
    private int id;
    private String content;
    private long globalUsageCount;
    private boolean hidden;
}
