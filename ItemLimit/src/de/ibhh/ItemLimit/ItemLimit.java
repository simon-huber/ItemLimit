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
        //Adding commands
        Command_args.add("about");
        Command_args.add("help");
        Command_args.add("reload");
        getLoggerUtility();
        getConfigHandler().onStart();
        getPermissionsUtility();
        getItemLimitListener();
        getUtilities();
        getLoggerUtility().log("ItemLimit loaded.", LoggerUtility.Level.INFO);
        getCommand("ItemLimit").setExecutor(new ItemLimitCommand(this));
        getCommand("ItemLimit").setTabCompleter(new ItemLimitCommandTab(this));
    }

    @Override
    public void onDisable() {
        super.onDisable(); //To change body of generated methods, choose Tools | Templates.
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
            loggerUtility = new LoggerUtility(this);
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
            configurationHandler = new ConfigurationHandler(this);
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
            permissionsUtility = new PermissionsUtility(this);
        }
        return permissionsUtility;
    }

    public ItemLimitListener getItemLimitListener() {
        if (itemLimitListener == null) {
            itemLimitListener = new ItemLimitListener(this);
        }
        return itemLimitListener;
    }

    public Utilities getUtilities() {
        if (utilities_reload == null) {
            utilities_reload = new Utilities(this);
        }
        return utilities_reload;
    }
}
