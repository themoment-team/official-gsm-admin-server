package com.themoment.officialgsm.global.security;

import com.themoment.officialgsm.domain.auth.service.OAuthService;
import com.themoment.officialgsm.global.filter.JwtRequestFilter;
import com.themoment.officialgsm.global.security.handler.CustomAccessDeniedHandler;
import com.themoment.officialgsm.global.security.handler.CustomAuthenticationEntryPointHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final OAuthService oAuthService;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity)throws Exception{
        httpSecurity
                .csrf().disable();

        httpSecurity
                .cors().disable();

        httpSecurity
                .authorizeHttpRequests()
                        .requestMatchers("/auth/token/reissue").permitAll()
                        .requestMatchers("/auth/unapproved/list").hasAuthority("ADMIN")
                        .requestMatchers("/auth/approved/*").hasAuthority("ADMIN")
                        .anyRequest().authenticated();
        httpSecurity
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.NEVER);

        httpSecurity
                .exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .authenticationEntryPoint(new CustomAuthenticationEntryPointHandler());

        httpSecurity
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity
                .formLogin().disable()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(oAuthService);

        return httpSecurity.build();
    }
}
