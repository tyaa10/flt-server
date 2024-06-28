package org.tyaa.training.current.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tyaa.training.current.server.entities.LanguageEntity;
import org.tyaa.training.current.server.entities.LanguageLevelEntity;

/**
 * Репозиторий комбинаций "родной язык - изучаемый язык - уровень владения языком"
 * */
@Repository
public interface LanguageLevelRepository extends JpaRepository<LanguageLevelEntity, Long> {

    @Query("SELECT ll FROM LanguageLevelEntity ll WHERE ll.level.name = :level and ll.nativeLanguage.name = :nativeLang and ll.learningLanguage.name = :learningLang")
    LanguageLevelEntity findLanguageLevel(
            @Param("level") String levelName,
            @Param("nativeLang") String nativeLanguageName,
            @Param("learningLang") String learningLanguageName
    );
}
