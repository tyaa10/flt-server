package org.tyaa.training.current.server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.tyaa.training.current.server.entities.WordEntity;
import org.tyaa.training.current.server.entities.WordTestEntity;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.models.WordTestModel;
import org.tyaa.training.current.server.repositories.WordRepository;
import org.tyaa.training.current.server.repositories.WordTestRepository;
import org.tyaa.training.current.server.services.interfaces.IUserProfileService;
import org.tyaa.training.current.server.services.interfaces.IWordTestService;
import org.tyaa.training.current.server.utils.TypeConverters;

import java.util.Optional;

/**
 * Реализация службы проверки знания изучаемых слов
 * */
@Service
public class WordTestService extends BaseService implements IWordTestService {

    private final WordTestRepository wordTestRepository;
    private final WordRepository wordRepository;
    private final IUserProfileService profileService;

    public WordTestService(
            ObjectMapper objectMapper,
            WordTestRepository wordTestRepository,
            WordRepository wordRepository,
            IUserProfileService profileService
    ) {
        super(objectMapper);
        this.wordTestRepository = wordTestRepository;
        this.wordRepository = wordRepository;
        this.profileService = profileService;
    }

    public static WordTestModel entityToModel(WordTestEntity wordTestEntity) {
        return WordTestModel.builder()
                .id(wordTestEntity.getId())
                .attemptsNumber(wordTestEntity.getAttemptsNumber())
                .successNumber(wordTestEntity.getSuccessNumber())
                .build();
    }

    public static WordTestEntity modelToEntity(WordTestModel wordTestModel) {
        return WordTestEntity.builder()
                .id(wordTestModel.getId())
                .attemptsNumber(wordTestModel.getAttemptsNumber())
                .successNumber(wordTestModel.getSuccessNumber())
                .build();
    }

    @Override
    public ResponseModel getWordTestResults(Authentication authentication, Long wordId) throws Exception {
        // создание пустого объекта модели ответа, содержимое которого будет определено ниже
        ResponseModel response = new ResponseModel();
        // получение результатов проверки знаний слова в контексте профиля текущего пользователя
        return profileService.doInProfileContext(authentication, response, profile -> {
            // попытка получить сущность с данными о результатах проверок знания слова
            Optional<WordTestEntity> wordTestEntityOptional =
                    wordTestRepository.findWordTestEntityByWordIdAndProfileId(wordId, profile.getId());
            // если сущность результатов проверок знаний получена
            if (wordTestEntityOptional.isPresent()) {
                // получить объект сущности из обёртки
                WordTestEntity wordTestEntity = wordTestEntityOptional.get();
                // скопировать данные из сущности в модель
                response.setStatus(ResponseModel.SUCCESS_STATUS);
                response.setMessage(String.format("Word test results for word #%d and profile #%d found", wordId, profile.getId()));
                response.setData(entityToModel(wordTestEntity));
            } else {
                // объект модели ответа сервера с сообщением о том, что данные результатов проверок знаний
                // данного слова для пользователя с данным профилем не найдены
                response.setStatus(ResponseModel.FAIL_STATUS);
                response.setMessage(String.format("Word test results for word #%d and profile #%d not found", wordId, profile.getId()));
            }
            return response;
        });
    }

    @Override
    public ResponseModel createTestResults(Authentication authentication, Long wordId, WordTestModel wordTestModel) throws Exception {
        // попытка получить данные результатов проверок знания слова с указанным идентификатором
        ResponseModel wordTestResultsResponse = getWordTestResults(authentication, wordId);
        // если данные получены - вернуть ответ с сообщением о попытке нарушения ограничения уникальности
        if (wordTestResultsResponse.getStatus().equals(ResponseModel.SUCCESS_STATUS)) {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message("Word knowledge test result already exists")
                    .build();
        } else if (!wordTestResultsResponse.getMessage().startsWith("Word test results")) {
            // иначе если данные не получены, и произошла ошибка -
            // вернуть ответ с сообщением об ошибке
            return wordTestResultsResponse;
        }
        // иначе - создать пустой объект модели ответа, содержимое которого будет определено ниже
        ResponseModel response = new ResponseModel();
        // создание результатов проверки знаний слова в контексте профиля текущего пользователя
        return profileService.doInProfileContext(authentication, response, profile -> {
            // частичное заполнение объекта сущности результатов полученными данными
            WordTestEntity wordTestEntity = modelToEntity(wordTestModel);
            // попытка получить объект сущности слова
            Optional<WordEntity> wordEntityOptional = wordRepository.findById(wordId);
            // если слово найдено
            if (wordEntityOptional.isPresent()) {
                // установка сущности слова в сущность результатов проверки знания слова
                wordTestEntity.setWord(wordEntityOptional.get());
                // установка сущности профиля в сущность результатов проверки знания слова
                wordTestEntity.setProfile(profile);
                // сохранение сущности результатов проверки знания слова
                wordTestRepository.save(wordTestEntity);
                // подготовка данных для тела положительного ответа
                response.setStatus(ResponseModel.SUCCESS_STATUS);
                response.setMessage("Word test results created");
            } else {
                // подготовить данные ответа о том, что профиль не найден
                response.setStatus(ResponseModel.FAIL_STATUS);
                response.setMessage("Word test results not created - Word not found");
            }
            return response;
        });
    }

