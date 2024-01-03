package org.tyaa.training.current.server.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Модель роли пользователя
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RoleModel {
    /**
     * Локально уникальный идентификатор
     * */
    private Long id;
    /**
     * Название роли
     * */
    private String name;
}
