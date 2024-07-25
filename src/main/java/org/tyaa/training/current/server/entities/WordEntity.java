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
@Table(name = "Words")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class WordEntity implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "word", nullable = false, unique = true, length = 50)
    private String word;

    @Column(name = "translation", nullable = false, unique = true, length = 50)
    private String translation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="lesson_id", nullable = false)
    private WordLessonEntity wordLesson;

    @OneToMany(mappedBy = "word", fetch = FetchType.LAZY)
    private Set<WordTestEntity> wordTests = new HashSet<>();

    @Column(name = "sequence_number", nullable = false, unique = true)
    private Integer sequenceNumber;

    @Column(name = "image", nullable = false, length = Length.LOB_DEFAULT)
    private String image;

    @Column(name = "pronunciation_audio", nullable = false, length = Length.LOB_DEFAULT)
    private String pronunciationAudio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public WordLessonEntity getWordLesson() {
        return wordLesson;
    }

    public void setWordLesson(WordLessonEntity wordLesson) {
        this.wordLesson = wordLesson;
    }

    public Set<WordTestEntity> getWordTests() {
        return wordTests;
    }

    public void setWordTests(Set<WordTestEntity> wordTests) {
        this.wordTests = wordTests;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPronunciationAudio() {
        return pronunciationAudio;
    }

    public void setPronunciationAudio(String pronunciationAudio) {
        this.pronunciationAudio = pronunciationAudio;
    }
}
