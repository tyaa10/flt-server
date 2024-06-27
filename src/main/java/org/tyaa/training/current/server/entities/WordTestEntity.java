package org.tyaa.training.current.server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "WordTests")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class WordTestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "success_score", nullable = false)
    private Double successScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="profile_id", nullable = false)
    private UserProfileEntity profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="word_id", nullable = false)
    private WordEntity word;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSuccessScore() {
        return successScore;
    }

    public void setSuccessScore(Double successScore) {
        this.successScore = successScore;
    }

    public UserProfileEntity getProfile() {
        return profile;
    }

    public void setProfile(UserProfileEntity profile) {
        this.profile = profile;
    }

    public WordEntity getWord() {
        return word;
    }

    public void setWord(WordEntity word) {
        this.word = word;
    }
}
