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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.models.LessonModel;
import org.tyaa.training.current.server.services.interfaces.ILessonService;

import java.util.List;

/**
 * Контроллер обучающих уроков
 * */
@Tag(name = "Lessons", description = "Learning lessons")
@RestController
@RequestMapping("/api/lessons")
@SecurityRequirement(name = "jsessionid")
public class LessonController {

    private final ILessonService wordLessonService;

    public LessonController(ILessonService wordLessonService) {
        this.wordLessonService = wordLessonService;
    }

    @Operation(summary = "Get lesson list items for current language-level combination")
    @Secured({"ROLE_ADMIN", "ROLE_CUSTOMER"})
    @GetMapping("/list-items")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lessons",
                    content = @Content(schema = @Schema(implementation = LessonController.GetWordLessonListItemsResponseModel.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "No user",
                    content = @Content(schema = @Schema(implementation = ResponseModel.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Profile not found",
                    content = @Content(schema = @Schema(implementation = ResponseModel.class))
            )
    })
    public ResponseEntity<ResponseModel> getLessonListItems(Authentication authentication) {
        ResponseModel responseModel = wordLessonService.getLessonListItems(authentication);
        return new ResponseEntity<>(
                responseModel,
                switch (responseModel.getMessage()) {
                    case "Profile not found" -> HttpStatus.NOT_FOUND;
                    case "No user" -> HttpStatus.UNAUTHORIZED;
                    default -> (responseModel.getStatus().equals(ResponseModel.SUCCESS_STATUS))
                            ? HttpStatus.OK
                            :HttpStatus.INTERNAL_SERVER_ERROR;
                }
        );
    }

    /**
     * Технический класс для автоматической документации,
     * задающий точный тип данных ответа сервера
     * на запрос пунктов для списка уроков
     * */
    private class GetWordLessonListItemsResponseModel extends ResponseModel<List<LessonModel>> {}
}
