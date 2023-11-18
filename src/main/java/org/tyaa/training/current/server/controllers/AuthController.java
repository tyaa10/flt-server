package org.tyaa.training.current.server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tyaa.training.current.server.models.RoleModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Контроллер аутентификации для регистрации, входа и выхода пользователей
 * */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    /**
     * Получение списка всех ролей, которые могут иметь пользователи
     * */
    @GetMapping("/admin/roles")
    public ResponseEntity<List<RoleModel>> getRoles () {
        RoleModel adminRoleModel = new RoleModel();
        adminRoleModel.id = 1;
        adminRoleModel.name = "admin";
        RoleModel customerRoleModel = new RoleModel();
        customerRoleModel.id = 2;
        customerRoleModel.name = "customer";
        List<RoleModel> roleModels = new ArrayList<>();
        roleModels.add(adminRoleModel);
        roleModels.add(customerRoleModel);
        return new ResponseEntity<>(roleModels, HttpStatus.OK);
    }
}
