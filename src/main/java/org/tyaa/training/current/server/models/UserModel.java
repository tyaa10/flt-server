package org.tyaa.training.current.server.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tyaa.training.current.server.models.interfaces.IModel;

/**
 * Модель пользователя
 * */
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserModel implements IModel {
    /**
     * Логин
     * */
    @NotBlank(message = "Username is required")
    @Pattern(regexp = "^[a-z0-9_-]{3,16}$", message = "Username can contain digits from 0 to 9, lowercase letters, _ and - characters, no space, and it must be 3-16 characters long")
    public String name;
    /**
     * Пароль
     * */
    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*\\W)(?!.* ).{8,16}$", message = "Password must contain one digit from 0 to 9, one lowercase letter, one uppercase letter, one special character, no space, and it must be 8-16 characters long")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    /**
     * Идентификатор роли
     * */
    public Long roleId;
    /**
     * Название роли
     * */
    public String roleName;
}
