package org.tyaa.training.current.server.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tyaa.training.current.server.models.interfaces.IModel;

/**
 * Модель профиля пользователя
 * */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileModel implements IModel {
    /**
     * Локально уникальный идентификатор
     * */
    public Long id;
    /**
     * Псевдоним или имя пользователя
     * */
    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^[a-z0-9_-]{3,16}$", message = "Username can contain digits from 0 to 9, lowercase letters, _ and - characters, no space, and it must be 3-16 characters long")
    public String name;
    /**
     * Аватар пользователя в виде строки в формате base64, полученной из файла изображения в формате png
     * */
    public String avatar;
    /**
     * Название родного языка пользователя
     * */
    public String nativeLanguageName;
    /**
     * Название языка, изучаемого пользователем
     * */
    public String learningLanguageName;
    /**
     * Название уровня владения языком, на который обучается пользователь
     * */
    public String levelName;
}
