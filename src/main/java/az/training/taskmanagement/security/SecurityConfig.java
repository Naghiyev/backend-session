package az.training.taskmanagement.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JwtAuthFilter-i yalnız qorunan endpoint-lərə (/tasks, /users) tətbiq edir.
 * /auth/login açıqdır (token almaq üçün).
 */
@Configuration
public class SecurityConfig {

    @Bean
    public FilterRegistrationBean<JwtAuthFilter> jwtFilter(JwtService jwtService) {
        FilterRegistrationBean<JwtAuthFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new JwtAuthFilter(jwtService));
        registration.addUrlPatterns("/tasks", "/tasks/*", "/users", "/users/*");
        registration.setOrder(1);
        return registration;
    }
}