    @Override
    public ResponseModel updateTestResults(Long id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        // поиск результатов проверки знания слова
        Optional<WordTestEntity> wordTestEntityOptional = wordTestRepository.findById(id);
        // если результаты найдены
        if (wordTestEntityOptional.isPresent()) {
            // извлечь сущность из обёртки
            WordTestEntity wordTestEntity = wordTestEntityOptional.get();
            // копировать все данные из сущности в модель и применить к ней поступившие изменения
            WordTestModel patchedWordTestModel = applyJsonPatch(patch, entityToModel(wordTestEntity));
            // копировать данные, которые могли измениться при получении обновления, из изменённой модели в сущность
            wordTestEntity.setAttemptsNumber(patchedWordTestModel.getAttemptsNumber());
            wordTestEntity.setSuccessNumber(patchedWordTestModel.getSuccessNumber());
            // сохранить изменённую сущность
            wordTestRepository.save(wordTestEntity);
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("Word test results #%d updated", id))
                    .build();
        } else {
            // если результаты найдены - вернуть объект модели данных для тела ответа с сообщением об этом
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Word test results #%d not found", id))
                    .build();
        }
    }

    @Override
    public ResponseModel addTestResult(Authentication authentication, Long wordId, Boolean success) throws Exception {
        // попытка получить данные результатов проверок знания слова с указанным идентификатором
        ResponseModel wordTestResultsResponse = getWordTestResults(authentication, wordId);
        // если данные не получены - попытаться создать впервые
        if (!wordTestResultsResponse.getStatus().equals(ResponseModel.SUCCESS_STATUS)) {
            ResponseModel wordTestResultsCreationResponse =
                    createTestResults(
                    authentication,
                    wordId,
                    WordTestModel.builder()
                            // начальное число попыток устанавливается равным единице
                            .attemptsNumber(1)
                            // начальное число успешных переводов равно нулю, если текущая попытка неуспешна, или единице - если успешна
                            .successNumber(TypeConverters.booleanToInteger(success))
                            .build()
            );
            // Если попытка создания удалась - установить в ответ сообщение об успешном добавлении результата.
            if (wordTestResultsCreationResponse.getStatus().equals(ResponseModel.SUCCESS_STATUS)) {
                wordTestResultsCreationResponse.setMessage("Word test result added");
            }
            // Иначе, если попытка создания не удалась - оставить неизменным полученный ответ с сообщением об ошибке.
            // В любом случае вернуть ответ с сообщением.
            return wordTestResultsCreationResponse;
        } else {
            // иначе - обновить данные результатов
            // создание пустого объекта модели ответа, содержимое которого будет определено ниже
            ResponseModel response = new ResponseModel();
            // получение результатов проверки знаний слова в контексте профиля текущего пользователя
            return profileService.doInProfileContext(authentication, response, profile -> {
                // получить объект сущности результатов проверок знания слова со старыми данными
                WordTestEntity wordTestEntity =
                        wordTestRepository.findById(((WordTestModel) wordTestResultsResponse.getData()).getId()).get();
                // обновить данные результатов в объекте сущности
                wordTestEntity.setAttemptsNumber(wordTestEntity.getAttemptsNumber() + 1);
                wordTestEntity.setSuccessNumber(wordTestEntity.getSuccessNumber() + TypeConverters.booleanToInteger(success));
                // сохранить объект сущности с обновлёнными данными
                wordTestRepository.save(wordTestEntity);
                response.setStatus(ResponseModel.SUCCESS_STATUS);
                response.setMessage("Word test result added");
                return response;
            });
        }
    }
}
