package org.tyaa.training.current.server.controllers.rest;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.models.RoleModel;
import org.tyaa.training.current.server.models.UserModel;
import org.tyaa.training.current.server.services.interfaces.IAuthService;

import java.util.ArrayList;
import java.util.List;

/**
 * Контроллер аутентификации для регистрации, входа и выхода пользователей
 * */
@Tag(name = "Authentication", description = "Authentication, users, roles")
@RestController
@RequestMapping("/api/auth")
@SecurityRequirement(name = "jsessionid")
public class AuthController {
    private final IAuthService authService;

    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    /**
     * Получение списка всех ролей, которые могут иметь пользователи
     * */
    @Operation(summary = "Get all roles")
    @Secured("ROLE_ADMIN")
    @GetMapping("/admin/roles")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Roles successfully retrieved",
                    content = @Content(schema = @Schema(implementation = GetRolesResponseModel.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = Void.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden (administrator rights required)",
                    content = @Content(schema = @Schema(implementation = Void.class))
            )
    })
    public ResponseEntity<ResponseModel> getRoles () {
        return new ResponseEntity<>(authService.getRoles(), HttpStatus.OK);
    }

    @Operation(summary = "Create a new role")
    @Secured("ROLE_ADMIN")
    @PostMapping("/admin/roles")
    public ResponseEntity<ResponseModel> createRole (@RequestBody RoleModel roleModel) {
        ResponseModel responseModel = authService.createRole(roleModel);
        return new ResponseEntity<>(
                responseModel,
                (responseModel.getMessage().toLowerCase().contains("created"))
                        ? HttpStatus.CREATED
                        : (responseModel.getMessage().equals("This name is already taken")
                        ? HttpStatus.CONFLICT
                        : HttpStatus.BAD_GATEWAY)
        );
    }

    @Operation(summary = "Get all users")
    @Secured("ROLE_ADMIN")
    @GetMapping("/admin/users")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Roles successfully retrieved",
                    content = @Content(schema = @Schema(implementation = GetUsersResponseModel.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden (administrator rights required)",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "502",
                    description = "Bad Gateway",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<ResponseModel> getUsers() {
        ResponseModel responseModel = authService.getUsers();
        return new ResponseEntity<>(
                responseModel,
                (responseModel.getData() != null)
                        ? HttpStatus.OK
                        : HttpStatus.BAD_GATEWAY
        );
    }

    @Operation(summary = "Get users with specific role id")
    @Secured("ROLE_ADMIN")
    @GetMapping("/admin/roles/{id}/users")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Roles successfully retrieved",
                    content = @Content(schema = @Schema(implementation = GetUsersResponseModel.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden (administrator rights required)",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Role not found",
                    content = @Content(schema = @Schema())
            ),
            @ApiResponse(
                    responseCode = "502",
                    description = "Bad Gateway",
                    content = @Content(schema = @Schema())
            )
    })
    public ResponseEntity<ResponseModel> getUsersByRole(@PathVariable Long id) {
        ResponseModel responseModel =
                authService.getRoleUsers(id);
        return new ResponseEntity<>(
                responseModel,
                (responseModel.getData() != null)
                        ? HttpStatus.OK
                        : HttpStatus.NOT_FOUND
        );
    }

    @Operation(summary = "Create a new user")
    @PostMapping("/users")
    public ResponseEntity<ResponseModel> createUser(@Validated @RequestBody UserModel userModel) {
        ResponseModel responseModel =
                authService.createUser(userModel);
        return new ResponseEntity<>(
                responseModel,
                (responseModel.getMessage().toLowerCase().contains("created"))
                        ? HttpStatus.CREATED
                        : responseModel.getMessage().contains("This name is already taken")
                        ? HttpStatus.CONFLICT
                        : HttpStatus.BAD_GATEWAY
        );
    }

    @Operation(summary = "Delete user by id")
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<ResponseModel> deleteUser(@PathVariable Long id) {
        return new ResponseEntity<>(authService.deleteUser(id), HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Change user role")
    @Secured("ROLE_ADMIN")
    @PatchMapping(value = "/admin/users/{userId}/change-role/{newRoleId}")
    public ResponseEntity<ResponseModel> changeUserRole(@PathVariable Long userId, @PathVariable Long newRoleId) {
        return new ResponseEntity<>(authService.changeUserRole(userId, newRoleId), HttpStatus.OK);
    }

    @Operation(summary = "Check if the user is a guest or not")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User <username> Signed In",
                    content = @Content(schema = @Schema(implementation = CheckUserResponseModel.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User is a Guest",
                    content = @Content(schema = @Schema())
            )
    })
    @GetMapping(value = "/users/check")
    /** @param authentication объект стандартного типа с данными учетной записи
     * пользователя теущего http-сеанса, если ранее произошла успешная аутентификация,
     * получается внедрением зависимости через аргумент метода */
    public ResponseEntity<ResponseModel> checkUser(Authentication authentication) {
        ResponseModel responseModel = authService.check(authentication);
        return new ResponseEntity<>(
                responseModel,
                (responseModel.getData() != null)
                        ? HttpStatus.OK
                        : HttpStatus.UNAUTHORIZED
        );
    }

    @Hidden
    @GetMapping("/users/signedout")
    public ResponseEntity<ResponseModel> signedOut() {
        return new ResponseEntity<>(authService.onSignOut(), HttpStatus.OK);
    }

    @Hidden
    @GetMapping("/users/onerror")
    public ResponseEntity<ResponseModel> onError() {
        return new ResponseEntity<>(authService.onError(), HttpStatus.UNAUTHORIZED);
    }

    /**
     * Технический класс для автоматической документации,
     * задающий точный тип данных ответа сервера
     * на запрос всех ролей
     * */
    private class GetRolesResponseModel extends ResponseModel<List<RoleModel>> {}

    /**
     * Технический класс для автоматической документации,
     * задающий точный тип данных ответа сервера
     * на запрос состояния аутентификации клиента
     * */
    private class CheckUserResponseModel extends ResponseModel<UserModel> {}

    /**
     * Технический класс для автоматической документации,
     * задающий точный тип данных ответа сервера
     * на запрос списка пользователей
     * */
    private class GetUsersResponseModel extends ResponseModel<List<UserModel>> {}
}