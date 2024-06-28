package org.tyaa.training.current.server.services.interfaces.imports;

import org.tyaa.training.current.server.models.ResponseModel;
import org.tyaa.training.current.server.models.imports.WordStudyImportModel;

import java.util.List;

/**
 * Интерфейс службы импорта данных уроков
 * */
public interface ILessonsImportService {

    ResponseModel importWordLessons(List<WordStudyImportModel> wordStudies);
}
