package org.tyaa.training.current.server.seeders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.tyaa.training.current.server.entities.RoleEntity;
import org.tyaa.training.current.server.repositories.RoleRepository;
import org.tyaa.training.current.server.seeders.interfaces.ISeeder;

import java.util.List;

@Component
@Order(value=1)
public class RoleSeeder implements ISeeder {

    private final RoleRepository roleRepository;

    @Value("${custom.init-data.roles}")
    private List<String> roles;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void seed() {
        /* Обеспечение наличия в БД ролей пользователей */
        for (String roleName : roles) {
            roleRepository.save(RoleEntity.builder().name(roleName).build());
        }
    }
}
