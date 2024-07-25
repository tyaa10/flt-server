package org.tyaa.training.current.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tyaa.training.current.server.entities.UserProfileEntity;

import java.util.Optional;

/**
 * Репозиторий профилей пользователей
 * */
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileEntity, Long> {

    @Query("SELECT p FROM UserProfileEntity p WHERE p.user.name = :user")
    Optional<UserProfileEntity> findProfileByUserName(
            @Param("user") String userName
    );
}
