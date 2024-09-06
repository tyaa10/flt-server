package org.tyaa.training.current.server.controllers.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.tyaa.training.current.server.models.LevelModel;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.services.interfaces.ILevelService;

/**
 * Контроллер уровней владения языком
 * */
@Tag(name = "Levels", description = "Learning language levels")
@RestController
@RequestMapping("/api/levels")
@SecurityRequirement(name = "jsessionid")
public class LevelController {

    private final ILevelService levelService;


    public LevelController(ILevelService levelService) {
        this.levelService = levelService;
    }

    @Operation(summary = "Get all levels")
    @Secured({"ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_CONTENT_MANAGER"})
    @GetMapping
    public ResponseEntity<ResponseModel> getLevels() {
        return new ResponseEntity<>(levelService.getLevels(), HttpStatus.OK);
    }

    @Operation(summary = "Create a new level")
    @Secured({"ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_CONTENT_MANAGER"})
    @PostMapping
    public ResponseEntity<ResponseModel> createLevel(@RequestBody LevelModel levelModel) {
        ResponseModel responseModel = levelService.createLevel(levelModel);
        return new ResponseEntity<>(
                responseModel,
                (responseModel.getMessage().toLowerCase().contains("created"))
                        ? HttpStatus.CREATED
                        : (responseModel.getMessage().equals("This name is already taken")
                        ? HttpStatus.CONFLICT
                        : HttpStatus.BAD_GATEWAY)
        );
    }

    @Operation(summary = "Delete level by id")
    @Secured({"ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_CONTENT_MANAGER"})
    @DeleteMapping(value = "{id}")
    public ResponseEntity<ResponseModel> deleteLevel(@PathVariable Long id) {
        return new ResponseEntity<>(levelService.deleteLevel(id), HttpStatus.NO_CONTENT);
    }
}
