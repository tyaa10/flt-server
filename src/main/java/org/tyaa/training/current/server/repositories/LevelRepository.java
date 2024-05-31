package org.tyaa.training.current.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tyaa.training.current.server.entities.LevelEntity;

/**
 * Репозиторий уровней владения языком
 * */
@Repository
public interface LevelRepository extends JpaRepository<LevelEntity, Long> {}
