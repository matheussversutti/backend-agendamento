package com.agendamento_odontologico.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Permitir todas as origens
        config.addAllowedOrigin("http://localhost");
        config.addAllowedOrigin("http://localhost:80");

        // Permitir todos os métodos HTTP
        config.addAllowedMethod("*");

        // Permitir todos os headers
        config.addAllowedHeader("*");

        // Permitir credenciais
        config.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}