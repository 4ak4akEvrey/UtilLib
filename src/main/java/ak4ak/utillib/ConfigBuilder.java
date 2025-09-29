package ak4ak.utillib;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ConfigBuilder {
    private final JavaPlugin plugin;
    private File configFile;
    private FileConfiguration config;

    public ConfigBuilder(JavaPlugin plugin, String configName) {
        this.plugin = plugin;
        // Создаем директорию плагина, если её нет
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs(); // Создаем папку плагина
        }

        // Создаем файл конфигурации
        this.configFile = new File(plugin.getDataFolder(), configName + ".yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile(); // Создаем файл
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create config file: " + configFile.getName());
                e.printStackTrace();
            }
        }

        // Загружаем конфигурацию
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    public ConfigBuilder(JavaPlugin plugin, String folderName, String configName) {
        this.plugin = plugin;

        // Создаем папку для дополнительных файлов внутри папки плагина
        File folder = new File(plugin.getDataFolder(), folderName);
        if (!folder.exists()) {
            folder.mkdirs(); // Создаем папку, если её нет
        }

        this.configFile = new File(folder, configName + ".yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Could not create config file: " + configFile.getName());
                e.printStackTrace();
            }
        }

        // Загружаем конфигурацию
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    public ConfigBuilder loadConfig(){
        plugin.saveResource(config.getName(),false);
        return this;
    }
    public ConfigBuilder loadConfig(String name){
        plugin.saveResource(name+".yml",false);
        return this;
    }

    public ConfigBuilder set(String path, Object value) {
        config.set(path, value);
        return this;
    }

    public ConfigBuilder setDefault(String path, Object value) {
        if (!config.contains(path)) {
            config.set(path, value);
        }
        return this;
    }

    public ConfigBuilder save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save config to " + configFile);
            e.printStackTrace();

        }
        return this;
    }


    public String getString(String path) {
        return this.config.getString(path);
    }

    public int getInt(String path) {
        return this.config.getInt(path);
    }

    public boolean contains(String path) {
        return this.config.contains(path);
    }
    public FileConfiguration buld(){
        return this.config;
    }
}