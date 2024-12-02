package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desabilita CSRF para simplificar (não recomendado em produção)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // Permite acesso a todos os endpoints sem autenticação
                )
                .httpBasic(Customizer.withDefaults()); // Adiciona suporte a autenticação HTTP básica

        return http.build();
    }
}
