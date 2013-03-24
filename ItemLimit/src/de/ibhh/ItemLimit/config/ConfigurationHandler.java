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

    /**
     * Creates a new ConfigurationHandler
     *
     * @param plugin Needed for saving configs
     */
    public ConfigurationHandler() throws NotEnabledException {
        if (ItemLimit.getPlugin() == null) {
            throw new NotEnabledException("Could not start logger because the plugin is NOT enabled!");
        }
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
        return ItemLimit.getPlugin().getConfig();
    }

    /**
     * Called on start
     *
     * @return true if config was successfully loaded, false if it failed;
     */
    public boolean onStart() {
        //loading main config
        try {
            ItemLimit.getPlugin().getConfig().options().copyDefaults(true);
            ItemLimit.getPlugin().saveConfig();
            ItemLimit.getPlugin().reloadConfig();
            ItemLimit.getPlugin().getLoggerUtility().log("Config loaded", LoggerUtility.Level.DEBUG);
        } catch (Exception e) {
            ItemLimit.getPlugin().getLoggerUtility().log("Cannot create config!", LoggerUtility.Level.ERROR);
            e.printStackTrace();
            ItemLimit.getPlugin().onDisable();
        }
        createLanguageConfig("de");
        return true;
    }

    /**
     * Creates the language config and added defaults
     */
    private void createLanguageConfig(String language) {
        File folder = new File(ItemLimit.getPlugin().getDataFolder() + File.separator);
        folder.mkdirs();
        File configl = new File(ItemLimit.getPlugin().getDataFolder() + File.separator + "language_" + language + ".yml");
        if (!configl.exists()) {
            try {
                configl.createNewFile();
            } catch (IOException ex) {
                ItemLimit.getPlugin().getLoggerUtility().log("Couldnt create new config file!", LoggerUtility.Level.ERROR);
            }
        }
        language_config = YamlConfiguration.loadConfiguration(configl);
        if (language.equalsIgnoreCase("de")) {
            //permission output
            language_config.addDefault("permission.error", "Wir haben ein Problem! Dies darfst Du nicht machen!");
        } else {
            language_config.addDefault("permission.error", "we have a problem! You musnt do this!");
        }
        try {
            language_config.options().copyDefaults(true);
            language_config.save(configl);
        } catch (IOException ex) {
            ex.printStackTrace();
            ItemLimit.getPlugin().getLoggerUtility().log("Couldnt save language config!", LoggerUtility.Level.ERROR);
        }
        File configfile = new File(ItemLimit.getPlugin().getDataFolder() + File.separator + "language_" + ItemLimit.getPlugin().getConfig().getString("language") + ".yml");
        try {
            language_config = YamlConfiguration.loadConfiguration(configfile);
        } catch (Exception e) {
            e.printStackTrace();
            ItemLimit.getPlugin().getLoggerUtility().log("Couldnt load language config!", LoggerUtility.Level.ERROR);
            ItemLimit.getPlugin().getConfig().set("language", "en");
            ItemLimit.getPlugin().saveConfig();
            ItemLimit.getPlugin().onDisable();
            return;
        }
        ItemLimit.getPlugin().getLoggerUtility().log("language config loaded", LoggerUtility.Level.DEBUG);
    }
}