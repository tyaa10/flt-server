package org.tyaa.training.current.server.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Главный конфигурационный класс системы безопасности
 * */
@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfig implements WebMvcConfigurer {

    private final HibernateWebAuthProvider authProvider;

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    private final SavedReqAwareAuthSuccessHandler savedReqAwareAuthSuccessHandler;

    public SecurityConfig(HibernateWebAuthProvider authProvider, RestAuthenticationEntryPoint restAuthenticationEntryPoint, SavedReqAwareAuthSuccessHandler savedReqAwareAuthSuccessHandler) {
        this.authProvider = authProvider;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.savedReqAwareAuthSuccessHandler = savedReqAwareAuthSuccessHandler;
    }

    /**
     * Включение механизма аутентификации с использованием Hibernate
     * */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider);
        return authenticationManagerBuilder.build();
    }

    /**
     * Обработчики, фильтрующие http-запросы до того, как они достигнут контроллеров
     * */
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // отключение проверки токена csrf в связи с тем,
        // что веб-запросы отправляются не из веб-страниц с формами
        return http.csrf(AbstractHttpConfigurer::disable)
                // Включение настроек обработки кросс-доменных http-запросов
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                        .authenticationEntryPoint(restAuthenticationEntryPoint))
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry
                        // запросы к WEB SPA админ-панели разрешены всем
                        .requestMatchers( "/", "/css/**", "/js/**").permitAll()
                        // запросы к конечным точкам авто-документации REST API любыми методами разрешены всем
                        .requestMatchers( "/v3/api-docs/**", "/swagger-ui/**").permitAll()
                        // запросы к стандартным конечным точкам SpringBoot обработки ошибок перед отправкой ответа клиенту разрешены всем любыми методами
                        .requestMatchers( "/error/**").permitAll()
                        // запросы к конечной точке "пользователи" методом GET разрешены всем
                        .requestMatchers(HttpMethod.GET, "/api/auth/users/**").permitAll()
                        // запросы к конечной точке "пользователи" методом POST разрешены всем
                        .requestMatchers(HttpMethod.POST, "/api/auth/users/**").permitAll()
                        // запросы к конечной точке "пользователи" методом DELETE разрешены только аутентифицированным
                        .requestMatchers(HttpMethod.DELETE, "/api/auth/users/**").authenticated()
                        // запросы к конечной точке "роли" любыми методами разрешены всем
                        .requestMatchers("/api/auth/roles").permitAll()
                        // запросы к конечной точке "роль" методом GET разрешены всем
                        .requestMatchers(HttpMethod.GET, "/api/auth/role/**").permitAll()
                        // запросы к конечной точке "профиль" любыми методами разрешены только аутентифицированным
                        .requestMatchers("/api/profiles/**").authenticated()
                        // запросы к конечной точке "языки" любыми методами разрешены только аутентифицированным
                        .requestMatchers("/api/languages/**").authenticated()
                        // запросы к конечной точке "уровни" любыми методами разрешены только аутентифицированным
                        .requestMatchers("/api/levels/**").authenticated()
                        // запросы к конечной точке "комбинаций 'родной язык - изучаемый язык - уровень владения языком'" любыми методами разрешены только аутентифицированным
                        .requestMatchers("/api/language-levels/**").authenticated()
                        // запросы к конечной точке "профили" любыми методами разрешены только аутентифицированным
                        .requestMatchers("/api/profiles/**").authenticated()
                        // запросы к конечной точке "уроки" любыми методами разрешены только аутентифицированным
                        .requestMatchers("/api/lessons/**").authenticated()
                        // запросы к конечной точке "проверки" любыми методами разрешены только аутентифицированным
                        .requestMatchers("/api/tests/**").authenticated()
                        // TODO запросы к конечной точке "импорт" любыми методами разрешены всем (временно, в дальнейшем - разрешить только контент-менеджерам)
                        .requestMatchers("api/import/**").permitAll()
                        // запросы к любым конечным точкам, когда в пути есть секция "admin", разрешены только администраторам
                        .requestMatchers("/api/*/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                )
                // настройки ответов после попытки авторизации
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer
                        .successHandler(savedReqAwareAuthSuccessHandler)
                        .failureUrl("/api/auth/users/onerror"))
                /* установка пользовательского URI, на который должен обратиться клиент для попытки входа в учетную запись
                 не производится, потому что в Spring Security предустановлен URI /login,
                 на который необходимо отправлять POST-запрос с телом в формате x-www-form-urlencoded,
                 держащим два строковых параметра - username и password */
                // настройки выхода из учетной записи
                .logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer
                        // умолчание: очищать объект аутентификации после выхода из учетной записи
                        .clearAuthentication(true)
                        // установка пользовательского URI, на который должен обратиться клиент
                        // для выхода из учетной записи
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        // адрес перенаправления на бэкенде в случае успешного выхода из учетной записи
                        .logoutSuccessUrl("/api/auth/users/signedout"))
                .build();
    }

    /** Настройки обработки кросс-доменных http-запросов */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // разрешённые адреса источников http-клиентов, например, фронтенд, загруженный в браузер с адреса http://localhost:3000
        corsConfiguration.setAllowedOriginPatterns(List.of("*")); // "*" - разрешены запросы от клиентов из любых источников
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(8600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
