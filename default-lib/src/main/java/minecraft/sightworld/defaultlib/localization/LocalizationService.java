package minecraft.sightworld.defaultlib.localization;

import java.util.List;

public interface LocalizationService {

    String[] getFiles();

    /**
     * Скачивание файла из репозитория по имени
     *
     * @param filePrefix имя файла
     * @param fileUrl имя файла
     */
    void download(String filePrefix, String fileUrl);

    void loadFile(String filePrefix, String filePath);

    /**
     * Получения сообщения по ключу
     *
     * @param type   язык
     * @param key    ключ
     * @param format форматирование
     * @return локализированное сообщение, если сообщение не найдено на другом
     * языке, то вернёт сообщение на русском, если нет сообщения на русском,
     * то вернёт имя ключа!
     */
    String get(Language type, String key, Object... format);

    /**
     * Получить список сообщений
     *
     * @param type   язык
     * @param key    ключ
     * @param format форматирование
     * @return список сообщений, если сообщение не найдено на другом
     * языке, то вернёт сообщение на русском, если нет сообщения на русском,
     * то вернёт имя ключа!
     */
    List<String> getList(Language type, String key, Object... format);

}
