package org.tyaa.training.current.server.seeders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.tyaa.training.current.server.entities.LanguageLevelEntity;
import org.tyaa.training.current.server.repositories.LanguageLevelRepository;
import org.tyaa.training.current.server.repositories.LanguageRepository;
import org.tyaa.training.current.server.repositories.LevelRepository;
import org.tyaa.training.current.server.seeders.interfaces.ISeeder;
import org.tyaa.training.current.server.services.interfaces.ILanguageService;
import org.tyaa.training.current.server.services.interfaces.ILevelService;

import java.util.List;

@Component
@Order(value=5)
public class LanguageLevelSeeder implements ISeeder {

    private final LanguageRepository languageRepository;
    private final LevelRepository levelRepository;
    private final LanguageLevelRepository languageLevelRepository;
    @Value("${custom.init-data.languages}")
    private List<String> languages;
    @Value("${custom.init-data.levels}")
    private List<String> levels;

    public LanguageLevelSeeder(
            LanguageRepository languageRepository,
            LevelRepository levelRepository,
            LanguageLevelRepository languageLevelRepository) {
        this.languageRepository = languageRepository;
        this.levelRepository = levelRepository;
        this.languageLevelRepository = languageLevelRepository;
    }

    @Override
    public void seed() {
        LanguageLevelEntity languageLevelEntity =
                LanguageLevelEntity.builder()
                        .nativeLanguage(languageRepository.findLanguageEntityByName(languages.get(ILanguageService.BASE_LANGUAGES.RUSSIAN.ordinal())))
                        .learningLanguage(languageRepository.findLanguageEntityByName(languages.get(ILanguageService.BASE_LANGUAGES.ENGLISH.ordinal())))
                        .level(levelRepository.findLevelEntityByName(levels.get(ILevelService.BASE_LEVELS.BEGINNER.ordinal())))
                        .active(true)
                        .build();
        languageLevelRepository.save(languageLevelEntity);
    }
}
