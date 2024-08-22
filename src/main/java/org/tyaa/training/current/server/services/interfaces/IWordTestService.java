package org.tyaa.training.current.server.services.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.security.core.Authentication;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.models.WordTestModel;

/**
 * Интерфейс службы проверки знания изучаемых слов
 * */
public interface IWordTestService {

    /**
     * Получение результатов проверки знаний слова пользователем
     * @param authentication стандартные данные Spring Security о текущем пользователе
     * @param wordId идентификатор данных для изучения слова
     * */
    ResponseModel getWordTestResults(Authentication authentication, Long wordId) throws Exception;

    /**
     * Получение результатов проверки знаний слова пользователем
     * @param authentication стандартные данные Spring Security о текущем пользователе
     * @param wordId
     * @param wordTestModel модель результатов проверки знания слова
     * */
    ResponseModel createTestResults(Authentication authentication, Long wordId, WordTestModel wordTestModel) throws Exception;

    /**
     * Обновить результаты проверки знаний слова
     * @param id идентификатор результатов проверки, которые нужно обновить
     * @param patch данные для обновления
     * */
    ResponseModel updateTestResults (Long id, JsonPatch patch) throws JsonPatchException, JsonProcessingException;
}
