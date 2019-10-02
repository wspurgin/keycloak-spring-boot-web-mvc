package com.example.demo;

import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

/**
 * Contains the security configuration for the extension.
 * 
 */
@KeycloakConfiguration
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class SecurityConfiguration extends KeycloakWebSecurityConfigurerAdapter {

    /* (non-Javadoc)
     * @see org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter#
     * configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.headers()
            .and().csrf().disable()
            .anonymous().disable()
            .authorizeRequests()
            .anyRequest().authenticated();

    }
    
    /*
     * (non-Javadoc)
     *
     * @see org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter#
     * sessionAuthenticationStrategy()
     */
    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(new SessionRegistryImpl());
    }

    /**
     * Configures the global authentication provider.
     * 
     * @param builder
     *            Handles building of the {@code AuthenticationManager}.
     * @throws Exception
     *             When the provider cannot be built.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        KeycloakAuthenticationProvider provider = keycloakAuthenticationProvider();
        provider.setGrantedAuthoritiesMapper(new SimpleAuthorityMapper());
        builder.authenticationProvider(provider);
    }

    @Bean
    @Primary
    public ExampleKeycloakSpringBootConfigResolver keycloakConfigResolver(
            KeycloakSpringBootProperties properties) {
        return new ExampleKeycloakSpringBootConfigResolver(properties);
    }
    
}
