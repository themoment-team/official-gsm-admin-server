package com.themoment.officialgsm.global.security;

import com.themoment.officialgsm.domain.auth.service.OAuthService;
import com.themoment.officialgsm.global.exception.handler.ExceptionHandlerFilter;
import com.themoment.officialgsm.global.filter.JwtRequestFilter;
import com.themoment.officialgsm.global.security.handler.CustomAccessDeniedHandler;
import com.themoment.officialgsm.global.security.handler.CustomAuthenticationEntryPointHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    private final ExceptionHandlerFilter exceptionHandlerFilter;

    private final OAuthService oAuthService;

    private final AuthenticationFailureHandler authenticationFailureHandler;

    @Value("${redirect-base-url}")
    private String redirectBaseUrl;

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
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/health").permitAll()
                        .requestMatchers("/login/oauth2/**").permitAll()
                        .requestMatchers("/oauth2/**").permitAll()
                        .requestMatchers("/api/auth/token/refresh").permitAll()
                        .requestMatchers("/api/auth/logout").permitAll()
                        .requestMatchers("/api/auth/userinfo").authenticated()
                        .requestMatchers("/api/auth/username").authenticated()
                        .requestMatchers("/api/post/**").hasAuthority("ADMIN")
                        .requestMatchers("/api/auth/unapproved/list").hasAuthority("ADMIN")
                        .requestMatchers("/api/auth/approved/**").hasAuthority("ADMIN")
                        .anyRequest().permitAll();
        httpSecurity
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        httpSecurity
                .exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .authenticationEntryPoint(new CustomAuthenticationEntryPointHandler());

        httpSecurity
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter, JwtRequestFilter.class);

        httpSecurity
                .formLogin().disable()
                .httpBasic().disable()
                .headers().frameOptions().sameOrigin()
                .and()
                .oauth2Login()
                .failureHandler(authenticationFailureHandler)
                .defaultSuccessUrl(redirectBaseUrl)
                .userInfoEndpoint()
                .userService(oAuthService);

        return httpSecurity.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("")); // 추후 수정
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
