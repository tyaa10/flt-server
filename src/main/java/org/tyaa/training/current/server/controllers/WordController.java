package org.tyaa.training.current.server.controllers;

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
import org.springframework.web.bind.annotation.*;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.models.WordModel;
import org.tyaa.training.current.server.services.interfaces.IWordService;

import java.util.List;

/**
 * Контроллер изучаемых слов
 * */
@Tag(name = "Words", description = "Learning lessons")
@RestController
@RequestMapping("/api/lessons/word-lessons")
@SecurityRequirement(name = "jsessionid")
public class WordController {

    private final IWordService wordService;

    public WordController(IWordService wordService) {
        this.wordService = wordService;
    }

    @Operation(summary = "Get word models for a specific word study lesson")
    @Secured({"ROLE_ADMIN", "ROLE_CUSTOMER"})
    @GetMapping("/{id}/words")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Words",
                    content = @Content(schema = @Schema(implementation = GetWordLessonWordsResponseModel.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ResponseModel.class))
            )
    })
    public ResponseEntity<ResponseModel> getWordLessonWords(@PathVariable("id") Long wordLessonId) {
        ResponseModel responseModel = wordService.getWordsByWordLessonId(wordLessonId);
        return new ResponseEntity<>(
                responseModel,
                ((responseModel.getStatus().equals(ResponseModel.SUCCESS_STATUS))
                            ? HttpStatus.OK
                            : HttpStatus.INTERNAL_SERVER_ERROR)
        );
    }

    @Operation(summary = "Delete word-study lesson words")
    @DeleteMapping(value = "/{id}/words")
    public ResponseEntity<ResponseModel> deleteWordLessonWords(@PathVariable("id") Long wordLessonId) {
        return new ResponseEntity<>(
                wordService.clearWordLessonWords(wordLessonId),
                HttpStatus.NO_CONTENT
        );
    }

    /**
     * Технический класс для автоматической документации,
     * задающий точный тип данных ответа сервера
     * на запрос моделей изучения слов из определённого урока
     * */
    private class GetWordLessonWordsResponseModel extends ResponseModel<List<WordModel>> {}
}
