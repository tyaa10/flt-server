package org.tyaa.training.current.server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.stereotype.Service;
import org.tyaa.training.current.server.entities.LanguageLevelEntity;
import org.tyaa.training.current.server.models.LanguageLevelModel;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.repositories.LanguageLevelRepository;
import org.tyaa.training.current.server.repositories.LanguageRepository;
import org.tyaa.training.current.server.repositories.LevelRepository;
import org.tyaa.training.current.server.services.interfaces.ILanguageLevelService;

import java.util.Optional;

/**
 * Реализация службы комбинаций "родной язык - изучаемый язык - уровень владения языком"
 * */
@Service
public class LanguageLevelService extends BaseService implements ILanguageLevelService {
    private final LanguageLevelRepository languageLevelRepository;
    private final LanguageRepository languageRepository;
    private final LevelRepository levelRepository;
    protected LanguageLevelService(
            ObjectMapper objectMapper,
            LanguageLevelRepository languageLevelRepository,
            LanguageRepository languageRepository,
            LevelRepository levelRepository) {
        super(objectMapper);
        this.languageLevelRepository = languageLevelRepository;
        this.languageRepository = languageRepository;
        this.levelRepository = levelRepository;
    }

    public static LanguageLevelModel entityToModel(LanguageLevelEntity languageLevelEntity) {
        return LanguageLevelModel.builder()
                .id(languageLevelEntity.getId())
                .nativeLanguage(LanguageService.entityToModel(languageLevelEntity.getNativeLanguage()))
                .learningLanguage(LanguageService.entityToModel(languageLevelEntity.getLearningLanguage()))
                .level(LevelService.entityToModel(languageLevelEntity.getLevel()))
                .active(languageLevelEntity.isActive())
                .build();
    }

    public static LanguageLevelEntity modelToEntity(LanguageLevelModel languageLevelModel) {
        return LanguageLevelEntity.builder()
                .id(languageLevelModel.getId())
                .nativeLanguage(LanguageService.modelToEntity(languageLevelModel.getNativeLanguage()))
                .learningLanguage(LanguageService.modelToEntity(languageLevelModel.getLearningLanguage()))
                .level(LevelService.modelToEntity(languageLevelModel.getLevel()))
                .active(languageLevelModel.getActive())
                .build();
    }

    @Override
    public ResponseModel getLanguageLevels() {
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message("All the levels fetched successfully")
                .data(languageLevelRepository.findAll().stream()
                        .map(LanguageLevelService::entityToModel)
                        .toList())
                .build();
    }

    @Override
    public ResponseModel getLanguageLevel(Long id) {
        Optional<LanguageLevelEntity> languageLevelEntityOptional = languageLevelRepository.findById(id);
        if (languageLevelEntityOptional.isPresent()) {
            LanguageLevelEntity languageLevelEntity = languageLevelEntityOptional.get();
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("Language-level pair #%d found", id))
                    .data(entityToModel(languageLevelEntity))
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Language-level pair #%d not found", id))
                    .build();
        }
    }

    @Override
    public ResponseModel createLanguageLevel(LanguageLevelModel languageLevelModel) {
        languageLevelRepository.save(modelToEntity(languageLevelModel));
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(
                        String.format(
                                "LanguageLevel 'From %s To %s %s' created",
                                languageLevelModel.getNativeLanguage().getName(),
                                languageLevelModel.getLearningLanguage().getName(),
                                languageLevelModel.getLevel().getName()
                        )
                ).build();
    }

    @Override
    public ResponseModel updateLanguageLevel(Long id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        Optional<LanguageLevelEntity> languageLevelEntityOptional = languageLevelRepository.findById(id);
        if (languageLevelEntityOptional.isPresent()) {
            LanguageLevelEntity languageLevelEntity = languageLevelEntityOptional.get();
            LanguageLevelModel patchedLevelModel = applyJsonPatch(patch, entityToModel(languageLevelEntity));
            languageLevelEntity = modelToEntity(patchedLevelModel);
            languageLevelRepository.save(languageLevelEntity);
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("LanguageLevel pair #%d updated", id))
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("LanguageLevel pair #%d not found", id))
                    .build();
        }
    }

    @Override
    public ResponseModel deleteLanguageLevel(Long id) {
        languageLevelRepository.deleteById(id);
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("LanguageLevel pair #%d deleted", id))
                .build();
    }
}
