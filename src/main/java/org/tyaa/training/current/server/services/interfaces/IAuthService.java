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
        ADMIN, CUSTOMER
    }
    ResponseModel getRoles();
    ResponseModel createRole(RoleModel roleModel);
    ResponseModel getRoleUsers(Long roleId);
    ResponseModel createUser(UserModel userModel);
    ResponseModel deleteUser(Long id);
    ResponseModel makeUserAdmin(Long id) throws Exception;
    /**
     * Получение подтверждения, что клиент сейчас аутентифицирован,
     * и возврат информации об учетной записи
     * @param authentication стандартные данные Spring Security о текущем пользователе
     * */
    ResponseModel check(Authentication authentication);
    ResponseModel onSignOut();
    ResponseModel onError();
}
