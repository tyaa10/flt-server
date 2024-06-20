package org.tyaa.training.current.server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.stereotype.Service;
import org.tyaa.training.current.server.entities.LanguageEntity;
import org.tyaa.training.current.server.models.LanguageModel;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.repositories.LanguageRepository;
import org.tyaa.training.current.server.services.interfaces.ILanguageService;

import java.util.Optional;

/**
 * Реализация службы языков, использующая РБД-репозитории
 * */
@Service
public class LanguageService extends BaseService implements ILanguageService {

    private final LanguageRepository languageRepository;

    public LanguageService(ObjectMapper objectMapper, LanguageRepository languageRepository) {
        super(objectMapper);
        this.languageRepository = languageRepository;
    }

    public static LanguageModel entityToModel(LanguageEntity languageEntity) {
        return LanguageModel.builder()
                .id(languageEntity.getId())
                .name(languageEntity.getName())
                .build();
    }

    public static LanguageEntity modelToEntity(LanguageModel languageModel) {
        return LanguageEntity.builder()
                .id(languageModel.getId())
                .name(languageModel.getName())
                .build();
    }

    @Override
    public ResponseModel getLanguages() {
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message("Levels")
                .data(languageRepository.findAll().stream()
                        .map(LanguageService::entityToModel)
                        .toList())
                .build();
    }

    @Override
    public ResponseModel getLanguage(Long id) {
        Optional<LanguageEntity> levelEntityOptional = languageRepository.findById(id);
        if (levelEntityOptional.isPresent()) {
            LanguageEntity languageEntity = levelEntityOptional.get();
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("Language #%d found", id))
                    .data(entityToModel(languageEntity))
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Language #%d not found", id))
                    .build();
        }
    }

    @Override
    public ResponseModel createLanguage(LanguageModel languageModel) {
        languageRepository.save(modelToEntity(languageModel));
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("Language %s created", languageModel.name))
                .build();
    }

    @Override
    public ResponseModel updateLanguage(Long id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        Optional<LanguageEntity> levelEntityOptional = languageRepository.findById(id);
        if (levelEntityOptional.isPresent()) {
            LanguageEntity languageEntity = levelEntityOptional.get();
            LanguageModel patchedLanguageModel = applyJsonPatch(patch, entityToModel(languageEntity));
            languageEntity.setName(patchedLanguageModel.getName());
            languageRepository.save(languageEntity);
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("Language #%d updated", id))
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Language #%d not found", id))
                    .build();
        }
    }

    @Override
    public ResponseModel deleteLanguage(Long id) {
        languageRepository.deleteById(id);
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("Language #%d deleted", id))
                .build();
    }
}
