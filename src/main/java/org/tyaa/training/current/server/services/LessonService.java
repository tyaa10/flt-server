package org.tyaa.training.current.server.services;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.tyaa.training.current.server.entities.UserProfileEntity;
import org.tyaa.training.current.server.entities.WordLessonEntity;
import org.tyaa.training.current.server.entities.interfaces.ILessonEntity;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.models.LessonModel;
import org.tyaa.training.current.server.repositories.UserProfileRepository;
import org.tyaa.training.current.server.repositories.WordLessonRepository;
import org.tyaa.training.current.server.services.interfaces.ILessonService;

import java.util.List;
import java.util.Optional;

/**
 * Реализация службы уроков по изучению слов
 * */
@Service
public class LessonService implements ILessonService {

    private final WordLessonRepository wordLessonRepository;
    private final UserProfileRepository profileRepository;

    public LessonService(WordLessonRepository wordLessonRepository, UserProfileRepository profileRepository) {
        this.wordLessonRepository = wordLessonRepository;
        this.profileRepository = profileRepository;
    }

    public static LessonModel entityToModel(ILessonEntity wordLessonEntity) {
        return LessonModel.builder()
                .id(wordLessonEntity.getId())
                .name(wordLessonEntity.getName())
                .build();
    }

    @Override
    public ResponseModel getLessonListItems(Authentication authentication) {
        // создание пустого объекта модели ответа, содержимое которого будет определено ниже
        ResponseModel response = new ResponseModel();
        // если пользователь из текущего http-сеанса аутентифицирован
        if (authentication != null && authentication.isAuthenticated()) {
            // попытаться получить из БД профиль пользователя по его имени
            Optional<UserProfileEntity> profileEntityOptional =
                    profileRepository.findProfileByUserName(authentication.getName());
            // если существует профиль данного пользователя
            if (profileEntityOptional.isPresent()) {
                // из профиля текущего пользователя получить идентификатор комбинации языки-уровень
                final Long languageLevelId =
                    profileEntityOptional.get().getLanguageLevel().getId();
                // подготовить данные ответа со списком уроков,
                // соответствующих комбинации языки-уровень из профиля текущего пользователя
                response.setStatus(ResponseModel.SUCCESS_STATUS);
                List<LessonModel> lessonListItems =
                        wordLessonRepository.findByLanguageLevelId(languageLevelId).stream()
                                .map(LessonService::entityToModel)
                                .toList();
                response.setData(lessonListItems);
                response.setMessage(
                        lessonListItems.size() > 0
                            ? String.format("Lessons for language-level %d", languageLevelId)
                            : String.format("No lessons for language-level %d", languageLevelId)
                );
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
}
