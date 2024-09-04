package org.tyaa.training.current.server.controllers.rest.imports;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.models.imports.WordStudyImportModel;
import org.tyaa.training.current.server.services.interfaces.imports.ILessonsImportService;
import org.tyaa.training.current.server.validators.annotations.ValidZipFile;

import java.io.IOException;
import java.io.InputStream;
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

    @Operation(summary = "Import word study lessons data from JSON")
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

    @Operation(summary = "Import word study lessons data from zip file with xlsx and media files inside")
    @PostMapping(value = "/word-study/zip", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ResponseModel> importWordStudyLessons(@ValidZipFile @RequestParam("file") MultipartFile file) throws IOException {
        ResponseModel responseModel;
        // Загрузить данные полученного файла архива в поток ввода,
        // который будет автоматически закрыт по окончании работы с ним
        try (InputStream inputStream = file.getInputStream()) {
            // Передать ссылку на поток ввода службе импорта данных и получить в ответ модель отклика
            responseModel = lessonsImportService.importWordLessons(inputStream);
        } catch (Exception ex) {
            System.out.println("\nController Ex: ");
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            System.out.println();
            responseModel =
                ResponseModel.builder()
                        .status(ResponseModel.FAIL_STATUS)
                        .message("Unknown internal server error")
                        .build();
        }
        return new ResponseEntity<>(
                responseModel,
                (responseModel.getMessage().toLowerCase().contains("imported"))
                        ? HttpStatus.CREATED
                        : responseModel.getMessage().contains("This name is already taken")
                            ? HttpStatus.CONFLICT
                            : responseModel.getMessage().equals("Unknown internal server error")
                                ? HttpStatus.INTERNAL_SERVER_ERROR
                                : HttpStatus.BAD_GATEWAY
        );
    }
}
