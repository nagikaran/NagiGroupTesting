package com.NagiGroup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebMvc
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // Allow all API endpoints
                        .allowedOrigins("*") // Allow all domains (Replace "*" with frontend URL for security)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed request methods
                        .allowedHeaders("*") // Allow all headers
                        .exposedHeaders("Authorization") // Allow frontend to read Authorization header
                        .allowCredentials(false); // Set to true if using cookies or sessions
            }
        };
    }
}