package pl.envelo.melo.authorization.admin;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.authorization.user.User;

import javax.naming.ldap.PagedResultsControl;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToMany
    private Set<CategoryRequest> adminInbox;
    @OneToOne
    @NotBlank
    private User user;

}
