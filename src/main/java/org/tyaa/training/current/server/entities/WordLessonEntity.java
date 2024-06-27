package org.tyaa.training.current.server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "WordLessons")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class WordLessonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="language_level_id", nullable = false)
    private LanguageLevelEntity languageLevel;

    @OneToMany(mappedBy = "wordLesson", fetch = FetchType.LAZY)
    private Set<WordEntity> words = new HashSet<>();

    @OneToOne(mappedBy = "currentWordLesson")
    private UserProfileEntity currentWordLessonProfile;

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

    public LanguageLevelEntity getLanguageLevel() {
        return languageLevel;
    }

    public void setLanguageLevel(LanguageLevelEntity languageLevel) {
        this.languageLevel = languageLevel;
    }

    public Set<WordEntity> getWords() {
        return words;
    }

    public void setWords(Set<WordEntity> words) {
        this.words = words;
    }
}
