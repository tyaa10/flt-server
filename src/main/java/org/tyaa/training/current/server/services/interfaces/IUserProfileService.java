package org.tyaa.training.current.server.services.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.security.core.Authentication;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.models.UserProfileModel;

/**
 * Интерфейс службы профилей пользователей
 * */
public interface IUserProfileService {
    /**
     * Получение списка всех профилей
     * */
    ResponseModel getProfiles();
    /**
     * Получение профиля
     * @param id идентификатор профиля, который нужно получить
     * */
    ResponseModel getProfile(Long id);
    /**
     * Получение профиля текущего пользователя
     *
     * */
    ResponseModel getCurrentUserProfile(Authentication authentication);
    /**
     * Создание профиля
     * @param authentication стандартные данные Spring Security о текущем пользователе, для которого создаётся профиль
     * @param profileModel начальные данные профиля пользователя
     * */
    ResponseModel createProfile(Authentication authentication, UserProfileModel profileModel);
    /**
     * Обновление данных профиля
     * @param id идентификатор профиля, данные которого нужно обновить
     * @param patch данные для обновления
     * */
    ResponseModel updateProfile(Long id, JsonPatch patch) throws JsonPatchException, JsonProcessingException;
    /**
    /**
     * Удаление профиля
     * */
    ResponseModel deleteLanguage(Long id);
}