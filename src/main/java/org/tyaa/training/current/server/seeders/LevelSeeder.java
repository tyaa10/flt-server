package org.tyaa.training.current.server.seeders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.tyaa.training.current.server.entities.LevelEntity;
import org.tyaa.training.current.server.repositories.LevelRepository;
import org.tyaa.training.current.server.seeders.interfaces.ISeeder;

import java.util.List;

@Component
@Order(value=4)
public class LevelSeeder implements ISeeder {

    private final LevelRepository levelRepository;

    @Value("${custom.init-data.levels}")
    private List<String> levels;

    public LevelSeeder(LevelRepository levelRepository) {
        this.levelRepository = levelRepository;
    }

    @Override
    public void seed() {
        /* Обеспечение наличия в БД названий уровней владения языком */
        for (String levelName : levels) {
            levelRepository.save(LevelEntity.builder().name(levelName).build());
        }
    }
}
