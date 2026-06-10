package pl.klinika.Core;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Zastosuj do wszystkich endpointów w aplikacji
                .allowedOrigins("http://localhost:3000", "http://localhost:5173") // Pozwalamy na port 3000 (React) i ew. 5173 (Vite)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Pozwalamy na te metody HTTP
                .allowedHeaders("*") // Pozwalamy na wszystkie nagłówki
                .allowCredentials(true);
    }
}