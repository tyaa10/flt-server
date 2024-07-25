package org.tyaa.training.current.server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.Length;
import org.tyaa.training.current.server.entities.interfaces.IEntity;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "LanguageLevels")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LanguageLevelEntity implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="native_language_id", nullable = false)
    private LanguageEntity nativeLanguage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="learning_language_id", nullable = false)
    private LanguageEntity learningLanguage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="level_id", nullable = false)
    private LevelEntity level;

    @Column(name = "active")
    private boolean active;

    @OneToMany(mappedBy = "languageLevel", fetch = FetchType.LAZY)
    private Set<WordLessonEntity> wordLessons = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LanguageEntity getNativeLanguage() {
        return nativeLanguage;
    }

    public void setNativeLanguage(LanguageEntity nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    public LanguageEntity getLearningLanguage() {
        return learningLanguage;
    }

    public void setLearningLanguage(LanguageEntity learningLanguage) {
        this.learningLanguage = learningLanguage;
    }

    public LevelEntity getLevel() {
        return level;
    }

    public void setLevel(LevelEntity level) {
        this.level = level;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<WordLessonEntity> getWordLessons() {
        return wordLessons;
    }

    public void setWordLessons(Set<WordLessonEntity> wordLessons) {
        this.wordLessons = wordLessons;
    }
}
