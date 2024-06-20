package org.tyaa.training.current.server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.Length;

@Entity
@Table(name = "UserProfiles")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(length = Length.LOB_DEFAULT)
    private String avatar;

    @OneToOne(mappedBy = "profile")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="language_level_id", nullable = false)
    private LanguageLevelEntity languageLevel;

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public LanguageLevelEntity getLanguageLevel() {
        return languageLevel;
    }

    public void setLanguageLevel(LanguageLevelEntity languageLevel) {
        this.languageLevel = languageLevel;
    }
}
