package org.tyaa.training.current.server.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tyaa.training.current.server.models.RoleModel;
import org.tyaa.training.current.server.services.interfaces.IAuthService;

import java.util.ArrayList;
import java.util.List;

/**
 * Контроллер аутентификации для регистрации, входа и выхода пользователей
 * */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }
    /**
     * Получение списка всех ролей, которые могут иметь пользователи
     * */
    @GetMapping("/admin/roles")
    public ResponseEntity<List<RoleModel>> getRoles () {
        return new ResponseEntity<>(authService.getRoles(), HttpStatus.OK);
    }
}
