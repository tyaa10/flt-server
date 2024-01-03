package org.tyaa.training.current.server.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сущность роли пользователя
 * */
@Entity
@Table(name = "Roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RoleEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @Size(min = 1, max = 16, message = "Value out of range [1; 16] characters")
    private String name;
}
