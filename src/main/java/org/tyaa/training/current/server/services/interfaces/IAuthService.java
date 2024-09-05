package org.tyaa.training.current.server.services.interfaces;

import org.springframework.security.core.Authentication;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.models.RoleModel;
import org.tyaa.training.current.server.models.UserModel;

import java.util.List;

/**
 * Интерфейс службы аутентификации
 * */
public interface IAuthService {
    enum ROLES {
        ADMIN, CUSTOMER, CONTENT_MANAGER
    }
    ResponseModel getRoles();
    ResponseModel createRole(RoleModel roleModel);
    ResponseModel getUsers();
    ResponseModel getRoleUsers(Long roleId);
    ResponseModel createUser(UserModel userModel);
    ResponseModel deleteUser(Long id);
    ResponseModel changeUserRole(Long userId, Long newRoleId);
    /**
     * Получение подтверждения, что клиент сейчас аутентифицирован,
     * и возврат информации об учетной записи
     * @param authentication стандартные данные Spring Security о текущем пользователе
     * */
    ResponseModel check(Authentication authentication);
    ResponseModel onSignOut();
    ResponseModel onError();
}
