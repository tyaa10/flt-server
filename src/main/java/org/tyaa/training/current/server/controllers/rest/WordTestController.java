package org.tyaa.training.current.server.controllers.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import org.tyaa.training.current.server.models.JsonPatchModel;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.models.WordTestModel;
import org.tyaa.training.current.server.services.interfaces.IWordTestService;

/**
 * Контроллер проверки знания слов
 * */
@Tag(name = "Words tests", description = "Word knowledge tests")
@RestController
@RequestMapping("/api/tests/word-tests")
@SecurityRequirement(name = "jsessionid")
public class WordTestController {

    private final IWordTestService wordTestService;

    public WordTestController(IWordTestService wordTestService) {
        this.wordTestService = wordTestService;
    }

    @Operation(summary = "Get the user's knowledge test results for a word")
    @Secured({"ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_CONTENT_MANAGER"})
    @GetMapping("/words/{id}/results")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Word knowledge test results",
                    content = @Content(schema = @Schema(implementation = GetWordTestResultResponseModel.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Profile not found",
                    content = @Content(schema = @Schema(implementation = ResponseModel.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Word test results not found",
                    content = @Content(schema = @Schema(implementation = ResponseModel.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ResponseModel.class))
            )
    })
    public ResponseEntity<ResponseModel> getWordTestResults(Authentication authentication, @PathVariable("id") Long wordId) throws Exception {
        ResponseModel responseModel = wordTestService.getWordTestResults(authentication, wordId);
        return new ResponseEntity<>(
                responseModel,
                responseModel.getStatus().equals(ResponseModel.SUCCESS_STATUS)
                        ? HttpStatus.OK
                        : switch (responseModel.getMessage()) {
                            case "No user" -> HttpStatus.UNAUTHORIZED;
                            default -> responseModel.getMessage().endsWith("not found") ? HttpStatus.NOT_FOUND : HttpStatus.INTERNAL_SERVER_ERROR;
                        }
        );
    }

    @Operation(summary = "Get the user's knowledge test results for a word study lesson")
    @Secured({"ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_CONTENT_MANAGER"})
    @GetMapping("/lessons/{id}/results")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Word study lesson knowledge test results",
                    content = @Content(schema = @Schema(implementation = GetWordTestResultResponseModel.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Profile not found",
                    content = @Content(schema = @Schema(implementation = ResponseModel.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Word test results not found",
                    content = @Content(schema = @Schema(implementation = ResponseModel.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ResponseModel.class))
            )
    })
    public ResponseEntity<ResponseModel> getWordStudyLessonTestResults(Authentication authentication, @PathVariable("id") Long lessonId) throws Exception {
        ResponseModel responseModel = wordTestService.getWordStudyLessonTestResults(authentication, lessonId);
        return new ResponseEntity<>(
                responseModel,
                responseModel.getStatus().equals(ResponseModel.SUCCESS_STATUS)
                        ? HttpStatus.OK
                        : switch (responseModel.getMessage()) {
                    case "No user" -> HttpStatus.UNAUTHORIZED;
                    default -> responseModel.getMessage().endsWith("not found") ? HttpStatus.NOT_FOUND : HttpStatus.INTERNAL_SERVER_ERROR;
                }
        );
    }

    @Operation(summary = "Create a result record for the user's word knowledge test")
    @Secured({"ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_CONTENT_MANAGER"})
    @PostMapping("/words/{id}/results")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Word knowledge test result created",
                    content = @Content(schema = @Schema(implementation = ResponseModel.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Word knowledge test result already exists",
                    content = @Content(schema = @Schema(implementation = ResponseModel.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Profile not found",
                    content = @Content(schema = @Schema(implementation = ResponseModel.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Word test results not found",
                    content = @Content(schema = @Schema(implementation = ResponseModel.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ResponseModel.class))
            )
    })
    public ResponseEntity<ResponseModel> createWordTestResults(
            Authentication authentication,
            @PathVariable("id") Long wordId,
            @RequestBody WordTestModel wordTestModel) throws Exception {
        ResponseModel responseModel = wordTestService.createTestResults(authentication, wordId, wordTestModel);
        return new ResponseEntity<>(
                responseModel,
                responseModel.getStatus().equals(ResponseModel.SUCCESS_STATUS)
                        ? HttpStatus.CREATED
                        : switch (responseModel.getMessage()) {
                            case "No user" -> HttpStatus.UNAUTHORIZED;
                            case "Word knowledge test result already exists" -> HttpStatus.CONFLICT;
                            default -> responseModel.getMessage().endsWith("not found") ? HttpStatus.NOT_FOUND : HttpStatus.INTERNAL_SERVER_ERROR;
                        }
        );
    }

    @Operation(summary = "Update the results of a user's word knowledge test")
    @Secured({"ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_CONTENT_MANAGER"})
    // явное задание типа принимаемых данных - JSON со стандартной структурой обновления "JSON Patch"
    @PatchMapping(path = "/results/{id}", consumes = "application/json-patch+json")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Word knowledge test results updated",
                    content = @Content(schema = @Schema(implementation = GetWordTestResultResponseModel.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Word test results not found",
                    content = @Content(schema = @Schema(implementation = ResponseModel.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ResponseModel.class))
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(array = @ArraySchema(schema = @Schema(implementation = JsonPatchModel.class))))
    public ResponseEntity<ResponseModel> updateTestResults(
            @PathVariable("id") Long id,
            @RequestBody JsonPatch patch
    ) throws JsonPatchException, JsonProcessingException {
        ResponseModel responseModel = wordTestService.updateTestResults(id, patch);
        return new ResponseEntity<>(
                responseModel,
                responseModel.getStatus().equals(ResponseModel.SUCCESS_STATUS)
                        ? HttpStatus.OK
                        : responseModel.getMessage().endsWith("not found") ? HttpStatus.NOT_FOUND : HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @Operation(summary = "Add the result of the word knowledge test")
    @Secured({"ROLE_ADMIN", "ROLE_CUSTOMER", "ROLE_CONTENT_MANAGER"})
    @PostMapping("/words/{id}/results/add")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Word knowledge test result added"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Profile not found",
                    content = @Content(schema = @Schema(implementation = ResponseModel.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ResponseModel.class))
            )
    })
    public ResponseEntity<ResponseModel> addTestResult(Authentication authentication, @PathVariable("id") Long wordId, @RequestBody Boolean success) throws Exception {
        ResponseModel responseModel = wordTestService.addTestResult(authentication, wordId, success);
        return new ResponseEntity<>(
                responseModel,
                responseModel.getStatus().equals(ResponseModel.SUCCESS_STATUS)
                        ? HttpStatus.OK
                        : switch (responseModel.getMessage()) {
                    case "No user" -> HttpStatus.UNAUTHORIZED;
                    default -> responseModel.getMessage().endsWith("not found") ? HttpStatus.NOT_FOUND : HttpStatus.INTERNAL_SERVER_ERROR;
                }
        );
    }

    /**
     * Технический класс для автоматической документации,
     * задающий точный тип данных ответа сервера
     * на запрос результата проверки знания слова
     * */
    private class GetWordTestResultResponseModel extends ResponseModel<WordTestModel> {}
}
