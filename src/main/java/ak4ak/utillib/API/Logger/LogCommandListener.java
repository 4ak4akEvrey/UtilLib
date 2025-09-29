package ak4ak.utillib.API.Logger;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.FileUpload;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Слушатель slash-команды /log.
 * Проверяет роль пользователя и отправляет файл лога в чат.
 */
public class LogCommandListener extends ListenerAdapter {

    private static final Pattern DATE_PATTERN =
            Pattern.compile("^\\d{2}\\.\\d{2}\\.\\d{4}$");
    private static final String SELECT_ID_PREFIX = "log_select:";

    private final JavaPlugin plugin;

    public LogCommandListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // ─────────────────────────────────────────
    // Шаг 1: /log <дата>
    // ─────────────────────────────────────────

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        // ── /loadlog ──────────────────────────────────────────────────────────
        if (event.getName().equals("loadlog")) {
            handleLoadLog(event);
            return;
        }

        if (!event.getName().equals("log")) return;

        if (event.getGuild() == null || event.getMember() == null) {
            event.reply("❌ Команда доступна только на сервере.")
                    .setEphemeral(true).queue();
            return;
        }

        // Проверка роли
        if (!hasAllowedRole(event.getMember())) {
            event.reply("🚫 У вас нет прав для использования этой команды.")
                    .setEphemeral(true).queue();
            return;
        }

        String date = event.getOption("date") != null
                ? event.getOption("date").getAsString().trim()
                : "";

        if (!DATE_PATTERN.matcher(date).matches()) {
            event.reply("❌ Неверный формат даты. Пример: `/log 01.03.2026`")
                    .setEphemeral(true).queue();
            return;
        }

        List<String> available = getAvailableDirectories(date);

        if (available.isEmpty()) {
            event.reply("📭 Логов за **" + date + "** не найдено ни в одной директории.")
                    .setEphemeral(true).queue();
            return;
        }

        List<SelectOption> options = new ArrayList<>();
        for (String dir : available) {
            String emoji = dir.equals("main") ? "📋" : "📁";
            options.add(SelectOption.of(emoji + " " + dir, dir));
        }

        StringSelectMenu menu = StringSelectMenu
                .create(SELECT_ID_PREFIX + date)
                .setPlaceholder("Выберите директорию...")
                .addOptions(options)
                .build();

        event.reply("📅 Логи за **" + date + "** — выберите директорию:")
                .addActionRow(menu)
                .setEphemeral(true)
                .queue();
    }

    // ─────────────────────────────────────────
    // /loadlog — принудительный flush
    // ─────────────────────────────────────────

    private void handleLoadLog(SlashCommandInteractionEvent event) {
        if (event.getGuild() == null || event.getMember() == null) {
            event.reply("❌ Команда доступна только на сервере.")
                    .setEphemeral(true).queue();
            return;
        }

        if (!hasAllowedRole(event.getMember())) {
            event.reply("🚫 У вас нет прав для использования этой команды.")
                    .setEphemeral(true).queue();
            return;
        }

        event.deferReply().queue();

        // Flush — файловые операции, делаем асинхронно
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            LogManager.flushAll();
            plugin.getLogger().info("[Discord] " + event.getUser().getName() + " выполнил /loadlog");
            event.getHook()
                    .sendMessage("✅ Логи сохранены в файлы! Теперь можешь использовать `/log` для получения актуальных данных.")
                    .queue();
        });
    }

    // ─────────────────────────────────────────
    // Шаг 2: Пользователь выбрал директорию
    // ─────────────────────────────────────────

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        if (!event.getComponentId().startsWith(SELECT_ID_PREFIX)) return;

        String date = event.getComponentId().substring(SELECT_ID_PREFIX.length());
        String selectedDir = event.getValues().get(0);

        File logFile = new File(plugin.getDataFolder(), "logs/" + selectedDir + "/" + date + ".txt");

        if (!logFile.exists()) {
            event.reply("❌ Файл `" + selectedDir + "/" + date + ".txt` не найден.")
                    .setEphemeral(true).queue();
            return;
        }

        event.deferReply().queue();

        try {
            FileUpload upload = FileUpload.fromData(logFile, selectedDir + "_" + date + ".txt");

            event.getHook()
                    .sendMessage("📁 **" + selectedDir + "** | 📅 **" + date + "**")
                    .addFiles(upload)
                    .queue(
                            ok  -> plugin.getLogger().info(
                                    "[Discord] " + event.getUser().getName()
                                            + " получил лог " + selectedDir + "/" + date),
                            err -> event.getHook()
                                    .sendMessage("❌ Не удалось отправить файл: " + err.getMessage())
                                    .queue()
                    );
        } catch (Exception e) {
            plugin.getLogger().severe("[Discord] Ошибка отправки файла: " + e.getMessage());
            event.getHook().sendMessage("❌ Внутренняя ошибка.").queue();
        }
    }

    private List<String> getAvailableDirectories(String date) {
        List<String> result = new ArrayList<>();
        File logsRoot = new File(plugin.getDataFolder(), "logs");
        if (!logsRoot.exists() || !logsRoot.isDirectory()) return result;

        File[] subdirs = logsRoot.listFiles(File::isDirectory);
        if (subdirs == null) return result;

        for (File dir : subdirs) {
            if (dir.getName().equals("main")) {
                File f = new File(dir, date + ".txt");
                if (f.exists()) result.add(0, "main");
                break;
            }
        }
        for (File dir : subdirs) {
            if (dir.getName().equals("main")) continue;
            File f = new File(dir, date + ".txt");
            if (f.exists()) result.add(dir.getName());
        }
        return result;
    }

    private boolean hasAllowedRole(Member member) {
        String roleId = plugin.getConfig().getString("discord.allowed-role-id", "");
        if (roleId.isEmpty()) {
            plugin.getLogger().warning("[Discord] allowed-role-id не задан! /log доступен всем.");
            return true;
        }
        return member.getRoles().stream()
                .map(Role::getId)
                .anyMatch(id -> id.equals(roleId));
    }
}