package com.hung.noteapp.auth.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // REST API + JWT => tắt CSRF
                .csrf(csrf -> csrf.disable())

                // không dùng session
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // không hiện form login / basic login mặc định
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // mở các endpoint public
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v1/noteapp/auth/**",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}