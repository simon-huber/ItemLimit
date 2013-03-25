package de.ibhh.ItemLimit.config;

import de.ibhh.ItemLimit.Exceptions.NotEnabledException;
import de.ibhh.ItemLimit.ItemLimit;
import de.ibhh.ItemLimit.logger.LoggerUtility;
import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 *
 * @author ibhh
 */
public class ConfigurationHandler {

    private YamlConfiguration language_config;
    private ItemLimit plugin;

    /**
     * Creates a new ConfigurationHandler
     *
     * @param plugin Needed for saving configs
     */
    public ConfigurationHandler(ItemLimit plugin) {
        this.plugin = plugin;
    }

    /**
     * Returns the current language configuration
     *
     * @return YamlConfiguration
     */
    public YamlConfiguration getLanguage_config() {
        return language_config;
    }

    /**
     *
     * @return plugin.getConifg();
     */
    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    /**
     * Called on start
     *
     * @return true if config was successfully loaded, false if it failed;
     */
    public boolean onStart() {
        //loading main config
        try {
            plugin.getConfig().options().copyDefaults(true);
            plugin.saveConfig();
            plugin.reloadConfig();
            plugin.getLoggerUtility().log("Config loaded", LoggerUtility.Level.DEBUG);
        } catch (Exception e) {
            plugin.getLoggerUtility().log("Cannot create config!", LoggerUtility.Level.ERROR);
            e.printStackTrace();
            plugin.onDisable();
        }
        createLanguageConfig("de");
        return true;
    }

    /**
     * Creates the language config and added defaults
     */
    private void createLanguageConfig(String language) {
        File folder = new File(plugin.getDataFolder() + File.separator);
        folder.mkdirs();
        File configl = new File(plugin.getDataFolder() + File.separator + "language_" + language + ".yml");
        if (!configl.exists()) {
            try {
                configl.createNewFile();
            } catch (IOException ex) {
                plugin.getLoggerUtility().log("Couldnt create new config file!", LoggerUtility.Level.ERROR);
            }
        }
        language_config = YamlConfiguration.loadConfiguration(configl);
        if (language.equalsIgnoreCase("de")) {
            //permission output
            language_config.addDefault("permission.error", "Wir haben ein Problem! Dies darfst Du nicht machen!");
            
            //reload command
            language_config.addDefault("commands.reload.name", "reload");
            language_config.addDefault("commands.reload.permission", "ItemLimit.reload");
            language_config.addDefault("commands.reload.description", "Laedt das Plugin neu");
            language_config.addDefault("commands.reload.usage", "/ItemLimit reload");
            //relaod output
            language_config.addDefault("commands.reload.message", "Neu geladen!");
            //About
            language_config.addDefault("commands.about.name", "about");
            language_config.addDefault("commands.about.permission", "ItemLimit.about");
            language_config.addDefault("commands.about.description", "Kurze Info ueber das Plugin.");
            language_config.addDefault("commands.about.usage", "/ItemLimit about");
            //Help
            language_config.addDefault("commands.help.name", "help");
            language_config.addDefault("commands.help.permission", "ItemLimit.help");
            language_config.addDefault("commands.help.description", "Hilfe des Plugins.");
            language_config.addDefault("commands.help.usage", "/ItemLimit help");
        } else {
            language_config.addDefault("permission.error", "we have a problem! You are not allowed to do this!");
            //reload command
            language_config.addDefault("commands.reload.name", "reload");
            language_config.addDefault("commands.reload.permission", "ItemLimit.reload");
            language_config.addDefault("commands.reload.description", "Reloads the plugin.");
            language_config.addDefault("commands.reload.usage", "/ItemLimit reload");
            language_config.addDefault("commands.reload.message", "Reloaded!");
            //About
            language_config.addDefault("commands.about.name", "about");
            language_config.addDefault("commands.about.permission", "ItemLimit.about");
            language_config.addDefault("commands.about.description", "About the plugin.");
            language_config.addDefault("commands.about.usage", "/ItemLimit about");
            //Help
            language_config.addDefault("commands.help.name", "help");
            language_config.addDefault("commands.help.permission", "ItemLimit.help");
            language_config.addDefault("commands.help.description", "Help of the plugin");
            language_config.addDefault("commands.help.usage", "/ItemLimit help");
        }
        try {
            language_config.options().copyDefaults(true);
            language_config.save(configl);
        } catch (IOException ex) {
            ex.printStackTrace();
            plugin.getLoggerUtility().log("Couldnt save language config!", LoggerUtility.Level.ERROR);
        }
        File configfile = new File(plugin.getDataFolder() + File.separator + "language_" + plugin.getConfig().getString("language") + ".yml");
        try {
            language_config = YamlConfiguration.loadConfiguration(configfile);
        } catch (Exception e) {
            e.printStackTrace();
            plugin.getLoggerUtility().log("Couldnt load language config!", LoggerUtility.Level.ERROR);
            plugin.getConfig().set("language", "en");
            plugin.saveConfig();
            plugin.onDisable();
            return;
        }
        plugin.getLoggerUtility().log("language config loaded", LoggerUtility.Level.DEBUG);
    }
}
