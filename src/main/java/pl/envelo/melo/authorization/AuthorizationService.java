package pl.envelo.melo.authorization;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import pl.envelo.melo.authorization.admin.Admin;
import pl.envelo.melo.authorization.admin.AdminRepository;
import pl.envelo.melo.authorization.employee.Employee;
import pl.envelo.melo.authorization.employee.EmployeeRepository;
import pl.envelo.melo.authorization.person.Person;
import pl.envelo.melo.authorization.person.PersonRepository;
import pl.envelo.melo.authorization.user.User;
import pl.envelo.melo.authorization.user.UserRepository;
import pl.envelo.melo.exceptions.AppUserNotFoundException;

import java.security.Principal;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuthorizationService {
    final UserRepository userRepository;
    final EmployeeRepository employeeRepository;
    final AdminRepository adminRepository;
    final PersonRepository personRepository;
    @Value("${melo.employee-role}")
    private String employeeRole;
    @Value("${melo.admin-role}")
    private String adminRole;
    private static final int EMPLOYEE_CREATED = 1;
    private static final int ADMIN_CREATED = 3;
    private static final int ADMIN_AND_EMPLOYEE_CREATED = 4;

    public AuthStatus createUser(Principal principal) {
        if(Objects.isNull(principal))
            throw new NullPointerException();
        JwtAuthenticationToken tokenPrincipal = (JwtAuthenticationToken) principal;
        UUID principalId = UUID.fromString(tokenPrincipal.getTokenAttributes().get("sub").toString());
        if (userRepository.existsById(principalId)) {
            return AuthSucceded.USER_EXISTS;
        } else {
            return returnCode(createdEntities(tokenPrincipal, principalId));
        }
    }

    public UUID getUUID(Principal principal) {
        createUser(principal);
        return UUID.fromString((String) ((JwtAuthenticationToken) principal).getTokenAttributes().get("sub"));
    }

    public String getEmail(Principal principal) {
        createUser(principal);
        return (String) ((JwtAuthenticationToken) principal).getTokenAttributes().get("email");
    }

    private int createdEntities(JwtAuthenticationToken tokenPrincipal, UUID principalId) {
        Person person;
        try {
            person = new Person();
            person.setEmail(tokenPrincipal.getTokenAttributes().get("email").toString());
            person.setFirstName(tokenPrincipal.getTokenAttributes().get("given_name").toString());
            person.setLastName(tokenPrincipal.getTokenAttributes().get("family_name").toString());
            person = personRepository.save(person);
        } catch (NullPointerException e) {
            return -1;
        }
        Collection<GrantedAuthority> authorities = tokenPrincipal.getAuthorities();
        if (authorities.contains(new SimpleGrantedAuthority(employeeRole)) ||
                authorities.contains(new SimpleGrantedAuthority(adminRole))
        ) {
            User user = new User();
            user.setId(principalId);
            user.setPerson(person);
            user = userRepository.save(user);
            int createdEntities = 0;
            for (GrantedAuthority authority : authorities) {
                if (authority.getAuthority().equals(employeeRole)) {
                    Employee employee = new Employee();
                    employee.setUser(user);
                    employeeRepository.save(employee);
                    createdEntities += 1;
                }
                if (authority.getAuthority().equals(adminRole)) {
                    Admin admin = new Admin();
                    admin.setUser(user);
                    adminRepository.save(admin);
                    createdEntities += 3;
                }
            }
            return createdEntities;
        }
        return -1;
    }

    private AuthStatus returnCode(int createdEntities) {
        return switch (createdEntities) {
            case EMPLOYEE_CREATED -> AuthSucceded.EMPLOYEE_CREATED;
            case ADMIN_CREATED -> AuthSucceded.ADMIN_CREATED;
            case ADMIN_AND_EMPLOYEE_CREATED -> AuthSucceded.ADMIN_AND_EMPLOYEE_CREATED;
            default -> AuthFailed.PRINCIPAL_NOT_ALLOWED;
        };
    }

}
