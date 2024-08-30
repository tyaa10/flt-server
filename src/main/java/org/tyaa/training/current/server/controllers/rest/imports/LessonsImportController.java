package org.tyaa.training.current.server.controllers.rest.imports;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.models.imports.WordStudyImportModel;
import org.tyaa.training.current.server.services.interfaces.imports.ILessonsImportService;

import java.util.List;

/**
 * Контроллер аутентификации для регистрации, входа и выхода пользователей
 * */
@Tag(name = "Lessons Import", description = "Importing lessons data")
@RestController
@RequestMapping("/api/import/lessons")
public class LessonsImportController {

    private final ILessonsImportService lessonsImportService;

    public LessonsImportController(ILessonsImportService lessonsImportService) {
        this.lessonsImportService = lessonsImportService;
    }

    @Operation(summary = "Import word study lessons data")
    @PostMapping("/word-study")
    public ResponseEntity<ResponseModel> importWordStudyLessons(@Valid @RequestBody List<WordStudyImportModel> wordStudyImportModel) {
        ResponseModel responseModel = lessonsImportService.importWordLessons(wordStudyImportModel);
        return new ResponseEntity<>(
                responseModel,
                (responseModel.getMessage().toLowerCase().contains("imported"))
                        ? HttpStatus.CREATED
                        : responseModel.getMessage().contains("This name is already taken")
                        ? HttpStatus.CONFLICT
                        : HttpStatus.BAD_GATEWAY
        );
    }
}
