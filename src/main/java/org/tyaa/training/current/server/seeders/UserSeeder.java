package org.tyaa.training.current.server.seeders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.tyaa.training.current.server.entities.RoleEntity;
import org.tyaa.training.current.server.entities.UserEntity;
import org.tyaa.training.current.server.repositories.RoleRepository;
import org.tyaa.training.current.server.repositories.UserRepository;
import org.tyaa.training.current.server.seeders.interfaces.ISeeder;
import org.tyaa.training.current.server.services.interfaces.IAuthService;

import java.util.List;
import java.util.Map;

@Component
@Order(value=2)
public class UserSeeder implements ISeeder {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${custom.init-data.roles}")
    private List<String> roles;

    @Value("#{${custom.init-data.users}}")
    private Map<String, String> users;

    public UserSeeder(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void seed() {
        /* Обеспечение наличия в БД нескольких фейковых пользователей для отладки и тестирования приложения */
        RoleEntity adminRole = roleRepository.findRoleByName(roles.get(IAuthService.ROLES.ADMIN.ordinal()));
        RoleEntity userRole = roleRepository.findRoleByName(roles.get(IAuthService.ROLES.CUSTOMER.ordinal()));
        RoleEntity contentManagerRole = roleRepository.findRoleByName(roles.get(IAuthService.ROLES.CONTENT_MANAGER.ordinal()));
        users.forEach((userName, userPassword) -> userRepository.save(
                UserEntity.builder()
                        .name(userName)
                        .password(passwordEncoder.encode(userPassword))
                        .role(
                                userName.contains(String.valueOf(IAuthService.ROLES.ADMIN).toLowerCase())
                                        ? adminRole
                                        : userName.contains(String.valueOf(IAuthService.ROLES.CONTENT_MANAGER).toLowerCase())
                                            ? contentManagerRole
                                            : userRole
                        ).build()
        ));
    }
}
