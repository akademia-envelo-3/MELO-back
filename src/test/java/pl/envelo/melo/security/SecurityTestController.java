package pl.envelo.melo.security;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class SecurityTestController {
    @GetMapping("/test/security")
    public ResponseEntity<?> getRoles(Principal principal){
        if(principal == null){
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
        }
        return ResponseEntity.ok(((JwtAuthenticationToken)principal).getAuthorities());
    }
}
