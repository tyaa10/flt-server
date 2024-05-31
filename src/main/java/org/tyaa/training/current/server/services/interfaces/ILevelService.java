package org.tyaa.training.current.server.services.interfaces;

import org.tyaa.training.current.server.models.LevelModel;
import org.tyaa.training.current.server.models.ResponseModel;

/**
 * Интерфейс службы уровней владения языком
 * */
public interface ILevelService {
    /**
     * Получение списка всех уровней
     * */
    ResponseModel getLevels();
    /**
     * Создание уровня
     * */
    ResponseModel createLevel(LevelModel roleModel);
    /**
     * Изменение уровня (редактирование названия)
     * */
    ResponseModel updateLevel(LevelModel roleModel);
    /**
     * Удаление уровня
     * */
    ResponseModel deleteLevel(Long id);
}
