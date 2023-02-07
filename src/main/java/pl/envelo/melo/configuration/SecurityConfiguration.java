package pl.envelo.melo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@EnableMethodSecurity
@Configuration
public class SecurityConfiguration {
    @Value("${melo.client-id}")
    private String appResource;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers(  "/swagger-ui/**").permitAll()
                .requestMatchers( "/api-docs/**").permitAll()
                .requestMatchers( new AntPathRequestMatcher("/h2-console/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher( "/participation/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/external/**")).permitAll()
                .and()
                .authorizeHttpRequests().anyRequest().fullyAuthenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .oauth2ResourceServer().jwt();
        http.csrf().disable();
        http.headers().frameOptions().disable();

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            try {
                Map<String, Map<?, ?>> resourceAccess = jwt.getClaim("resource_access");
                Map<?, ?> resource = resourceAccess.get(appResource);
                List<?> roles = (List<?>) resource.get("roles");
                roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.toString())));
            }catch (NullPointerException e){
                return authorities;
            }
            return authorities;
        });
        return jwtAuthenticationConverter;
    }
}
