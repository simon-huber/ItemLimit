package de.ibhh.ItemLimit.commands;

import de.ibhh.ItemLimit.ItemLimit;
import de.ibhh.ItemLimit.logger.LoggerUtility;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author ibhh
 */
public class ItemLimitCommand implements CommandExecutor {

    private ItemLimit plugin;

    public ItemLimitCommand(ItemLimit plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.isRegistered()) {
            if (cmd.getName().equalsIgnoreCase("ItemLimit")) {
                if (cs instanceof Player) {
                    Player player = (Player) cs;
                    if (args.length > 0) {
                        if (args.length == 1) {
                            if (args[0].equalsIgnoreCase(plugin.getConfigHandler().getLanguage_config().getString("commands.about.name"))) {
                                if (plugin.getPermissionsUtility().checkpermissions(player, plugin.getConfigHandler().getLanguage_config().getString("commands.about.permission"))) {
                                    plugin.getLoggerUtility().log(player, "ItemLimit " + plugin.getDescription().getVersion() + " by ibhh", LoggerUtility.Level.INFO);
                                }
                                return true;
                            } else if (args[0].equalsIgnoreCase(plugin.getConfigHandler().getLanguage_config().getString("commands.reload.name"))) {
                                if (plugin.getPermissionsUtility().checkpermissions(player, plugin.getConfigHandler().getLanguage_config().getString("commands.reload.permission"))) {
                                    plugin.reload(player);
                                }
                                return true;
                            }
                        }
                    } else {
                        //Call help
                        return false;
                    }
                } else {
                    plugin.reload();
                    return true;
                }
            }
        }
        return false;
    }
}
