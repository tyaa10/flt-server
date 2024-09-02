package org.tyaa.training.current.server.fileprocessing.mediafiles;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * Преобразование данных из медиа-файлов в Base64-строки
 * */
@Service
public class BinaryFilesReader {

    /**
     * Преобразование данных из файла в файловой системе в Base64-строку
     * @param filePath строка пути к файлу в файловой системе
     * */
    public String readToBase64(String filePath) throws IOException {
        return Base64.getEncoder().encodeToString(
                Files.readAllBytes(Paths.get(filePath))
        );
    }

    /**
     * Преобразование данных файла в виде массива в оперативной памяти в Base64-строку
     * @param fileDataByteArray данные файла в виде массива в оперативной памяти
     * */
    public String readToBase64(byte[] fileDataByteArray) {
        return Base64.getEncoder().encodeToString(fileDataByteArray);
    }
}
