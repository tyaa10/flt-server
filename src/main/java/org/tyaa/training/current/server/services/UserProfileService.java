package org.tyaa.training.current.server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.tyaa.training.current.server.entities.UserEntity;
import org.tyaa.training.current.server.entities.UserProfileEntity;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.models.UserProfileModel;
import org.tyaa.training.current.server.repositories.LanguageLevelRepository;
import org.tyaa.training.current.server.repositories.UserProfileRepository;
import org.tyaa.training.current.server.repositories.UserRepository;
import org.tyaa.training.current.server.services.interfaces.IModelConverter;
import org.tyaa.training.current.server.services.interfaces.IUserProfileService;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Реализация службы профилей пользователей, использующая РБД-репозитории
 * */
@Service
public class UserProfileService extends BaseService implements IUserProfileService, IModelConverter<UserProfileModel, UserProfileEntity> {

    private final UserProfileRepository profileRepository;
    private final LanguageLevelRepository languageLevelRepository;
    private final UserRepository userRepository;

    public UserProfileService(ObjectMapper objectMapper, UserProfileRepository profileRepository, LanguageLevelRepository languageLevelRepository, UserRepository userRepository) {
        super(objectMapper);
        this.profileRepository = profileRepository;
        this.languageLevelRepository = languageLevelRepository;
        this.userRepository = userRepository;
    }

    @Override
    public UserProfileModel entityToModel(UserProfileEntity profileEntity) {
        return UserProfileModel.builder()
                .id(profileEntity.getId())
                .name(profileEntity.getName())
                .avatar(profileEntity.getAvatar())
                .nativeLanguageName(profileEntity.getLanguageLevel().getNativeLanguage().getName())
                .learningLanguageName(profileEntity.getLanguageLevel().getLearningLanguage().getName())
                .levelName(profileEntity.getLanguageLevel().getLevel().getName())
                .build();
    }

    @Override
    public UserProfileEntity modelToEntity(UserProfileModel profileModel) {
        return UserProfileEntity.builder()
                .id(profileModel.getId())
                .name(profileModel.getName())
                .avatar(profileModel.getAvatar())
                .languageLevel(languageLevelRepository.findLanguageLevel(
                        profileModel.levelName,
                        profileModel.nativeLanguageName,
                        profileModel.learningLanguageName
                ))
                .build();
    }

    @Override
    public ResponseModel getProfiles() {
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message("All the profiles fetched successfully")
                .data(profileRepository.findAll().stream().map(this::entityToModel))
                .build();
    }

