package org.tyaa.training.current.server.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;
import org.tyaa.training.current.server.validators.annotations.ValidZipFile;

/**
 * Валидация файлов zip-архивов
 * */
public class ZipFileValidator implements ConstraintValidator<ValidZipFile, MultipartFile> {

    @Override
    public void initialize(ValidZipFile constraintAnnotation) {
        // ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        boolean result = true;
        final String fileNameExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        final String contentType = multipartFile.getContentType();
        if (!isSupportedExtension(fileNameExtension) || !isSupportedContentType(contentType)) {
            result = false;
        }
        return result;
    }

    /**
     * Расширение имени файла должно быть равно "zip"
     * */
    private boolean isSupportedExtension(String fileNameExtension) {
        System.out.println("fileNameExtension = " + fileNameExtension);
        return fileNameExtension != null && (fileNameExtension.equals("zip"));
    }

    /**
     * Название медиа-типа содержимого файла должно быть равно "application/zip"
     * */
    private boolean isSupportedContentType(String contentType) {
        System.out.println("contentType = " + contentType);
        return contentType != null && (contentType.equals("application/zip") || contentType.equals("application/x-zip-compressed"));
    }
}
