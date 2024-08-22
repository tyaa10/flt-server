package org.tyaa.training.current.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tyaa.training.current.server.entities.WordEntity;
import org.tyaa.training.current.server.entities.WordTestEntity;

import java.util.Optional;

/**
 * Репозиторий статистики успешных переводов слова определённым пользователем
 * */
@Repository
public interface WordTestRepository extends JpaRepository<WordTestEntity, Long> {

    Optional<WordTestEntity> findWordTestEntityByWordIdAndProfileId(Long wordId, Long profileId);
}
