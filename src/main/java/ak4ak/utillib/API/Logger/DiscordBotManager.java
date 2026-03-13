package ak4ak.utillib.API.Logger;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Управляет жизненным циклом Discord бота.
 * Создаётся в onEnable(), уничтожается в onDisable().
 */
public class DiscordBotManager {

    private JDA jda;
    private final JavaPlugin plugin;

    public DiscordBotManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        String token = plugin.getConfig().getString("discord.token", "");

        if (token.isEmpty() || token.equals("YOUR_BOT_TOKEN_HERE")) {
            plugin.getLogger().warning("[Discord] Токен бота не указан в config.yml! Бот не запущен.");
            return;
        }

        try {
            jda = JDABuilder
                    .createLight(token, GatewayIntent.GUILD_MESSAGES)
                    .addEventListeners(new LogCommandListener(plugin))
                    .build();

            jda.awaitReady();

            String guildId = plugin.getConfig().getString("discord.guild-id", "");
            if (guildId.isEmpty()) return;

            Guild guild = jda.getGuildById(guildId);
            if (guild == null) {
                plugin.getLogger().warning("[Discord] Гильдия с ID " + guildId + " не найдена. Проверьте guild-id в конфиге.");
                return;
            }

            // Обе команды в одном updateCommands — иначе вторая перезапишет первую
            guild.updateCommands()
                    .addCommands(
                            Commands.slash("log", "Получить лог плагина за указанную дату")
                                    .addOption(
                                            OptionType.STRING,
                                            "date",
                                            "Дата в формате ДД.ММ.ГГГГ (например: 01.03.2026)",
                                            true
                                    ),
                            Commands.slash("loadlog", "Сохранить текущие буферы логов в файлы прямо сейчас")
                    )
                    .queue(
                            success -> plugin.getLogger().info("[Discord] Команды /log и /loadlog зарегистрированы!"),
                            error   -> plugin.getLogger().severe("[Discord] Не удалось зарегистрировать команды: " + error.getMessage())
                    );

            plugin.getLogger().info("[Discord] Бот успешно запущен как: " + jda.getSelfUser().getName());

        } catch (Exception e) {
            plugin.getLogger().severe("[Discord] Ошибка при запуске бота: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stop() {
        if (jda != null) {
            jda.shutdown();
            plugin.getLogger().info("[Discord] Бот остановлен.");
        }
    }
    public JDA getJda() { return jda; }
    public boolean isRunning() { return jda != null && jda.getStatus() == JDA.Status.CONNECTED; }
}