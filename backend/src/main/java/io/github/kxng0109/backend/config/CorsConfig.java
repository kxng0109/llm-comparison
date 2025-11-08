package io.github.kxng0109.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The CorsConfig class is a configuration class that implements the WebMvcConfigurer interface
 * to customize Cross-Origin Resource Sharing (CORS) settings for the application.
 *
 * This configuration allows controlling how resources on the server are accessed from different origins,
 * ensuring secure and flexible cross-origin support.
 *
 * The allowed origins for CORS are configured via an externalized property `cors.allowed-origins`,
 * which is read using the @Value annotation. Multiple origins can be specified as a comma-separated string.
 *
 * This class configures CORS using the following settings:
 *
 * - Maps CORS settings to all endpoint paths using `/**`.
 * - Allows specific origins defined by the `cors.allowed-origins` property.
 * - Permits HTTP methods: GET, POST, PUT, DELETE, and OPTIONS.
 * - Permits all request headers by setting allowed headers to "*".
 * - Enables credentials sharing via `allowCredentials(true)`.
 * - Configures the maximum cache duration for CORS preflight responses to 3600 seconds (1 hour).
 *
 * The addCorsMappings(CorsRegistry registry) method defines the detailed CORS configuration
 * to apply globally across the application.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        allowedOrigins.split(",")
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
