package org.tyaa.training.current.server.services.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.tyaa.training.current.server.models.LevelModel;
import org.tyaa.training.current.server.models.ResponseModel;

/**
 * Интерфейс службы уровней владения языком
 * */
public interface ILevelService {
    enum BASE_LEVELS {
        BEGINNER, ELEMENTARY, INTERMEDIATE, UPPER_INTERMEDIATE, ADVANCED, PROFICIENCY
    }
    /**
     * Получение списка всех уровней
     * */
    ResponseModel getLevels();
    /**
     * Создание уровня
     * */
    ResponseModel createLevel(LevelModel roleModel);
    /**
     * Обновление данных языка
     * @param id идентификатор уровня, данные которого нужно обновить
     * @param patch данные для обновления
     * */
    ResponseModel updateLevel(Long id, JsonPatch patch) throws JsonPatchException, JsonProcessingException;
    /**
     * Удаление уровня
     * */
    ResponseModel deleteLevel(Long id);
}
