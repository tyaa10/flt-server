package org.tyaa.training.current.server.test.dataproviders;

import com.github.javafaker.Faker;
import org.tyaa.training.current.server.entities.RoleEntity;
import org.tyaa.training.current.server.models.RoleModel;

import java.util.List;

/**
 * Источник данных ролей для тестов
 * */
public class RoleProvider {

    private static final Faker faker = new Faker();

    public static final List<String> AVAILABLE_ROLES =
            List.of("ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_CONTENT_MANAGER");

    public static RoleEntity getRandomAvailableRoleEntity() {
        return RoleEntity.builder()
                .id(faker.number().numberBetween(1L, 100L))
                .name(faker.options().option(AVAILABLE_ROLES).getFirst()).build();
    }

    public static RoleEntity getRoleEntity() {
        return RoleEntity.builder()
                .id(faker.number().numberBetween(1L, 100L))
                .name("ROLE_" + faker.internet()
                        .password(3, 8, false, false, false)
                        .toUpperCase()
                ).build();
    }

    public static List<RoleEntity> getAvailableRoleEntities() {
        return AVAILABLE_ROLES.stream()
                .map(roleName -> RoleEntity.builder()
                        .id(faker.number().numberBetween(1L, 100L))
                        .name(roleName).build()
                ).toList();
    }

    public static RoleModel getAvailableRoleModel() {
        final RoleEntity roleEntity = getRandomAvailableRoleEntity();
        return RoleModel.builder()
                .id(roleEntity.getId())
                .name(roleEntity.getName()).build();
    }

    public static RoleModel getRoleModel() {
        final RoleEntity roleEntity = getRoleEntity();
        return RoleModel.builder()
                .id(roleEntity.getId())
                .name(roleEntity.getName()).build();
    }

    public static List<RoleModel> getAvailableRoleModels() {
        return getAvailableRoleEntities().stream().map(
                roleEntity -> RoleModel.builder()
                        .id(roleEntity.getId())
                        .name(roleEntity.getName())
                        .build()
        ).toList();
    }
}
