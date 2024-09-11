package com.example.pract.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static com.example.pract.enums.Role.ADMIN;
import static com.example.pract.enums.Role.USER;
import static org.springframework.http.HttpMethod.*;

@Configuration
public class SecurityConfig {

    /** @noinspection SpringJavaInjectionPointsAutowiringInspection*/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                                .requestMatchers("/register", "/signup", "/auth", "/login", "/home", "/multiply", "/static/img/**", "/static/css/**", "/static/js/**", "/").permitAll()
                                .anyRequest().authenticated()
                )
                .csrf().disable()
                .formLogin(formLogin -> formLogin
                        .loginPage("/auth")
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .failureUrl("/auth?error=true")
                        .defaultSuccessUrl("/main", true))
                .logout(config -> config.logoutSuccessUrl("/"));

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}