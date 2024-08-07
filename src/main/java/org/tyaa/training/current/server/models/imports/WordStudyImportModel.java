package org.tyaa.training.current.server.models.imports;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WordStudyImportModel {

    @NotBlank(message = "Lesson name is required")
    @Pattern(regexp = "^[\\p{L}'](?:[ '\\p{L}-]{1,50}\\p{L})?$", message = "Lesson name can contain only words, apostrophes, hyphens, spaces in the middle, can contain only words and apostrophes at the beginning and in the end, and it must be 1-50 characters long")
    public String lessonName;

    @NotBlank(message = "Level name is required")
    @Pattern(regexp = "^[\\p{L}'](?:[ '\\p{L}-]{1,50}\\p{L})?$", message = "A level name can only contain words, apostrophes, hyphens, spaces in the middle, can contain only words and apostrophes at the beginning and in the end, and it must be 1-50 characters long")
    public String levelName;

    @NotBlank(message = "Native language name is required")
    @Pattern(regexp = "^\\p{L}{1,50}$", message = "A native language name can only contain a word and must be between 1 and 50 characters long")
    public String nativeLanguageName;

    @NotBlank(message = "Learning language name is required")
    @Pattern(regexp = "^\\p{L}{1,50}$", message = "A learning language name can only contain a word and must be between 1 and 50 characters long")
    public String learningLanguageName;

    @NotNull(message = "Sequence number is required")
    @Min(value = 0, message = "Sequence number should not be less than 0")
    @Max(value = 2147483647, message = "Sequence number should not be greater than 2147483647")
    public Integer sequenceNumber;

    @NotBlank(message = "Word is required")
    @Pattern(regexp = "^[\\p{L}'](?:[ '\\p{L}-]{1,50}\\p{L})?$", message = "Word or sentence can contain only words, apostrophes, hyphens, spaces in the middle, can contain only words and apostrophes at the beginning and in the end, and it must be 1-50 characters long")
    public String word;

    @NotBlank(message = "Translation is required")
    @Pattern(regexp = "^[\\p{L}'](?:[ '\\p{L}-]{1,50}\\p{L})?$", message = "Translation can contain only words, apostrophes, hyphens, spaces in the middle, can contain only words and apostrophes at the beginning and in the end, and it must be 1-50 characters long")
    public String translation;

    @NotBlank(message = "Image as base64 string is required")
    public String image;

    @NotBlank(message = "Audio as base64 string is required")
    public String pronunciationAudio;
}
