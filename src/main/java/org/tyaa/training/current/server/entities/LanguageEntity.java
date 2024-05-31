package org.tyaa.training.current.server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Languages")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LanguageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @OneToMany(mappedBy = "nativeLanguage", fetch = FetchType.LAZY)
    private Set<UserProfileEntity> profilesNative = new HashSet<>();

    @OneToMany(mappedBy = "learningLanguage", fetch = FetchType.LAZY)
    private Set<UserProfileEntity> profilesLearning = new HashSet<>();

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

    public Set<UserProfileEntity> getProfilesNative() {
        return profilesNative;
    }

    public void setProfilesNative(Set<UserProfileEntity> profilesNative) {
        this.profilesNative = profilesNative;
    }

    public Set<UserProfileEntity> getProfilesLearning() {
        return profilesLearning;
    }

    public void setProfilesLearning(Set<UserProfileEntity> profilesLearning) {
        this.profilesLearning = profilesLearning;
    }
}
