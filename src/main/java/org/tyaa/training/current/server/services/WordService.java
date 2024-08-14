package org.tyaa.training.current.server.services;

import org.springframework.stereotype.Service;
import org.tyaa.training.current.server.entities.WordEntity;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.models.WordModel;
import org.tyaa.training.current.server.repositories.WordRepository;
import org.tyaa.training.current.server.services.interfaces.IWordService;

import java.util.List;

import static org.tyaa.training.current.server.models.ResponseModel.SUCCESS_STATUS;

/**
 * Реализация службы изучаемых слов
 * */
@Service
public class WordService implements IWordService {

    private final WordRepository wordRepository;

    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public static WordModel entityToModel(WordEntity wordEntity) {
        return WordModel.builder()
                .id(wordEntity.getId())
                .word(wordEntity.getWord())
                .translation(wordEntity.getTranslation())
                .sequenceNumber(wordEntity.getSequenceNumber())
                .image(wordEntity.getImage())
                .pronunciationAudio(wordEntity.getPronunciationAudio())
                .build();
    }

    @Override
    public ResponseModel getWordsByWordLessonId(Long wordLessonId) {
        List<WordModel> wordModels =
                wordRepository.findByWordLessonId(wordLessonId).stream()
                        .map(WordService::entityToModel)
                        .toList();
        return ResponseModel.builder()
                .status(SUCCESS_STATUS)
                .message(wordModels.size() > 0 ? String.format("%s words fetched", wordModels.size()) : "No words")
                .data(wordModels)
                .build();
    }
}
