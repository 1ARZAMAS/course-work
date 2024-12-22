package com.example.demo.config;

import com.example.demo.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurityConfig(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF configuration (you can disable it as needed)
            .csrf().disable()

            // Updated method to configure authorization
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/index.html", "/resources/**", "/").permitAll()  // Permit access to index and public resources
                .requestMatchers("/login", "/register").not().fullyAuthenticated()  // Allow unauthenticated users to access login and register
                .anyRequest().authenticated()  // Require authentication for all other requests
            )
            .formLogin(form -> form
                .loginPage("/login")                  // Custom login page
                .defaultSuccessUrl("/index", true)    // Redirect to index after login
                .permitAll()                          // Allow anyone to access the login page
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/index")           // Redirect to index after logout
                .permitAll()                          // Allow anyone to access the logout
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}