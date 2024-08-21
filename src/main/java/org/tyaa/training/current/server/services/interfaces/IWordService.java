package org.tyaa.training.current.server.services.interfaces;

import org.tyaa.training.current.server.models.ResponseModel;

/**
 * Интерфейс службы изучаемых слов
 * */
public interface IWordService {

    /**
     * Получить список моделей слов из указанного урока
     * @param wordLessonId идентификатор урока по изучению слов
     * */
    ResponseModel getWordsByWordLessonId(Long wordLessonId);

    /**
     * Очистить список моделей слов указанного урока
     * @param wordLessonId идентификатор урока по изучению слов
     * */
    ResponseModel clearWordLessonWords(Long wordLessonId);
}
