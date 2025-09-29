package ak4ak.utillib.API.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogEntry {

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm:ss");

    private final LocalDateTime timestamp;
    private final String source;
    private final String message;
    private final Throwable throwable;

    public LogEntry(String source, String message, Throwable throwable) {
        this.timestamp = LocalDateTime.now();
        this.source    = source;
        this.message   = message;
        this.throwable = throwable;
    }

    /** Форматирует запись в строку: [HH:mm:ss] [Source] Message */
    public String format() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(timestamp.format(TIME_FORMATTER)).append("] ");
//        if (source != null && !source.isEmpty()) {
//            sb.append("[").append(source).append("] ");
//        }
        sb.append(message);

        if (throwable != null) {
            sb.append("\n  Ерор: ")
                    .append(throwable.getClass().getName())
                    .append(": ").append(throwable.getMessage());
            for (StackTraceElement el : throwable.getStackTrace()) {
                sb.append("\n    at ").append(el);
            }
        }
        return sb.toString();
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public String getSource()           { return source; }
    public String getMessage()          { return message; }
    public Throwable getThrowable()     { return throwable; }
}
