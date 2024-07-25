package org.tyaa.training.current.server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.tyaa.training.current.server.entities.interfaces.IEntity;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Levels")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LevelEntity implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @OneToMany(mappedBy = "level", fetch = FetchType.LAZY)
    private Set<LanguageLevelEntity> languageLevels = new HashSet<>();

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

    public Set<LanguageLevelEntity> getLanguageLevels() {
        return languageLevels;
    }

    public void setLanguageLevels(Set<LanguageLevelEntity> languageLevels) {
        this.languageLevels = languageLevels;
    }
}
