package org.tyaa.training.current.server.test.unit.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.tyaa.training.current.server.entities.RoleEntity;
import org.tyaa.training.current.server.entities.UserEntity;
import org.tyaa.training.current.server.entities.UserProfileEntity;
import org.tyaa.training.current.server.models.RoleModel;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.models.UserModel;
import org.tyaa.training.current.server.repositories.RoleRepository;
import org.tyaa.training.current.server.repositories.UserRepository;
import org.tyaa.training.current.server.services.AuthService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * Модульный тест службы AuthService
 * */
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    /**
     * Тестовые данные
     * */
    private final List<String> AVAILABLE_ROLES =
            List.of("ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_CONTENT_MANAGER");

    /*
    * Макеты зависимостей для внедрения в службу AuthService
    * */

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    /*
     * Экземпляр службы AuthService с внедрёнными в него макетами зависимостей
     * */

    @InjectMocks
    private AuthService authService;

    /*
    * Тестовые случаи
    * */

    @Test
    void getRolesTest() {
        // 1. Дано
        // 1.1 входящие данные
        final List<RoleEntity> roleEntities =
                AVAILABLE_ROLES.stream()
                        .map(roleName -> RoleEntity.builder().name(roleName).build())
                        .toList();
        // 1.2 поведение зависимостей
        when(roleRepository.findAll()).thenReturn(roleEntities);
        // 2. Вызов тестируемого метода
        ResponseModel responseModel = authService.getRoles();
        // 3. Проверки результатов выполнения тестируемого метода
        assertEquals(responseModel.getStatus(), ResponseModel.SUCCESS_STATUS);
        final List<RoleModel> roleModels = (List<RoleModel>) responseModel.getData();
        for (int i = 0; i < roleModels.size(); i++) {
            assertEquals(roleEntities.get(i).getName(), roleModels.get(i).getName());
        }
    }

    @Test
    void createRoleTest() {
        RoleModel roleModel = new RoleModel();
        roleModel.name = "test";
        when(roleRepository.save(any())).thenReturn(new RoleEntity());
        ResponseModel responseModel = authService.createRole(roleModel);
        assertEquals(responseModel.getStatus(), ResponseModel.SUCCESS_STATUS);
        assertNull(responseModel.getData());
    }

    @Test
    void createUserTest() {
        UserModel userModel = new UserModel();
        userModel.name = "test";
        userModel.setPassword("test");
        ReflectionTestUtils.setField(authService, "roles", AVAILABLE_ROLES);
        when(roleRepository.findRoleByName("ROLE_CUSTOMER")).thenReturn(new RoleEntity());
        when(passwordEncoder.encode(any())).thenReturn("test");
        when(userRepository.save(any())).thenReturn(new UserEntity());
        ResponseModel responseModel = authService.createUser(userModel);
        assertEquals(responseModel.getStatus(), ResponseModel.SUCCESS_STATUS);
    }

    @Test
    void getUsersTest() {
        when(userRepository.findAll())
                .thenReturn(
                        List.of(
                                UserEntity.builder()
                                        .id(1L)
                                        .name("test-user")
                                        .role(new RoleEntity())
                                        .profile(
                                                UserProfileEntity.builder()
                                                        .id(1L)
                                                        .name("test-profile")
                                                        .build()
                                        ).build()
                        )
                );
        ResponseModel responseModel = authService.getUsers();
        assertEquals(responseModel.getStatus(), ResponseModel.SUCCESS_STATUS);
        List<UserModel> users = (List<UserModel>) responseModel.getData();
        assertEquals(users.size(), 1);
        assertEquals(users.get(0).getClass(), UserModel.class);
        assertEquals(users.get(0).getId(), 1L);
        assertEquals(users.get(0).getName(), "test-user");
    }

    @Test
    void getRoleUsersTest() {
        when(roleRepository.findById(any())).thenReturn(Optional.of(new RoleEntity()));
        ResponseModel responseModel = authService.getRoleUsers(1L);
        assertEquals(responseModel.getStatus(), ResponseModel.SUCCESS_STATUS);
    }

    @Test
    void deleteUserTest() {
        ResponseModel responseModel = authService.deleteUser(1L);
        assertEquals(responseModel.getStatus(), ResponseModel.SUCCESS_STATUS);
    }

    @Test
    void changeUserRoleTest() {
        when(roleRepository.findById(any())).thenReturn(Optional.of(new RoleEntity()));
        when(userRepository.findById(any())).thenReturn(Optional.of(new UserEntity()));
        ResponseModel responseModel = authService.changeUserRole(1L, 1L);
        assertEquals(responseModel.getStatus(), ResponseModel.SUCCESS_STATUS);
    }

    @Test
    void checkTest() {
        when(authentication.isAuthenticated()).thenReturn(true);
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))).when(authentication).getAuthorities();
        ResponseModel responseModel = authService.check(authentication);
        assertEquals(responseModel.getStatus(), ResponseModel.SUCCESS_STATUS);
    }

    @Test
    void onSignOutTest() {
        ResponseModel responseModel = authService.onSignOut();
        assertEquals(responseModel.getStatus(), ResponseModel.SUCCESS_STATUS);
    }

    @Test
    void onErrorTest() {
        ResponseModel responseModel = authService.onError();
        assertEquals(responseModel.getStatus(), ResponseModel.FAIL_STATUS);
    }
}