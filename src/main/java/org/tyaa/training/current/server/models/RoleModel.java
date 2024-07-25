package org.tyaa.training.current.server.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tyaa.training.current.server.models.interfaces.IModel;

/**
 * Модель роли пользователя
 * Имя класса выбирается произвольно
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleModel implements IModel {
    /**
     * Локально уникальный идентификатор
     * */
    public Long id;
    /**
     * Название роли
     * */
    public String name;
}
