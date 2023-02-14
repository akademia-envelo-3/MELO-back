package pl.envelo.melo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@EnableMethodSecurity
@Configuration("securityConfiguration")
public class SecurityConfiguration {
    @Value("${melo.admin-role}")
    private String adminRole;
    @Value("${melo.employee-role}")
    private String employeeRole;
    @Value("${melo.client-id}")
    private String appResource;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Value("${melo.api.version}")
    private String apiVersion;
    @Value("${melo.events.path}")
    private String eventsPath;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers(  "/swagger-ui/**").permitAll()
                .requestMatchers( "/api-docs/**").permitAll()
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                .requestMatchers(new AntPathRequestMatcher( "/"+apiVersion+eventsPath+"/participation/**")).permitAll()
                .requestMatchers(new RegexRequestMatcher("/"+apiVersion+eventsPath+"/\\d+/external", HttpMethod.POST.name())).permitAll()
                .and()
                .authorizeHttpRequests().anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .oauth2ResourceServer().jwt();
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
    public String getAdminRole() {
        return adminRole;
    }

    public String getEmployeeRole() {
        return employeeRole;
    }
}
