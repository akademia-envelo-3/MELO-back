package pl.envelo.melo.security;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.envelo.melo.authorization.AuthorizationService;

import java.security.Principal;

@AllArgsConstructor
@RestController
public class SecurityTestController {
    private AuthorizationService authorizationService;
    @GetMapping("/test/security")
    public ResponseEntity<?> getRoles(Principal principal){
        if(principal == null){
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
        }
        return ResponseEntity.ok(((JwtAuthenticationToken)principal).getAuthorities());
    }
    @GetMapping("/test/security/auth")
    public ResponseEntity<?> createOrGetUser(Principal principal){
        if(principal == null){
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
        }
        return ResponseEntity.ok(authorizationService.inflateUser(principal));
    }
    @GetMapping("/test/security/mail")
    public ResponseEntity<?> getUserMail(Principal principal){
        if(principal == null){
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
        }
        return ResponseEntity.ok(authorizationService.getEmail(principal));
    }
    @GetMapping("/test/security/uuid")
    public ResponseEntity<?> getUserUUID(Principal principal){
        if(principal == null){
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
        }
        return ResponseEntity.ok(authorizationService.getUUID(principal));
    }
}
