package org.tyaa.training.current.server.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.models.RoleModel;
import org.tyaa.training.current.server.models.UserModel;
import org.tyaa.training.current.server.repositories.RoleRepository;
import org.tyaa.training.current.server.repositories.UserRepository;
import org.tyaa.training.current.server.services.interfaces.IAuthService;
import org.tyaa.training.current.server.entities.UserEntity;
import org.tyaa.training.current.server.entities.RoleEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
* Реализация службы аутентификации, использующая РБД-репозитории
* */
@Service
public class AuthService implements IAuthService {
    @Value("${custom.init-data.roles}")
    private List<String> roles;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ResponseModel getRoles() {
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message("All the roles fetched successfully")
                .data(roleRepository.findAll().stream()
                        .map(roleEntity -> RoleModel.builder()
                                .id(roleEntity.getId())
                                .name(roleEntity.getName())
                                .build())
                        .toList())
                .build();
    }

    @Override
    public ResponseModel createRole(RoleModel roleModel) {
        roleRepository.save(RoleEntity.builder().name(roleModel.name).build());
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("Role %s created", roleModel.name))
                .build();
    }

    @Override
    public ResponseModel createUser(UserModel userModel) {
        UserEntity user =
                UserEntity.builder()
                        .name(userModel.getName().trim())
                        .password(passwordEncoder.encode(userModel.getPassword()))
                        .role(roleRepository.findRoleByName(roles.get(IAuthService.ROLES.CUSTOMER.ordinal())))
                        .build();
        userRepository.save(user);
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("User %s created", user.getName()))
                .build();
    }

    @Override
    @Transactional
    public ResponseModel getRoleUsers(Long roleId) {
        Optional<RoleEntity> roleOptional = roleRepository.findById(roleId);
        if (roleOptional.isPresent()) {
            RoleEntity role = roleOptional.get();
            List<UserModel> userModels =
                    role.getUsers().stream().map(user ->
                                    UserModel.builder()
                                            .name(user.getName())
                                            .roleId(user.getRole().getId())
                                            .build()
                            )
                            .collect(Collectors.toList());
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("List of %s Role Users Retrieved Successfully", role.getName()))
                    .data(userModels)
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("No Users: Role #%d Not Found", roleId))
                    .build();
        }
    }

    @Override
    public ResponseModel deleteUser(Long id) {
        userRepository.deleteById(id);
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("User #%d Deleted", id))
                .build();
    }

    @Override
    public ResponseModel makeUserAdmin(Long id) {
        return changeUserRole(id, ROLES.ADMIN);
    }

    @Override
    public ResponseModel makeUserContentManager(Long id) {
        return changeUserRole(id, ROLES.CONTENT_MANAGER);
    }

    @Override
    public ResponseModel check(Authentication authentication) {
        ResponseModel response = new ResponseModel();
        // если пользователь из текущего http-сеанса аутентифицирован
        if (authentication != null && authentication.isAuthenticated()) {
            UserModel userModel = UserModel.builder()
                    .name(authentication.getName())
                    .roleName(
                            authentication.getAuthorities().stream()
                                    .findFirst()
                                    .get()
                                    .getAuthority()
                    )
                    .build();
            response.setStatus(ResponseModel.SUCCESS_STATUS);
            response.setMessage(String.format("User %s Signed In", userModel.name));
            response.setData(userModel);
        } else {
            response.setStatus(ResponseModel.SUCCESS_STATUS);
            response.setMessage("User is a Guest");
        }
        return response;
    }

    @Override
    public ResponseModel onSignOut() {
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message("Signed out")
                .build();
    }

    @Override
    public ResponseModel onError() {
        return ResponseModel.builder()
                .status(ResponseModel.FAIL_STATUS)
                .message("Auth error")
                .build();
    }

    private ResponseModel changeUserRole(Long userId, ROLES newRole) {
        // Получаем из БД объект сущности указанной роли
        RoleEntity role = roleRepository.findRoleByName(roles.get(newRole.ordinal()));
        // Получаем из БД объект сущности указанного пользователя
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        // Если пользователь найден
        if (userOptional.isPresent()){
            UserEntity user = userOptional.get();
            // установить пользователю указанную роль
            user.setRole(role);
            // сохранить изменение роли пользователя в БД
            userRepository.save(user);
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("Content manager %s created successfully", user.getName()))
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("User #%d Not Found", userId))
                    .build();
        }
    }
}
