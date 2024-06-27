package org.tyaa.training.current.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tyaa.training.current.server.entities.LevelEntity;
import org.tyaa.training.current.server.entities.WordLessonEntity;

/**
 * Репозиторий уроков по изучению слов
 * */
@Repository
public interface WordLessonRepository extends JpaRepository<WordLessonEntity, Long> {
    WordLessonEntity findWordLessonEntityByName(String name);
}