    @Override
    public ResponseModel getProfile(Long id) {
        Optional<UserProfileEntity> profileEntityOptional = profileRepository.findById(id);
        if (profileEntityOptional.isPresent()) {
            UserProfileEntity profileEntity = profileEntityOptional.get();
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("User profile #%d fetched successfully", id))
                    .data(entityToModel(profileEntity))
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("User profile #%d not found", id))
                    .build();
        }
    }

    @Override
    public ResponseModel getCurrentUserProfile(Authentication authentication) {
        ResponseModel response = new ResponseModel();
        // если пользователь из текущего http-сеанса аутентифицирован
        if (authentication != null && authentication.isAuthenticated()) {
            // попытаться получить из БД профиль пользователя по его имени
            Optional<UserProfileEntity> profileEntityOptional =
                    profileRepository.findProfileByUserName(authentication.getName());
            // если существует профиль данного пользователя
            if (profileEntityOptional.isPresent()) {
                // подготовить данные ответа о найденном профиле
                UserProfileModel profileModel = this.entityToModel(profileEntityOptional.get());
                response.setStatus(ResponseModel.SUCCESS_STATUS);
                response.setMessage("Profile fetched");
                response.setData(profileModel);
            } else {
                // подготовить данные ответа о том, что профиль не найден
                response.setStatus(ResponseModel.FAIL_STATUS);
                response.setMessage("Profile not found");
            }
        } else {
            // подготовить данные ответа о том, что нет текущего пользователя,
            // профиль которого можно было бы найти
            response.setStatus(ResponseModel.FAIL_STATUS);
            response.setMessage("No user");
        }
        return response;
    }

    @Override
    public ResponseModel createProfile(Authentication authentication, UserProfileModel profileModel) {
        // если пользователь из текущего http-сеанса аутентифицирован
        if (authentication != null && authentication.isAuthenticated()) {
            // попытка найти пользователя
            Optional<UserEntity> userEntityOptional = userRepository.findUserByName(authentication.getName());
            if (userEntityOptional.isPresent()) {
                // если у данного пользователя уже есть профиль,
                // вернуть объект ответа с отказом создавать новый
                if (profileRepository.findProfileByUserName(authentication.getName()).isPresent()) {
                    return ResponseModel.builder()
                            .status(ResponseModel.FAIL_STATUS)
                            .message("Profile already exists")
                            .build();
                }
                // иначе - частичное заполнение объекта сущности профиля полученными данными
                UserProfileEntity profileEntity = modelToEntity(profileModel);
                // установка текущего пользователя в сущность профиля
                UserEntity userEntity = userEntityOptional.get();
                userEntity.setProfile(profileEntity);
                // вставка записи профиля и добавление её идентификатора в запись пользователя
                userEntity = userRepository.save(userEntity);
                // установка идентификатора созданной записи профиля в объект модели профиля
                profileModel.setId(userEntity.getProfile().getId());
                // возврат объекта с сообщением об успешном создании профиля
                // и с данными профиля, дополненными идентификатором, который создала и вернула СУБД
                return ResponseModel.builder()
                        .status(ResponseModel.SUCCESS_STATUS)
                        .message("User profile created")
                        .data(profileModel)
                        .build();
            } else {
                return ResponseModel.builder()
                        .status(ResponseModel.FAIL_STATUS)
                        .message("User not found")
                        .build();
            }
        } else {
            // возврат ответа о том, что нет текущего пользователя,
            // для которого можно было бы создать профиль
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message("No user")
                    .build();
        }
    }

    @Override
    public ResponseModel updateProfile(Long id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        Optional<UserProfileEntity> userProfileEntityOptional = profileRepository.findById(id);
        if (userProfileEntityOptional.isPresent()) {
            UserProfileEntity profileEntity = userProfileEntityOptional.get();
            UserProfileModel patchedProfileModel = applyJsonPatch(patch, entityToModel(profileEntity));
            UserProfileEntity patchedProfileEntity = modelToEntity(patchedProfileModel);
            // пользователя - владельца профиля менять нельзя
            patchedProfileEntity.setUser(profileEntity.getUser());
            // текущий урок по изучению слов непосредственно менять нельзя
            patchedProfileEntity.setCurrentWordLesson(profileEntity.getCurrentWordLesson());
            profileRepository.save(modelToEntity(patchedProfileModel));
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("Profile #%d updated", id))
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Profile #%d not found", id))
                    .build();
        }
    }

    @Override
    public ResponseModel deleteLanguage(Long id) {
        profileRepository.deleteById(id);
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("Profile #%d deleted", id))
                .build();
    }

    @Override
    public ResponseModel doInProfileContext(
            Authentication authentication,
            ResponseModel responseModel,
            Function<UserProfileEntity, ResponseModel> action
    ) throws Exception {
        // если пользователь из текущего http-сеанса аутентифицирован
        if (authentication != null && authentication.isAuthenticated()) {
            // попытаться получить из БД профиль пользователя по его имени
            Optional<UserProfileEntity> profileEntityOptional =
                    profileRepository.findProfileByUserName(authentication.getName());
            // если существует профиль данного пользователя
            if (profileEntityOptional.isPresent()) {
                responseModel = action.apply(profileEntityOptional.get());
            } else {
                // подготовить данные ответа о том, что профиль не найден
                responseModel.setStatus(ResponseModel.FAIL_STATUS);
                responseModel.setMessage("Profile not found");
            }
        } else {
            // подготовить данные ответа о том, что нет текущего пользователя,
            // профиль которого можно было бы найти
            responseModel.setStatus(ResponseModel.FAIL_STATUS);
            responseModel.setMessage("No user");
        }
        return responseModel;
    }
}
