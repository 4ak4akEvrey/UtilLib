package ak4ak.utillib.API.Logger;

import ak4ak.utillib.UtilLib;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class LogManager {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private static final List<LogManager> registry =
            Collections.synchronizedList(new ArrayList<>());

    private static JavaPlugin plugin = UtilLib.instance;
    private final String directoryName;

    private final List<LogEntry> buffer = new CopyOnWriteArrayList<>();
    private final java.util.logging.Logger consoleLogger;
    private boolean printToConsole = true;

    public LogManager(String directoryName) {
        this.directoryName = directoryName;
        this.consoleLogger = plugin.getLogger();

        registry.add(this);
    }

    public void info(String message) {
        log(message, null);
    }

    public void warning(String message) {
        log("[WARN] " + message, null);
    }

    public void warning(String message, Throwable t) {
        log("[WARN] " + message, t);
    }

    public void error(String message) {
        log("[ERROR] " + message, null);
    }

    public void error(String message, Throwable t) {
        log("[ERROR] " + message, t);
    }

    public void debug(String message) {
        log("[DEBUG] " + message, null);
    }

    public void log(String message) {
        LogEntry entry = new LogEntry(directoryName, message, null);
        buffer.add(entry);

        // Дублируем в MAIN логгер (если он существует и это не сам main)
        MainLogger main = MainLogger.getInstance();
        if (main != null && !directoryName.equalsIgnoreCase("main")) {
            main.receive(entry);
        }

        if (printToConsole) {
            String msg = "[" + directoryName + "] " + message;
            if (message.startsWith("[ERROR]")) consoleLogger.severe(msg);
            else if (message.startsWith("[WARN]")) consoleLogger.warning(msg);
            else consoleLogger.info(msg);

        }
    }

    /** Базовый метод — всё идёт сюда */
    public void log(String message, Throwable throwable) {
        LogEntry entry = new LogEntry(directoryName, message, throwable);
        buffer.add(entry);

        // Дублируем в MAIN логгер (если он существует и это не сам main)
        MainLogger main = MainLogger.getInstance();
        if (main != null && !directoryName.equalsIgnoreCase("main")) {
            main.receive(entry);
        }

        if (printToConsole) {
            String msg = "[" + directoryName + "] " + message;
            if (message.startsWith("[ERROR]")) consoleLogger.severe(msg);
            else if (message.startsWith("[WARN]")) consoleLogger.warning(msg);
            else consoleLogger.info(msg);

            if (throwable != null) {
                consoleLogger.severe("  => " + throwable.getClass().getName() + ": " + throwable.getMessage());
            }
        }
    }

    // ─────────────────────────────────────────
    // Запись в файл
    // ─────────────────────────────────────────

    /**
     * Сбрасывает буфер в:
     *   plugins/MyPlugin/logs/<directoryName>/ДД.ММ.ГГГГ.txt
     * Если файл существует — дописывает в конец.
     */
    public void flush() {
        if (buffer.isEmpty()) return;
        writeToFile(getLogFile(), new ArrayList<>(buffer));
        buffer.clear();
    }

    /** Возвращает файл лога этого менеджера за сегодня */
    public File getLogFile() {
        return getLogFile(LocalDate.now());
    }

    /** Возвращает файл лога этого менеджера за указанную дату */
    public File getLogFile(LocalDate date) {
        File dir = new File(plugin.getDataFolder(), "logs/" + directoryName);
        dir.mkdirs();
        return new File(dir, date.format(DATE_FORMATTER) + ".txt");
    }

    // ─────────────────────────────────────────
    // Статические утилиты
    // ─────────────────────────────────────────

    /** flush() для всех зарегистрированных менеджеров */
    public static void flushAll() {
        synchronized (registry) {
            for (LogManager m : registry) m.flush();
        }
        // После всех менеджеров — сбрасываем и main
        MainLogger main = MainLogger.getInstance();
        if (main != null) main.flush();
    }

    public static void clearRegistry() {
        registry.clear();
    }

    /**
     * Возвращает список всех зарегистрированных имён директорий.
     * Используется в Discord боте для построения Select Menu.
     */
    public static List<String> getRegisteredDirectories() {
        List<String> names = new ArrayList<>();
        synchronized (registry) {
            for (LogManager m : registry) {
                names.add(m.getDirectoryName());
            }
        }
        // Добавляем "main" как всегда доступный вариант
        if (!names.contains("main")) names.add(0, "main");
        return Collections.unmodifiableList(names);
    }

    // ─────────────────────────────────────────
    // Вспомогательные методы
    // ─────────────────────────────────────────

    static void writeToFile(File file, List<LogEntry> entries) {
        try (BufferedWriter w = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file, true), StandardCharsets.UTF_8))) {

            if (file.length() > 0) {
                w.write("\n════════════════════════════════════════\n");
            }
            w.write("=== Сервер ВКЛ ===\n");
            for (LogEntry e : entries) {
                w.write(e.format());
                w.newLine();
            }
            w.write("=== Сервер выкл | " + entries.size() + " строк ===\n");

        } catch (IOException e) {
            System.err.println("[LogManager] Ошибка записи в " + file.getPath() + ": " + e.getMessage());
        }
    }

    public LogManager setPrintToConsole(boolean v) { this.printToConsole = v; return this; }
    public String getDirectoryName()               { return directoryName; }
    public List<LogEntry> getBuffer()              { return Collections.unmodifiableList(buffer); }
}