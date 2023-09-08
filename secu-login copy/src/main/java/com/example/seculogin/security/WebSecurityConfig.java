package com.example.seculogin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true
)
public class WebSecurityConfig implements WebMvcConfigurer{

    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private CustomFilter customFilter;
    @Autowired
    private AccessDeniedHandlerCustom accessDeniedHandlerCustom;
    @Autowired
    private AuthenticationEntryPointCustom authenticationEntryPointCustom;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        String [] PUBLIC = {
//                "/", "/login-handle",
//                "/admin/login","/admin/forgot-password","/admin/change-password/{token}",
//                "api/v2/**"
//        };
        http
                .csrf(c -> c.disable())
                .authorizeHttpRequests((authz) -> authz
//                        .requestMatchers(PUBLIC).permitAll()
//                        .requestMatchers("/admin/login","/admin/forgot-password",
//                        "/admin/change-password/{token}",  "api/v2/**").permitAll()
                        .anyRequest().permitAll())
                .logout((logout) -> logout
                        .logoutUrl("/api/v1/admin/logout")
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .permitAll())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(accessDeniedHandlerCustom)
                        .authenticationEntryPoint(authenticationEntryPointCustom)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }

//    public void configure(WebSecurity web) throws Exception {
//        web
//                .ignoring().requestMatchers(
//                "/css/**", "/assets/**", "/img/**", "/vendor/**", "/js/**", "/adminlte/**", "/media/static/**");
//    }
}

