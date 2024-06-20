package org.tyaa.training.current.server.services.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.tyaa.training.current.server.models.LanguageLevelModel;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.models.UserProfileModel;

/**
 * Интерфейс службы комбинаций "родной язык - изучаемый язык - уровень владения языком"
 * */
public interface ILanguageLevelService {
    /**
     * Получение списка всех комбинаций
     * */
    ResponseModel getLanguageLevels();
    /**
     * Получение комбинации
     * @param id идентификатор комбинации, которую нужно получить
     * */
    ResponseModel getLanguageLevel(Long id);
    /**
     * Создание комбинации
     * */
    ResponseModel createLanguageLevel(LanguageLevelModel languageLevelModel);
    /**
     * Обновление данных комбинации
     * @param id идентификатор комбинации, данные которой нужно обновить
     * @param patch данные для обновления
     * */
    ResponseModel updateLanguageLevel(Long id, JsonPatch patch) throws JsonPatchException, JsonProcessingException;
    /**
     * Удаление комбинации
     * */
    ResponseModel deleteLanguageLevel(Long id);
}