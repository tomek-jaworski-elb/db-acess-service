package com.jaworski.dbaccessservice.configuration.security;

import com.jaworski.dbaccessservice.resources.AppResources;
import com.jaworski.dbaccessservice.dto.UserRestService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class CustomRestBasicAuth {

    private final AppResources appResources;

    public CustomRestBasicAuth(AppResources appResources) {
        this.appResources = appResources;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(getAuthorizationManagerRequestMatcherRegistryCustomizer()
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    private static Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> getAuthorizationManagerRequestMatcherRegistryCustomizer() {
        return (authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers("/api/**").hasRole("USER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserRestService restServiceCredentials = appResources.getRestServiceCredentials();
        UserDetails user = User.builder()
                .passwordEncoder(s -> "{noop}" + restServiceCredentials.getPassword())
                .username(restServiceCredentials.getName())
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
