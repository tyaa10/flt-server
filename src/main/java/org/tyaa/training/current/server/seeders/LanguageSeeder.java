package org.tyaa.training.current.server.seeders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.tyaa.training.current.server.entities.LanguageEntity;
import org.tyaa.training.current.server.entities.RoleEntity;
import org.tyaa.training.current.server.repositories.LanguageRepository;
import org.tyaa.training.current.server.repositories.RoleRepository;
import org.tyaa.training.current.server.seeders.interfaces.ISeeder;

import java.util.List;

@Component
@Order(value=3)
public class LanguageSeeder implements ISeeder {

    private final LanguageRepository languageRepository;

    @Value("${custom.init-data.languages}")
    private List<String> languages;

    public LanguageSeeder(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public void seed() {
        /* Обеспечение наличия в БД названий языков */
        for (String languageName : languages) {
            languageRepository.save(LanguageEntity.builder().name(languageName).build());
        }
    }
}
