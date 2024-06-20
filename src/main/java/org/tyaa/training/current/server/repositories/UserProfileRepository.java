package org.tyaa.training.current.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tyaa.training.current.server.entities.LanguageEntity;
import org.tyaa.training.current.server.entities.UserProfileEntity;

/**
 * Репозиторий профилей пользователей
 * */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {}
