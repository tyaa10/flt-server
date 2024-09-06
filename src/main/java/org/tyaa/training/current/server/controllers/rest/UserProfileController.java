package org.tyaa.training.current.server.controllers.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.models.RoleModel;
import org.tyaa.training.current.server.models.UserProfileModel;
import org.tyaa.training.current.server.services.interfaces.IUserProfileService;

import java.util.List;

/**
 * Контроллер профилей пользователей
 * */
@Tag(name = "Profiles", description = "User profile")
@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "jsessionid")
public class UserProfileController {

    private final IUserProfileService profileService;

    public UserProfileController(IUserProfileService profileService) {
        this.profileService = profileService;
    }

    @Operation(summary = "Get all profiles")
    @Secured("ROLE_ADMIN")
    @GetMapping("/admin/profiles")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Profiles successfully retrieved",
                    content = @Content(schema = @Schema(implementation = GetProfilesResponseModel.class))
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
    public ResponseEntity<ResponseModel> getProfiles() {
        return new ResponseEntity<>(profileService.getProfiles(), HttpStatus.OK);
    }

    @Operation(summary = "Create a new profile for current user")
    @Secured({"ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_CONTENT_MANAGER"})
    @PostMapping("/profiles")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User profile created",
                    content = @Content(schema = @Schema(implementation = GetProfileResponseModel.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No user",
                    content = @Content(schema = @Schema(implementation = ResponseModel.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Profile already exists",
                    content = @Content(schema = @Schema(implementation = ResponseModel.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(schema = @Schema(implementation = ResponseModel.class))
            )
    })
    public ResponseEntity<ResponseModel> createProfile(Authentication authentication, @RequestBody UserProfileModel profileModel) {
        ResponseModel responseModel = profileService.createProfile(authentication, profileModel);
        return new ResponseEntity<>(
                responseModel,
                switch (responseModel.getMessage()) {
                    case "Profile already exists" -> HttpStatus.CONFLICT;
                    case "User not found" -> HttpStatus.NOT_FOUND;
                    case "No user" -> HttpStatus.UNAUTHORIZED;
                    case "User profile created" -> HttpStatus.CREATED;
                    default -> HttpStatus.INTERNAL_SERVER_ERROR;
                }
            );
    }

    @Operation(summary = "Get current user profile")
    @Secured({"ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_CONTENT_MANAGER"})
    @GetMapping("/profiles/current")
    public ResponseEntity<ResponseModel> getProfile(Authentication authentication) {
        ResponseModel responseModel = profileService.getCurrentUserProfile(authentication);
        return new ResponseEntity<>(
                responseModel,
                switch (responseModel.getMessage()) {
                    case "Profile not found" -> HttpStatus.NOT_FOUND;
                    case "No user" -> HttpStatus.UNAUTHORIZED;
                    case "Profile fetched" -> HttpStatus.OK;
                    default -> HttpStatus.INTERNAL_SERVER_ERROR;
                }
        );
    }

    /**
     * Технический класс для автоматической документации,
     * задающий точный тип данных ответа сервера
     * на запрос всех профилей
     * */
    private class GetProfilesResponseModel extends ResponseModel<List<UserProfileModel>> {}

    /**
     * Технический класс для автоматической документации,
     * задающий точный тип данных ответа сервера
     * на запросы, предполагающие получение одного объекта модели профиля
     * */
    private class GetProfileResponseModel extends ResponseModel<UserProfileModel> {}
}
