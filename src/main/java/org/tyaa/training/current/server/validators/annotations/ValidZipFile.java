package org.tyaa.training.current.server.validators.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.tyaa.training.current.server.validators.ZipFileValidator;

import java.lang.annotation.*;

/**
 * Аннотация валидации файлов zip-архивов
 * */
@Documented
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ZipFileValidator.class})
public @interface ValidZipFile {

    String message() default "Only ZIP archives are allowed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
