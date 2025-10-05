package com.learnmore.legacy.global.security;

import com.learnmore.legacy.global.security.jwt.filter.JwtAuthenticationFilter;
import com.learnmore.legacy.global.security.jwt.filter.JwtExceptionFilter;
import com.learnmore.legacy.global.security.jwt.handler.JwtAccessDeniedHandler;
import com.learnmore.legacy.global.security.jwt.handler.JwtAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtExceptionFilter jwtExceptionFilter;

    public SecurityConfig(
            JwtAccessDeniedHandler jwtAccessDeniedHandler,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAuthenticationFilter jwtAuthenticationFilter,
            JwtExceptionFilter jwtExceptionFilter
    ) {
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtExceptionFilter = jwtExceptionFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)

                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.accessDeniedHandler(jwtAccessDeniedHandler);
                    exceptionHandling.authenticationEntryPoint(jwtAuthenticationEntryPoint);
                })

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**", "/api-docs").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/sign-in", "/auth/sign-up", "/auth/refresh").permitAll()
                        //user
                        .requestMatchers(HttpMethod.GET,"/user/uploadUrl").permitAll()
                        .requestMatchers("/user/**").hasAnyRole( "USER","ADMIN")
                        //ruins
                        .requestMatchers(HttpMethod.POST, "/ruins/comment").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/ruins/**").permitAll()
                        //quiz
                        .requestMatchers(HttpMethod.POST,"/quiz/check").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/quiz/**").permitAll()
                        //mail
                        .requestMatchers("/mail/**").hasAnyRole("USER", "ADMIN")
                        //kakao-auth
                        .requestMatchers("/kakao/**").permitAll()
                        //apple-auth
                        .requestMatchers("/apple/**").permitAll()
                        //inventory
                        .requestMatchers("/inventory/**").hasAnyRole("USER", "ADMIN")
                        //course
                        .requestMatchers("/course/**").hasAnyRole("USER", "ADMIN")
                        //card
                        .requestMatchers("/card","/card/collection/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/card/**").permitAll()
                        //block
                        .requestMatchers("/block/**").hasAnyRole("USER", "ADMIN")
                        //alarm
                        .requestMatchers("/alarm/**").permitAll()
                        //achievement
                        .requestMatchers(HttpMethod.POST,"/achievement").permitAll()
                        .requestMatchers("/achievement/**").hasAnyRole("USER", "ADMIN")
                        //store
                        .requestMatchers("/store/**").hasAnyRole("USER", "ADMIN")
                        //ranking
                        .requestMatchers("/ranklist/**").permitAll()
                        // google-auth
                        .requestMatchers("/google/**").permitAll()
                        //any
                        .anyRequest().permitAll())

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080","http://localhost:9999","https://api.legacygame.site","https://www.legacygame.site","https://legacygame.site"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
