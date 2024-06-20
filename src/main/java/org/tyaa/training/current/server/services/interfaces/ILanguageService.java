package org.tyaa.training.current.server.services.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.tyaa.training.current.server.models.LanguageModel;
import org.tyaa.training.current.server.models.ResponseModel;

/**
 * Интерфейс службы языков
 * */
public interface ILanguageService {
    enum BASE_LANGUAGES {
        ENGLISH,
        RUSSIAN
    }
    /**
     * Получение списка всех языков
     * */
    ResponseModel getLanguages();
    /**
     * Получение языка по его идентификатору
     * @param id идентификатор языка
     * */
    ResponseModel getLanguage(Long id);
    /**
     * Создание языка
     * */
    ResponseModel createLanguage(LanguageModel roleModel);
    /**
     * Обновление данных языка
     * @param id идентификатор языка, данные которого нужно обновить
     * @param patch данные для обновления
     * */
    ResponseModel updateLanguage(Long id, JsonPatch patch) throws JsonPatchException, JsonProcessingException;
    /**
     * Удаление языка
     * */
    ResponseModel deleteLanguage(Long id);
}
