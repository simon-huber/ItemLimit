package de.ibhh.ItemLimit;

import de.ibhh.ItemLimit.Exceptions.NotEnabledException;
import de.ibhh.ItemLimit.Permissions.PermissionsUtility;
import de.ibhh.ItemLimit.Tools.Utilities;
import de.ibhh.ItemLimit.commands.ItemLimitCommand;
import de.ibhh.ItemLimit.config.ConfigurationHandler;
import de.ibhh.ItemLimit.listeners.ItemLimitListener;
import de.ibhh.ItemLimit.logger.LoggerUtility;
import de.ibhh.ItemLimit.tabcompleters.ItemLimitCommandTab;
import java.util.ArrayList;
import java.util.List;
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
    private Utilities utilities_reload = null;
    //CommandList
    private List<String> Command_args = new ArrayList<String>();

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void onEnable() {
        super.onEnable();
        plugin = this;
        //Adding commands
        Command_args.add("about");
        Command_args.add("help");
        Command_args.add("reload");
        getLoggerUtility();
        getConfigHandler().onStart();
        getPermissionsUtility();
        getLoggerUtility().log("ItemLimit loaded.", LoggerUtility.Level.INFO);
        plugin.getCommand("ItemLimit").setExecutor(new ItemLimitCommand(plugin));
        plugin.getCommand("ItemLimit").setTabCompleter(new ItemLimitCommandTab(plugin));
    }

    @Override
    public void onDisable() {
        super.onDisable(); //To change body of generated methods, choose Tools | Templates.
    }

    public static ItemLimit getPlugin() {
        return plugin;
    }

    public List<String> getCommand_args() {
        return Command_args;
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

    public Utilities getUtilities() {
        if (utilities_reload == null) {
            utilities_reload = new Utilities(plugin);
        }
        return utilities_reload;
    }
}
