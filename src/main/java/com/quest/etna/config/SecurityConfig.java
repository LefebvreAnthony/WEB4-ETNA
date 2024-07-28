package com.quest.etna.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final JwtTokenUtil jwtTokenUtil;
        private final JwtUserDetailsService jwtUserDetailsService;

        public SecurityConfig(JwtTokenUtil jwtTokenUtil, JwtUserDetailsService jwtUserDetailsService) {
                this.jwtTokenUtil = jwtTokenUtil;
                this.jwtUserDetailsService = jwtUserDetailsService;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests((auth) -> auth
                                                .requestMatchers("/register", "/authenticate", "/").permitAll()
                                                .requestMatchers("/address/**").authenticated()
                                                .anyRequest().permitAll())
                                .addFilterBefore(new JwtRequestFilter(jwtUserDetailsService, jwtTokenUtil),
                                                UsernamePasswordAuthenticationFilter.class)
                                .exceptionHandling(exceptionHandling -> exceptionHandling
                                                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));
                return http.build();
        }
}
