package org.tyaa.training.current.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tyaa.training.current.server.entities.LanguageEntity;

/**
 * Репозиторий языков
 * */
@Repository
public interface LanguageRepository extends JpaRepository<LanguageEntity, Long> {
    LanguageEntity findLanguageEntityByName(String name);
}
