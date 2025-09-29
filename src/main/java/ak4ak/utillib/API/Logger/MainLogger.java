package ak4ak.utillib.API.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Главный логгер — синглтон.
 * Автоматически получает ВСЕ записи от всех LogManager-ов
 * и пишет их в один общий файл: logs/main/ДД.ММ.ГГГГ.txt
 *
 * Инициализировать ПЕРВЫМ, до любых LogManager-ов:
 *   MainLogger.init(plugin);
 *
 * Получить экземпляр из любого места:
 *   MainLogger.getInstance().info("Сообщение напрямую в main");
 */
public class MainLogger {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static MainLogger instance;

    private final JavaPlugin plugin;
    private final List<LogEntry> buffer = new CopyOnWriteArrayList<>();

    private MainLogger(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // ─────────────────────────────────────────
    // Инициализация
    // ─────────────────────────────────────────

    /** Создать синглтон. Вызывать в onEnable() до всех LogManager-ов. */
    public static MainLogger init(JavaPlugin plugin) {
        instance = new MainLogger(plugin);
        return instance;
    }

    /** Получить синглтон. Вернёт null если init() ещё не был вызван. */
    public static MainLogger getInstance() {
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

    // ─────────────────────────────────────────
    // Приём записей от LogManager-ов
    // ─────────────────────────────────────────

    /**
     * Вызывается автоматически из каждого LogManager.log().
     * Не нужно вызывать вручную.
     */
    void receive(LogEntry entry) {
        buffer.add(entry);
    }

    // ─────────────────────────────────────────
    // Прямые методы логирования
    // (для записей напрямую в main, минуя конкретный модуль)
    // ─────────────────────────────────────────

    public void info(String message) {
        buffer.add(new LogEntry("main", message, null));
        plugin.getLogger().info("[main] " + message);
    }

    public void warning(String message) {
        buffer.add(new LogEntry("main", "[WARN] " + message, null));
        plugin.getLogger().warning("[main] " + message);
    }

    public void error(String message, Throwable t) {
        buffer.add(new LogEntry("main", "[ERROR] " + message, t));
        plugin.getLogger().severe("[main] " + message);
    }

    // ─────────────────────────────────────────
    // Сохранение
    // ─────────────────────────────────────────

    /** Сбрасывает буфер в logs/main/ДД.ММ.ГГГГ.txt */
    public void flush() {
        if (buffer.isEmpty()) return;
        LogManager.writeToFile(getLogFile(), new ArrayList<>(buffer));
        buffer.clear();
    }

    public File getLogFile() {
        return getLogFile(LocalDate.now());
    }

    public File getLogFile(LocalDate date) {
        File dir = new File(plugin.getDataFolder(), "logs/main");
        dir.mkdirs();
        return new File(dir, date.format(DATE_FORMATTER) + ".txt");
    }

    public List<LogEntry> getBuffer() {
        return Collections.unmodifiableList(buffer);
    }
}
