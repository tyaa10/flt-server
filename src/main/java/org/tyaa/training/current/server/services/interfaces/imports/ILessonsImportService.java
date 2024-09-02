package org.tyaa.training.current.server.services.interfaces.imports;

import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.models.imports.WordStudyImportModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Интерфейс службы импорта данных уроков
 * */
public interface ILessonsImportService {

    /**
     * Импорт данных уроков по изучению слов в БД из списка моделей
     * @param wordStudies список моделей данных уроков по изучению слов
     * */
    ResponseModel importWordLessons(List<WordStudyImportModel> wordStudies);
    /**
     * Импорт данных уроков по изучению слов в БД из потока ввода, подключённого к zip-файлу
     * @param zipFileInputStream потока ввода данных, подключённый к zip-файлу, содержащему электронную таблицу и медиа-файлы к ней
     * */
    ResponseModel importWordLessons(InputStream zipFileInputStream) throws IOException;
}
