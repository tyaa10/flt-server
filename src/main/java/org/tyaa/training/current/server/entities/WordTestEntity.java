package org.tyaa.training.current.server.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.tyaa.training.current.server.entities.interfaces.IEntity;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "WordTests")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class WordTestEntity implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* @Column(name = "success_score", nullable = false)
    private Double successScore; */

    @Column(name = "attempts_number", nullable = false)
    private Integer attemptsNumber;

    @Column(name = "success_number", nullable = false)
    private Integer successNumber;

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

    /* public Double getSuccessScore() {
        return successScore;
    }

    public void setSuccessScore(Double successScore) {
        this.successScore = successScore;
    } */

    public Integer getAttemptsNumber() {
        return attemptsNumber;
    }

    public void setAttemptsNumber(Integer attemptsNumber) {
        this.attemptsNumber = attemptsNumber;
    }

    public Integer getSuccessNumber() {
        return successNumber;
    }

    public void setSuccessNumber(Integer successNumber) {
        this.successNumber = successNumber;
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
