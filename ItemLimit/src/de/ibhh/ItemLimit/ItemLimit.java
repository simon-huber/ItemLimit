package de.ibhh.ItemLimit;

import de.ibhh.ItemLimit.Exceptions.NotEnabledException;
import de.ibhh.ItemLimit.Permissions.PermissionsUtility;
import de.ibhh.ItemLimit.config.ConfigurationHandler;
import de.ibhh.ItemLimit.listeners.ItemLimitListener;
import de.ibhh.ItemLimit.logger.LoggerUtility;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Plugin to limit the store of items in chests
 *
 * @author ibhh
 */
public class ItemLimit extends JavaPlugin {

    private static ItemLimit plugin = null;
    //Tools
    private LoggerUtility loggerUtility = null;
    private ConfigurationHandler configurationHandler = null;
    private PermissionsUtility permissionsUtility = null;
    private ItemLimitListener itemLimitListener = null;

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        plugin = this;
        getLoggerUtility();
        getConfigHandler().onStart();
        getPermissionsUtility();
        getLoggerUtility().log("ItemLimit loaded.", LoggerUtility.Level.INFO);
    }

    @Override
    public void onDisable() {
        super.onDisable(); //To change body of generated methods, choose Tools | Templates.
    }

    public static ItemLimit getPlugin() {
        return plugin;
    }

    /**
     * Initializes the logger
     *
     * @return LoggerUtility
     */
    public LoggerUtility getLoggerUtility() {
        if (loggerUtility == null) {
            try {
                loggerUtility = new LoggerUtility();
            } catch (NotEnabledException ex) {
                Logger.getLogger(ItemLimit.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return loggerUtility;
    }

    /**
     * Initilizes the config and configurationhandler
     *
     * @return
     */
    public ConfigurationHandler getConfigHandler() {
        if (configurationHandler == null) {
            try {
                configurationHandler = new ConfigurationHandler();
            } catch (NotEnabledException ex) {
                Logger.getLogger(ItemLimit.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return configurationHandler;
    }

    /**
     * Initilizes the permissions utility
     *
     * @return
     */
    public PermissionsUtility getPermissionsUtility() {
        if (permissionsUtility == null) {
            try {
                permissionsUtility = new PermissionsUtility();
            } catch (NotEnabledException ex) {
                Logger.getLogger(ItemLimit.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return permissionsUtility;
    }
    
    public ItemLimitListener getItemLimitListener() {
        if (itemLimitListener == null) {
            try {
                itemLimitListener = new ItemLimitListener();
            } catch (NotEnabledException ex) {
                Logger.getLogger(ItemLimit.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return itemLimitListener;
    }
}
