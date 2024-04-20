package org.tyaa.training.current.server.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.tyaa.training.current.server.entities.RoleEntity;

/**
 * Репозиторий для работы с ролями пользователей
 * */
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    /**
     * Заказ для каркаса сгенерировать реализацию метода,
     * вызов которого приведёт к удалению всех строк в таблице "Роли"
     * и к сбросу счётчика в колонке идентификаторов
     * */
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE Roles RESTART IDENTITY", nativeQuery = true)
    void truncateTable();

    RoleEntity findRoleByName(String name);
}
