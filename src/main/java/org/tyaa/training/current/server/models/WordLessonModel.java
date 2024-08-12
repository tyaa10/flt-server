package org.tyaa.training.current.server.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Модель урока по изучению слов
 * */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WordLessonModel {

    /**
     * Локально уникальный идентификатор
     * */
    public Long id;
    /**
     * Название урока на родном языке
     * */
    public String name;
    /**
     * Множество моделей слов для изучения
     * */
    @Hidden
    public Set<WordLessonModel> words;
}
