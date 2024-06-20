package org.tyaa.training.current.server.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tyaa.training.current.server.models.LanguageLevelModel;
import org.tyaa.training.current.server.models.LanguageModel;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.services.interfaces.ILanguageLevelService;
import org.tyaa.training.current.server.services.interfaces.ILanguageService;

/**
 * Контроллер комбинаций "родной язык - изучаемый язык - уровень владения языком"
 * */
@Tag(name = "LanguageLevels", description = "Language Level Pairs")
@RestController
@RequestMapping("/api/language-levels")
@SecurityRequirement(name = "jsessionid")
public class LanguageLevelController {

    private final ILanguageLevelService languageLevelService;


    public LanguageLevelController(ILanguageLevelService languageLevelService) {
        this.languageLevelService = languageLevelService;
    }

    @Operation(summary = "Get all language level pairs")
    @GetMapping
    public ResponseEntity<ResponseModel> getLanguageLevels() {
        return new ResponseEntity<>(languageLevelService.getLanguageLevels(), HttpStatus.OK);
    }

    @Operation(summary = "Create a new language level pair")
    @PostMapping
    public ResponseEntity<ResponseModel> createLanguageLevel(@RequestBody LanguageLevelModel languageLevelModel) {
        ResponseModel responseModel = languageLevelService.createLanguageLevel(languageLevelModel);
        return new ResponseEntity<>(
                responseModel,
                (responseModel.getMessage().toLowerCase().contains("created"))
                        ? HttpStatus.CREATED
                        : (responseModel.getMessage().equals("This name is already taken")
                        ? HttpStatus.CONFLICT
                        : HttpStatus.BAD_GATEWAY)
        );
    }

    @Operation(summary = "Delete language level pair by id")
    @DeleteMapping(value = "{id}")
    public ResponseEntity<ResponseModel> deleteLanguageLevel(@PathVariable Long id) {
        return new ResponseEntity<>(languageLevelService.deleteLanguageLevel(id), HttpStatus.NO_CONTENT);
    }
}
