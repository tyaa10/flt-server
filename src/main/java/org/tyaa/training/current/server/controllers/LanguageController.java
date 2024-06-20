package org.tyaa.training.current.server.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tyaa.training.current.server.models.LanguageModel;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.services.interfaces.ILanguageService;

/**
 * Контроллер языков
 * */
@Tag(name = "Languages", description = "Languages")
@RestController
@RequestMapping("/api/languages")
@SecurityRequirement(name = "jsessionid")
public class LanguageController {

    private final ILanguageService languageService;


    public LanguageController(ILanguageService languageService) {
        this.languageService = languageService;
    }

    @Operation(summary = "Get all languages")
    @GetMapping
    public ResponseEntity<ResponseModel> getLanguages() {
        return new ResponseEntity<>(languageService.getLanguages(), HttpStatus.OK);
    }

    @Operation(summary = "Create a new language")
    @PostMapping
    public ResponseEntity<ResponseModel> createLanguage(@RequestBody LanguageModel languageModel) {
        ResponseModel responseModel = languageService.createLanguage(languageModel);
        return new ResponseEntity<>(
                responseModel,
                (responseModel.getMessage().toLowerCase().contains("created"))
                        ? HttpStatus.CREATED
                        : (responseModel.getMessage().equals("This name is already taken")
                        ? HttpStatus.CONFLICT
                        : HttpStatus.BAD_GATEWAY)
        );
    }

    @Operation(summary = "Delete language by id")
    @DeleteMapping(value = "{id}")
    public ResponseEntity<ResponseModel> deleteLanguage(@PathVariable Long id) {
        return new ResponseEntity<>(languageService.deleteLanguage(id), HttpStatus.NO_CONTENT);
    }
}
