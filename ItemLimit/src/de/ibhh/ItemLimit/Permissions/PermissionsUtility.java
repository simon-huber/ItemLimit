package de.ibhh.ItemLimit.Permissions;

import de.ibhh.ItemLimit.ItemLimit;
import de.ibhh.ItemLimit.logger.LoggerUtility;
import org.bukkit.entity.Player;

public class PermissionsUtility {
    
    private ItemLimit plugin;

    public PermissionsUtility(ItemLimit plugin) {
        this.plugin = plugin;
    }

    public boolean checkpermissionssilent(Player player, String action) {
        try {
            if (player.isOp()) {
                return true;
            }
            try {
                return player.hasPermission(action) || player.hasPermission(action.toLowerCase());
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            plugin.getLoggerUtility().log("Error on checking permissions!", LoggerUtility.Level.ERROR);
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkpermissions(Player player, String action) {
        try {
            if (player.isOp()) {
                return true;
            }
            try {
                if (player.hasPermission(action) || player.hasPermission(action.toLowerCase())) {
                    return true;
                } else {
                    sendErrorMessage(player, action);
                    return false;
                }
            } catch (Exception e) {
                sendGeneralErrorMessage(player, e);
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            sendGeneralErrorMessage(player, e);
            e.printStackTrace();
            return false;
        }
    }

    private void sendErrorMessage(Player player, String action) {
        plugin.getLoggerUtility().log(player, player.getName() + " " + plugin.getConfigHandler().getLanguage_config().getString("permission.error") + " (" + action + ")", LoggerUtility.Level.ERROR);
    }

    private void sendGeneralErrorMessage(Player player, Exception e) {
        plugin.getLoggerUtility().log("Error on checking permissions!", LoggerUtility.Level.ERROR);
        plugin.getLoggerUtility().log(player, "Error on checking permissions!", LoggerUtility.Level.ERROR);
    }
}