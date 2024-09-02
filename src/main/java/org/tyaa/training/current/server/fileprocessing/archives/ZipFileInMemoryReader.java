package org.tyaa.training.current.server.fileprocessing.archives;

import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Чтение zip-архивов в оперативной памяти
 * */
@Service
public class ZipFileInMemoryReader {

    private final Map<String, byte[]> mediaItems = new HashMap<>();

    /**
     * Прочесть из zip-архива данные основного файла
     * */
    public InputStream readSpreadsheetFileFromZip(InputStream zipFileInputStream) throws IOException {
        // Переменная для результирующего потока вывода данных из основного файла, извлекаемого из архива
        ByteArrayInputStream spreadsheetFileInputStream = null;
        // Получить поток ввода из файла архива
        ZipInputStream zipInput = new ZipInputStream(zipFileInputStream);
        ZipEntry entry;
        // Перебрать все вхождения архива
        while ((entry = zipInput.getNextEntry()) != null) {
            // Обработать только основной файл - с расширением xlsx
            if (entry.getName().endsWith(".xlsx")) {
                // Накопить данные в буфере потока вывода
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = zipInput.read(buffer)) != -1) {
                    outStream.write(buffer, 0, length);
                }
                // Подключить результирующий поток ввода к потоку вывода с данными
                spreadsheetFileInputStream = new ByteArrayInputStream(outStream.toByteArray());
            } else if(entry.getName().endsWith(".mp3")) {
                // Накопить данные в буфере потока вывода
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = zipInput.read(buffer)) != -1) {
                    outStream.write(buffer, 0, length);
                }
                // Добавить медиа-данные в словарь
                mediaItems.put(entry.getName().replace("\\", "/"), outStream.toByteArray());
            }
        }
        return spreadsheetFileInputStream;
    }

    /**
     * Прочесть из zip-архива данные файла, расположенного по указанному пути
     * */
    public byte[] readMediaFileFromZip(String relativeFilePathString) throws IOException {
        // Переменная для результирующего массива данных медиа-файла, ранее прочитанных из архива
        byte[] fileDataByteArray = null;
        // Перебрать все ранее сохранённые данные медиа-вхождений архива
        for (Map.Entry<String, byte[]> mediaItem : mediaItems.entrySet()) {
            // Если название текущего вхождения архива совпадает с путём к файлу
            System.out.println("entry: " + mediaItem.getKey() + " -> relativeFilePathString: " + relativeFilePathString.replace("\\", "/"));
            if (mediaItem.getKey().equals(relativeFilePathString.replace("\\", "/"))) {
                // Скопировать ссылку на массив байтов медиа-вхождения в выходную переменную
                fileDataByteArray = mediaItem.getValue();
                break;
            }
        }
        return fileDataByteArray;
    }
}
