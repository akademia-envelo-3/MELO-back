package pl.envelo.melo.authorization.admin;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.authorization.AppUser;
import pl.envelo.melo.authorization.user.User;
import pl.envelo.melo.domain.request.CategoryRequest;

import javax.naming.Name;
import javax.naming.ldap.PagedResultsControl;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "admins")
public class Admin implements AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @OneToOne(optional = false)
    @NotNull
    private User user;
}
