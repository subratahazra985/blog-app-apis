package com.subro.blog.config;

import com.subro.blog.security.CustomUserDetailsService;
import com.subro.blog.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Builds a {@link SecurityFilterChain} that
     * <ol>
     *     <li>Disables CSRF protection</li>
     *     <li>Requires all requests to be authenticated</li>
     *     <li>Uses the {@link #authenticationProvider()} as the authentication provider</li>
     *     <li>Disables the form-based login</li>
     *     <li>Uses the default HTTP Basic configuration</li>
     * </ol>
     * @param http the {@link HttpSecurity} to configure
     * @return the built {@link SecurityFilterChain}
     * @throws Exception if the configuration fails
     */
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf->csrf.disable())
            .authorizeHttpRequests(auth->auth.requestMatchers("/api/auth/**").permitAll()
                    .anyRequest().authenticated())
            .authenticationProvider(authenticationProvider())
            .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//            http.formLogin(flc->flc.disable());
//            http.httpBasic(withDefaults());

    return http.build();
}

    /**
     * Returns an {@link AuthenticationProvider} that uses the {@link CustomUserDetailsService}
     * to retrieve the user and the {@link #passwordEncoder()} to encode the password.
     * @return the built {@link AuthenticationProvider}
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Provides a {@link PasswordEncoder} bean that uses BCrypt hashing algorithm for encoding passwords.
     * <p>
     * This bean is used to encode passwords securely using the BCrypt hashing algorithm.
     * The BCrypt algorithm is widely used for its resistance to brute-force attacks.
     *
     * @return a {@link PasswordEncoder} instance configured to use BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
