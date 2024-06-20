package org.tyaa.training.current.server.services.interfaces;

import com.github.fge.jsonpatch.JsonPatch;
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
     * Создание профиля
     * */
    ResponseModel createProfile(UserProfileModel profileModel);
    /**
     * Обновление данных профиля
     * @param id идентификатор профиля, данные которого нужно обновить
     * @param patch данные для обновления
     * */
    ResponseModel updateProfile(Long id, JsonPatch patch);
    /**
    /**
     * Удаление профиля
     * */
    ResponseModel deleteLanguage(Long id);
}