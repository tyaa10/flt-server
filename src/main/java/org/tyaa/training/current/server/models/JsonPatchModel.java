package org.tyaa.training.current.server.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * Схема тела HTTP-запроса, соответствующего стандарту "JSON Patch"
 * */
public class JsonPatchModel {

    /**
     * Название действия (одно из стандартного набора {@link JsonPatchModel.Op})
     * */
    @NotBlank
    public Op op;

    public enum Op {
        replace, add, remove, copy, move, test
    }

    /**
     * Путь к изменяемому полю модели данных
     * */
    @NotBlank
    @Schema(example = "/name")
    public String path;

    /**
     * Новое значение для изменения значения поля
     * */
    @NotBlank
    public String value;
}
