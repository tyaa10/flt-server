package org.tyaa.training.current.server.test.unit.models;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.tyaa.training.current.server.models.UserModel;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Модульный тест для проверки валидации модели пользователя
 * */
public class UserModelUnitTest {

    /**
     * Проверка валидации модели пользователя
     * @param name - имя пользователя
     * @param password - пароль пользователя
     * @param expectedViolationCount - ожидаемое количество ошибок валидации
     * @param property - название невалидного свойства, если ожидается
     * */
    @ParameterizedTest
    @CsvFileSource(resources = "/unit/models/UserModelTest/testUserModelValidation.csv")
    public void testUserModelValidation(String name, String password, int expectedViolationCount, String property) {
        // Дано: имя пользователя и пароль из csv-файла,
        // а также ожидаемое количество ошибок валидации и название невалидного свойства, если ожидается
        System.out.println();
        System.out.printf("Validating name '%s' with password '%s'\n", name, password);
        System.out.printf("Expected violation count: %d\n", expectedViolationCount);
        if (property != null && !property.isBlank()) {
            System.out.printf("Expected invalid property: %s\n", property);
        }
        // Когда модель заполнена данными и проведена её валидация
        Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
            UserModel user =
                    UserModel.builder()
                            .id(1L)
                            .name(name)
                            .password(password)
                            .roleId(1L)
                            .roleName("testrole")
                            .build();
            // Тогда результаты валидации должны соответствовать ожидаемым
            Set<ConstraintViolation<UserModel>> violations = validator.validate(user);
            if (property != null && !property.isBlank() && !violations.isEmpty()) {
                System.err.println("Validation errors found:");
                violations.forEach(v -> System.err.printf("Property: %s, Message: %s\n", v.getPropertyPath(), v.getMessage()));
                assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().contains(property)));
            }
            assertEquals(expectedViolationCount, violations.size(), "No validation errors expected\n");
            System.out.println();
        }
    }
}
