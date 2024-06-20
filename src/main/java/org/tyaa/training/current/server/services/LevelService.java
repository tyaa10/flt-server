package org.tyaa.training.current.server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.stereotype.Service;
import org.tyaa.training.current.server.entities.LevelEntity;
import org.tyaa.training.current.server.models.LevelModel;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.repositories.LevelRepository;
import org.tyaa.training.current.server.services.interfaces.ILevelService;

import java.util.Optional;

/**
 * Реализация службы уровней владения языком, использующая РБД-репозитории
 * */
@Service
public class LevelService extends BaseService implements ILevelService {

    private final LevelRepository levelRepository;

    public LevelService(ObjectMapper objectMapper, LevelRepository levelRepository) {
        super(objectMapper);
        this.levelRepository = levelRepository;
    }

    public static LevelModel entityToModel(LevelEntity levelEntity) {
        return LevelModel.builder()
                .id(levelEntity.getId())
                .name(levelEntity.getName())
                .build();
    }

    public static LevelEntity modelToEntity(LevelModel levelModel) {
        return LevelEntity.builder()
                .id(levelModel.getId())
                .name(levelModel.getName())
                .build();
    }

    @Override
    public ResponseModel getLevels() {
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message("All the levels fetched successfully")
                .data(levelRepository.findAll().stream()
                        .map(LevelService::entityToModel)
                        .toList())
                .build();
    }

    @Override
    public ResponseModel createLevel(LevelModel levelModel) {
        levelRepository.save(modelToEntity(levelModel));
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("Level %s created", levelModel.name))
                .build();
    }

    @Override
    public ResponseModel updateLevel(Long id, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        Optional<LevelEntity> levelEntityOptional = levelRepository.findById(id);
        if (levelEntityOptional.isPresent()) {
            LevelEntity levelEntity = levelEntityOptional.get();
            LevelModel patchedLevelModel = applyJsonPatch(patch, entityToModel(levelEntity));
            levelEntity.setName(patchedLevelModel.getName());
            levelRepository.save(levelEntity);
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("Level #%d updated", id))
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Level #%d not found", id))
                    .build();
        }
    }

    @Override
    public ResponseModel deleteLevel(Long id) {
        levelRepository.deleteById(id);
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("Level #%d deleted", id))
                .build();
    }
}
