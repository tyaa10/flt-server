package org.tyaa.training.current.server.security;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.tyaa.training.current.server.entities.UserEntity;
import org.tyaa.training.current.server.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Реализация класса предоставления механизма аутентификации с использованием Hibernate
 * */
@Component
public class HibernateWebAuthProvider implements AuthenticationProvider, UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public HibernateWebAuthProvider(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity user = userRepository.findUserByName(userName).get();
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return AuthorityUtils.createAuthorityList(user.getRole().getName());
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getName();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }

    @Override
    @Transactional
    /** @param a данные, с которыми веб-клиент
     * пытается аутентифицироваться (войти в свою учетную запись в веб-приложении) */
    public Authentication authenticate(Authentication a) throws AuthenticationException {
        String name = a.getName();
        String password = a.getCredentials().toString();
        UserEntity user = null;
        // пытка найти в БД учетную запись по имени пользователя
        try {
            Optional<UserEntity> userEntityOptional = userRepository.findUserByName(name);
            if (userEntityOptional.isPresent()) {
                user = userEntityOptional.get();
            }
        } catch (Exception ex) {
            Logger.getLogger(HibernateWebAuthProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        // если учетная запись найдена - проверяем совпадение пароля
        if (user != null
                && user.getRole() != null
                // при помощи средства шифрования проверяем совпадение
                // 1. пароля, отправленного пользователем при попытке входа
                // (метод matches принимает его первым аргументом и шифрует
                // точно также, как был зашифрован пароль при регистрации пользователя),
                // 2. с паролем из учетной записи из БД, который был зашифрован
                // в процессе регистрации пользователя
                && (passwordEncoder.matches(password, user.getPassword()))
        ) {
            // создание объекта со списком данных авторизации
            // (то есть получения доступа к определенным ресурсам
            // веб-запроса ранее аутентифицированного пользователя)
            List<GrantedAuthority> authorities = new ArrayList<>();
            // добавление в список данных авторизации
            // имени роли пользователя
            authorities.add(new SimpleGrantedAuthority(user.getRole().getName()));
            // передача данных для проверки авторизаций в бин
            // авторизации на доступ к ресурсам на основе url-адресов
            return new UsernamePasswordAuthenticationToken(name, password, authorities);
        } else {
            // если учетная запись не найдена - возвращаем null -
            // признак ошибки аутентификации для системы безопасности
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(UsernamePasswordAuthenticationToken.class);
    }
}
