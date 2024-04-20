package org.tyaa.training.current.server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tyaa.training.current.server.entities.UserEntity;

/**
 * Репозиторий для работы с данными о пользователях
 * */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findUserByName(String name);
}
