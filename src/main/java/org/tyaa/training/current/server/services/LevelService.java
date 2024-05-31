package org.tyaa.training.current.server.services;

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
public class LevelService implements ILevelService {

    private final LevelRepository levelRepository;

    public LevelService(LevelRepository levelRepository) {
        this.levelRepository = levelRepository;
    }

    @Override
    public ResponseModel getLevels() {
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message("All the levels fetched successfully")
                .data(levelRepository.findAll().stream()
                        .map(levelEntity -> LevelModel.builder()
                                .id(levelEntity.getId())
                                .name(levelEntity.getName())
                                .build())
                        .toList())
                .build();
    }

    @Override
    public ResponseModel createLevel(LevelModel levelModel) {
        levelRepository.save(LevelEntity.builder().name(levelModel.getName()).build());
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("Level %s created", levelModel.name))
                .build();
    }

    @Override
    public ResponseModel updateLevel(LevelModel levelModel) {
        Optional<LevelEntity> levelEntityOptional = levelRepository.findById(levelModel.getId());
        if (levelEntityOptional.isPresent()) {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Level #%d not found", levelModel.getId()))
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("Level #%d updated", levelModel.getId()))
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
