package org.tyaa.training.current.server.fileprocessing.spreadsheets.interfaces;

import org.tyaa.training.current.server.models.imports.WordStudyImportModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Чтение данных из файлов электронных таблиц
 * */
public interface ISpreadsheetFileReader {
    /**
     * Чтение данных уроков по изучению слов в список моделей из файлов, хранимых в файловой системе
     * @param filePath строка пути к файлу таблицы, хранящемуся в файловой системе
     * */
    List<WordStudyImportModel> readWordStudyFromSpreadsheet(String filePath) throws IOException;
    /**
     * Чтение данных уроков по изучению слов в список моделей из файлов, получаемых через потоки из zip-архива
     * @param inputStream поток ввода данных из файла таблицы
     * */
    List<WordStudyImportModel> readWordStudyFromZip(InputStream inputStream) throws IOException;
}
