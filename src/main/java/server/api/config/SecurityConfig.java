package server.api.config;

import server.api.domain.Role;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.security.reactive.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.PrintWriter;

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
                        .requestMatchers("/user/**").permitAll()
                        //.requestMatchers("/user/**").hasRole(Role.USER.name())
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
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(unauthorizedEntryPoint) // 401 처리
                        .accessDeniedHandler(accessDeniedHandler) // 403 처리
                );

        return http.build();
    }

    /**
     * ✅ Spring Security가 특정 리소스를 무시하도록 설정
     * 이 설정을 추가하면 정적 리소스 (CSS, JS, 이미지 등)에 대해 Spring Security가 적용되지 않음.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                String.valueOf(PathRequest.toStaticResources().atCommonLocations()), // ✅ 정적 리소스 자동 제외 (추천)
                "index.html", // ✅ 기본 페이지 허용
                "/css/**", "/js/**", "/images/**", "/fonts/**", "/resources/**" // ✅ 정적 리소스 허용
        );
    }

    // 401 Unauthorized 처리
    public final AuthenticationEntryPoint unauthorizedEntryPoint =
            (request, response, authException) -> {
                ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                        HttpStatus.UNAUTHORIZED,
                        "인증 정보가 없습니다."
                );

                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(new ObjectMapper().writeValueAsString(problemDetail));
                writer.flush();
            };

    // 403 Forbidden 처리
    public final AccessDeniedHandler accessDeniedHandler =
            (request, response, accessDeniedException) -> {
                ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                        HttpStatus.FORBIDDEN,
                        "권한이 없습니다."
                );

                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write(new ObjectMapper().writeValueAsString(problemDetail));
                writer.flush();
            };
}
