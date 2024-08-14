package org.tyaa.training.current.server.services.interfaces;

import org.springframework.security.core.Authentication;
import org.tyaa.training.current.server.models.ResponseModel;

/**
 * Интерфейс службы уроков
 * */
public interface ILessonService {

    /**
     * Получить список моделей уроков,
     * доступных при текущей комбинации языков и уровня
     * @param authentication стандартные данные Spring Security о текущем пользователе
     * */
    ResponseModel getLessonListItems(Authentication authentication);
}
