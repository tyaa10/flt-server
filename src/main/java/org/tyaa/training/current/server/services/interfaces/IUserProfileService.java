package org.tyaa.training.current.server.services.interfaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.security.core.Authentication;
import org.tyaa.training.current.server.entities.UserProfileEntity;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.models.UserProfileModel;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

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
     * Удаление профиля
     * @param id идентификатор профиля, который нужно удалить
     * */
    ResponseModel deleteLanguage(Long id);
    /**
     * Выполнение действия в контексте профиля указанного пользователя
     * @param authentication стандартные данные Spring Security о текущем пользователе, для которого создаётся профиль
     * @param responseModel пустая модель ответа серверу для заполнения
     * @param action действие, которое нужно выполнить в контексте профиля пользователя
     * */
    ResponseModel doInProfileContext(Authentication authentication, ResponseModel responseModel, Function<UserProfileEntity, ResponseModel> action) throws Exception;
}