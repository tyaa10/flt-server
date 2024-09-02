package org.tyaa.training.current.server.services.imports;

import org.springframework.stereotype.Service;
import org.tyaa.training.current.server.entities.LanguageLevelEntity;
import org.tyaa.training.current.server.entities.WordEntity;
import org.tyaa.training.current.server.entities.WordLessonEntity;
import org.tyaa.training.current.server.fileprocessing.spreadsheets.interfaces.ISpreadsheetFileReader;
import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.models.imports.WordStudyImportModel;
import org.tyaa.training.current.server.repositories.LanguageLevelRepository;
import org.tyaa.training.current.server.repositories.WordLessonRepository;
import org.tyaa.training.current.server.repositories.WordRepository;
import org.tyaa.training.current.server.services.interfaces.imports.ILessonsImportService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class LessonImportService implements ILessonsImportService {

    private final WordLessonRepository wordLessonRepository;
    private final LanguageLevelRepository languageLevelRepository;
    private final WordRepository wordRepository;
    private final ISpreadsheetFileReader spreadsheetFileReader;

    public LessonImportService(WordLessonRepository wordLessonRepository, LanguageLevelRepository languageLevelRepository, WordRepository wordRepository, ISpreadsheetFileReader spreadsheetFileReader) {
        this.wordLessonRepository = wordLessonRepository;
        this.languageLevelRepository = languageLevelRepository;
        this.wordRepository = wordRepository;
        this.spreadsheetFileReader = spreadsheetFileReader;
    }

    @Override
    public ResponseModel importWordLessons(List<WordStudyImportModel> wordStudies) {
        wordStudies.stream()
                // для каждого описания изучаемого слова
                .forEach(wordStudyImportModel -> {
                    // поиск записи "Урок по изучению слов" с указанным названием
                    WordLessonEntity wordLessonEntity =
                            wordLessonRepository.findWordLessonEntityByName(wordStudyImportModel.lessonName);
                    // создание записи "Урок по изучению слов" с указанным названием, если не существует
                    if(wordLessonEntity == null) {
                        System.out.println("*** wordStudyImportModel ***");
                        System.out.println(wordStudyImportModel);

                        LanguageLevelEntity languageLevelEntity =
                            languageLevelRepository.findLanguageLevel(
                                    wordStudyImportModel.levelName,
                                    wordStudyImportModel.nativeLanguageName,
                                    wordStudyImportModel.learningLanguageName
                            );

                        System.out.println("*** languageLevelEntity ***");
                        System.out.println(languageLevelEntity);

                        wordLessonEntity = wordLessonRepository.save(
                                WordLessonEntity.builder()
                                        .name(wordStudyImportModel.lessonName)
                                        .languageLevel(
                                                languageLevelRepository.findLanguageLevel(
                                                        wordStudyImportModel.levelName,
                                                        wordStudyImportModel.nativeLanguageName,
                                                        wordStudyImportModel.learningLanguageName
                                                )
                                        )
                                        .build()
                        );
                    }
                    // создание записей изучаемых слов
                    wordRepository.save(WordEntity.builder()
                                    .wordLesson(wordLessonEntity)
                                    .sequenceNumber(wordStudyImportModel.sequenceNumber)
                                    .word(wordStudyImportModel.word)
                                    .translation(wordStudyImportModel.translation)
                                    .image(wordStudyImportModel.image)
                                    .pronunciationAudio(wordStudyImportModel.pronunciationAudio)
                            .build());
                });
        // возврат модели данных ответа сервера об успешном импорте
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("%s WordStudy items imported", wordStudies.size()))
                .build();
    }

    @Override
    public ResponseModel importWordLessons(InputStream zipFileInputStream) throws IOException {
        // Передать ссылку на поток ввода данных из файла архива службе заполнения списка моделей из файлов,
        // и полученный от неё в ответ список передать методу импорта из списка
        return importWordLessons(spreadsheetFileReader.readWordStudyFromZip(zipFileInputStream));
    }
}
