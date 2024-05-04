package org.tyaa.training.current.server.aspects;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.tyaa.training.current.server.models.ResponseModel;

import java.util.List;

/**
 * Набор служебных методов, формирующих объекты ответов по протоколу HTTP
 * в случае наличия ошибок валидации при выполнении методов REST-контроллеров
 * */
@ControllerAdvice
public class ControllerExceptionHandlerAdvice extends ResponseEntityExceptionHandler {
    // если валидатор обнаружил нарушения во входящих данных метода контроллера
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        // тексты всех ошибок, собранных валидатором, поместить в список
        List<String> errors =
                ex.getBindingResult().getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList();
        // вернуть HTTP-клиенту модель ответа со статусом и сообщением об ошибке
        // и со списком ошибок валидации
        return new ResponseEntity<>(
                ResponseModel.builder()
                        .status(ResponseModel.FAIL_STATUS)
                        .message("Invalid data")
                        .data(errors)
                        .build(),
                headers,
                status
        );
    }
}
