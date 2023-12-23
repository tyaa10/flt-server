package org.tyaa.training.current.server.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

/**
 * Сущность роли пользователя
 * */
@Entity
@Table(name = "Roles")
public class RoleEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @Size(min = 1, max = 16, message = "Value out of range [1; 16] characters")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
