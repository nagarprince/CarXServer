package com.LoginPage.login;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable()) // dev only; configure CSRF for production
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/register/**",
                                "/login",
                                "/error",
                                "/",
                                "/oauth2/**",
                                "/login/oauth2/**",
                                "/oauth2/authorization/**",
                                "/login/oauth2/code/**",
                                "/api/session"   // allow session check endpoint to be called without redirect
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                // For AJAX/fetch: return 401 instead of redirecting to login page
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .oauth2Login(oauth -> oauth
                        .defaultSuccessUrl("https://caar-x.vercel.app/home", true)
                        .failureUrl("https://caar-x.vercel.app/login?error")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("https://caar-x.vercel.app/login")
                );


        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // adjust origins if your frontend runs on different port
        config.setAllowedOrigins(List.of("http://localhost:5173", "http://localhost:5176", "https://caar-x.vercel.app"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
