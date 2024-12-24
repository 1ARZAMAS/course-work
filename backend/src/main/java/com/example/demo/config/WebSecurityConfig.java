package com.example.demo.config;

import com.example.demo.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/resources/", "/static/") // Исключите статические ресурсы
            )
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/index.html", "/resources/**", "/").permitAll()  // Permit access to index and public resources
                .requestMatchers("/login.html", "/register.html").not().fullyAuthenticated()  // Allow unauthenticated users to access login and register
                .anyRequest().authenticated()  // Require authentication for all other requests
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home_page", true)
                .permitAll()
                .successHandler((request, response, authentication) -> {
                    response.sendRedirect("/home_page"); // Перенаправление только после успешного входа
                })
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/index.html") // Перенаправление после выхода
                .permitAll()
            );

        return http.build();
    }
}