package org.tyaa.training.current.server.test.dataproviders;

import com.github.javafaker.Faker;
import org.tyaa.training.current.server.entities.UserEntity;
import org.tyaa.training.current.server.models.UserModel;

import java.util.List;

/**
 * Источник данных пользователей для тестов
 * */
public class UserProvider {

    private static final Faker faker = new Faker();

    public static UserEntity getUserEntity() {
        return UserEntity.builder()
                .id(faker.number().numberBetween(1L, 10L))
                .name(faker.name().firstName())
                .role(RoleProvider.getRoleEntity())
                .build();
    }

    public static List<UserEntity> getUserEntities() {
        return List.of(
                getUserEntity(),
                getUserEntity(),
                getUserEntity(),
                getUserEntity()
        );
    }

    public static UserModel getUserModel() {
        final UserEntity userEntity = getUserEntity();
        return UserModel.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .roleId(userEntity.getRole().getId())
                .roleName(userEntity.getRole().getName())
                .build();
    }

    public static List<UserModel> getUserModels() {
        return List.of(
                UserModel.builder().id(faker.number().numberBetween(1L, 10L)).name(faker.name().firstName()).build(),
                UserModel.builder().id(faker.number().numberBetween(1L, 10L)).name(faker.name().firstName()).build(),
                UserModel.builder().id(faker.number().numberBetween(1L, 10L)).name(faker.name().firstName()).build(),
                UserModel.builder().id(faker.number().numberBetween(1L, 10L)).name(faker.name().firstName()).build()
        );
    }
}
