package org.tyaa.training.current.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tyaa.training.current.server.entities.LevelEntity;
import org.tyaa.training.current.server.entities.WordEntity;
import org.tyaa.training.current.server.entities.WordLessonEntity;

import java.util.List;

/**
 * Репозиторий слов с переводом, изображением и аудио произношения
 * */
@Repository
public interface WordRepository extends JpaRepository<WordEntity, Long> {
    WordEntity findWordEntityByWord(String word);
    List<WordEntity> findByWordLessonId(Long wordLessonId);
}
