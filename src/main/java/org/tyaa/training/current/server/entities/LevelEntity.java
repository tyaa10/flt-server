package org.tyaa.training.current.server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "Levels")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LevelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @OneToMany(mappedBy = "level", fetch = FetchType.LAZY)
    private Set<UserProfileEntity> profiles;

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

    public Set<UserProfileEntity> getProfiles() {
        return profiles;
    }

    public void setProfiles(Set<UserProfileEntity> profiles) {
        this.profiles = profiles;
    }
}
