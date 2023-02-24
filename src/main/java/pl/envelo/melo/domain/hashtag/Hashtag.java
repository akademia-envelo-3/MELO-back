package pl.envelo.melo.domain.hashtag;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "hashtags")
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Column(nullable = false, length = 50) // unique = true)
    private String content;
    @Column(nullable = false)
    private int globalUsageCount;
    @Column(nullable = false)
    private boolean hidden;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hashtag hashtag = (Hashtag) o;
        return content.equalsIgnoreCase(hashtag.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content.toLowerCase());
    }
}
