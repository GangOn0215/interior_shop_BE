package amo;

import amo.domain.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers("/css/**", "/js/**", "/images/**", "/fonts/**", "/resources/**").permitAll()
                            .requestMatchers("/", "/login", "/hello", "/posts/**").permitAll() // ✅ "/hello" 경로 허용 추가
                            .requestMatchers("/posts/**").hasRole(Role.USER.name())
                            .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())
                            .anyRequest().authenticated();
                })
                .formLogin(form -> form
                        .loginPage("/login") // ✅ 로그인 페이지 경로 설정
                        .loginProcessingUrl("/login") // ✅ 로그인 처리 URL 설정
                        .defaultSuccessUrl("/", true) // ✅ 로그인 성공 후 이동할 페이지
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // ✅ 로그아웃 URL 설정
                        .logoutSuccessUrl("/") // ✅ 로그아웃 성공 후 이동할 페이지
                        .permitAll()
                );

        return http.build();
    }
}
