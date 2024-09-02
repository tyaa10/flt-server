package org.tyaa.training.current.server.fileprocessing.spreadsheets;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.tyaa.training.current.server.fileprocessing.archives.ZipFileInMemoryReader;
import org.tyaa.training.current.server.fileprocessing.mediafiles.BinaryFilesReader;
import org.tyaa.training.current.server.fileprocessing.spreadsheets.interfaces.ISpreadsheetFileReader;
import org.tyaa.training.current.server.models.imports.WordStudyImportModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.*;

@Service
public class ExcelFileReader implements ISpreadsheetFileReader {

    private final BinaryFilesReader binaryFilesReader;
    private final ZipFileInMemoryReader zipFileReader;

    public ExcelFileReader(BinaryFilesReader binaryFilesReader, ZipFileInMemoryReader zipFileReader) {
        this.binaryFilesReader = binaryFilesReader;
        this.zipFileReader = zipFileReader;
    }

    @Override
    public List<WordStudyImportModel> readWordStudyFromSpreadsheet(String spreadsheetFilePath) throws IOException {

        FileInputStream fileInputStream = new FileInputStream(spreadsheetFilePath);

        return readWordStudy(fileInputStream, (learningLanguageName, wordTranslation) ->
        {
            try {
                return getAudioFileBase64FromFilesystem(spreadsheetFilePath, learningLanguageName, wordTranslation);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<WordStudyImportModel> readWordStudyFromZip(InputStream zipFileInputStream) throws IOException {

        return readWordStudy(zipFileReader.readSpreadsheetFileFromZip(zipFileInputStream), (learningLanguageName, wordTranslation) ->
        {
            try {
                return getAudioFileBase64FromZip(learningLanguageName, wordTranslation);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Прочесть данные для изучения слов файла xlsx из потока ввода
     * @param spreadsheetFileInputStream поток ввода данных из файла xlsx
     * @param audioDataProvider реализация метода извлечения данных аудио в формате mp3 в виде base64-строки из указанного источника
     * */
    private List<WordStudyImportModel> readWordStudy(InputStream spreadsheetFileInputStream, IAudioDataProvider audioDataProvider) throws IOException {
        // Заполнение модели книги электронных таблиц из потока ввода
        Workbook workbook = new XSSFWorkbook(spreadsheetFileInputStream);
        // Получение модели второго листа книги электронных таблиц
        Sheet sheet = workbook.getSheetAt(1);
        // Создание и заполнение словаря заголовков колонок
        Map<String, Integer> headerMap = new HashMap<>();
        int headerCellIndex = 0;
        for (Cell headerCell : sheet.getRow(0)) {
            String headerCellValue = headerCell.getStringCellValue().trim();
            if (!headerCellValue.isBlank()) {
                headerMap.put(headerCellValue, headerCellIndex);
            }
            headerCellIndex++;
        }
        // Чтение текстовых и числовых данных строк в словарь
        LinkedHashMap<Integer, Map<Integer, Object>> simpleData = new LinkedHashMap<>();
        int rowCellIndex = 0;
        // перебор всех рядов данных
        for (Row row : sheet) {
            // первый ряд пропускается, так как в нём - заголовки колонок
            if(rowCellIndex == 0) {
                rowCellIndex++;
                continue;
            }
            // для каждого ряда создаётся одно вхождение словаря рядов:
            // ключ - индекс ячейки в ряду
            // значение - пустой словарь для накопления значений из ячеек ряда
            simpleData.put(rowCellIndex, new LinkedHashMap<>());
            // перебор всех ячеек в ряду
            for (Cell cell : row) {
                System.out.println(cell.getCellType());
                switch (cell.getCellType()) {
                    // в зависимости от типа данных в ячейке, они приводятся к соответствующему Java-типу
                    // и добавляются в словарь значений ячеек ряда
                    case STRING -> simpleData.get(rowCellIndex).put(cell.getColumnIndex(), cell.getStringCellValue().isBlank() ? "-" : cell.getStringCellValue().trim());
                    case NUMERIC -> simpleData.get(rowCellIndex).put(cell.getColumnIndex(), (int) Math.round(cell.getNumericCellValue()));
                }
            }
            System.out.println();
            rowCellIndex++;
        }
        // Чтение двоичных данных (изображений) листа в список
        XSSFDrawing patriarch = (XSSFDrawing) sheet.createDrawingPatriarch();
        List<XSSFShape> shapes = patriarch.getShapes();
        List<Picture> pictures =
                shapes.stream()
                        .filter(Picture.class::isInstance)
                        .map(xssfShape -> (Picture)xssfShape)
                        .toList();
        // Заполнение списка моделей импортируемых изучаемых слов
        final List<WordStudyImportModel> words = new ArrayList<>();
        simpleData.forEach((index, values) -> {
            if (!values.isEmpty() && !values.get(0).toString().isBlank()) {
                words.add(WordStudyImportModel.builder()
                    .lessonName((String) values.get(headerMap.get("урок")))
                    .nativeLanguageName((String) values.get(headerMap.get("родной язык")))
                    .learningLanguageName((String) values.get(headerMap.get("изучаемый язык")))
                    .levelName((String) values.get(headerMap.get("уровень")))
                    .sequenceNumber((Integer) values.get(headerMap.get("номер объекта")))
                    .word((String) values.get(headerMap.get("слово")))
                    .translation((String) values.get(headerMap.get("перевод")))
                    .image(Base64.getEncoder().encodeToString(pictures.stream().filter(object -> {
                        if (object.getAnchor() instanceof ClientAnchor anc) {
                            return (anc.getRow1() == index && anc.getCol1() == headerMap.get("картинка"));
                        } else {
                            return false;
                        }
                    }).findFirst().get().getPictureData().getData()))
                    .pronunciationAudio(
                            audioDataProvider.get(
                                    ((String) values.get(headerMap.get("изучаемый язык"))).trim(),
                                    ((String) values.get(headerMap.get("перевод"))).trim()
                            )
                    ).build());
            }
        });
        workbook.close();
        return words;
    }

    /**
     * Получение данных аудио-файла в формате mp3 в виде base64-строки из источника в файловой системе
     * */
    private String getAudioFileBase64FromFilesystem(String excelFilePath, String learningLanguageName, String wordTranslation) throws IOException {
        return binaryFilesReader.readToBase64(Paths.get(
                Paths.get(excelFilePath).getParent().toString(),
                "audio",
                learningLanguageName,
                String.format("%s.mp3", wordTranslation)
        ).toString());
    }

    /**
     * Получение данных аудио-файла в формате mp3 в виде base64-строки
     * из текущего внедрённого экземпляра службы чтения данных медиа-файлов
     * */
    private String getAudioFileBase64FromZip(String learningLanguageName, String wordTranslation) throws IOException {
        final String zippedAudioFilePath =
                Paths.get(
                        "audio",
                        learningLanguageName,
                        String.format("%s.mp3", wordTranslation)
                ).toString();
        return binaryFilesReader.readToBase64(
                zipFileReader.readMediaFileFromZip(zippedAudioFilePath)
        );
    }

    @FunctionalInterface
    private interface IAudioDataProvider {
        /**
         * Получить данные аудио в формате mp3 в виде base64-строки из указанного источника
         * @param learningLanguageName название изучаемого языка
         * @param wordTranslation перевод слова на изучаемый язык
         * */
        String get(String learningLanguageName, String wordTranslation);
    };
}
