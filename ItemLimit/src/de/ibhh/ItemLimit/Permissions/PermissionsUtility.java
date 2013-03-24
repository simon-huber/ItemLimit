package de.ibhh.ItemLimit.Permissions;

import de.ibhh.ItemLimit.Exceptions.NotEnabledException;
import de.ibhh.ItemLimit.ItemLimit;
import de.ibhh.ItemLimit.logger.LoggerUtility;
import org.bukkit.entity.Player;

public class PermissionsUtility {

    public PermissionsUtility() throws NotEnabledException {
        if (ItemLimit.getPlugin() == null) {
            throw new NotEnabledException("Could not start logger because the plugin is NOT enabled!");
        }
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
            ItemLimit.getPlugin().getLoggerUtility().log("Error on checking permissions!", LoggerUtility.Level.ERROR);
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
        ItemLimit.getPlugin().getLoggerUtility().log(player, player.getName() + " " + ItemLimit.getPlugin().getConfigHandler().getLanguage_config().getString("permission.error") + " (" + action + ")", LoggerUtility.Level.ERROR);
    }

    private void sendGeneralErrorMessage(Player player, Exception e) {
        ItemLimit.getPlugin().getLoggerUtility().log("Error on checking permissions!", LoggerUtility.Level.ERROR);
        ItemLimit.getPlugin().getLoggerUtility().log(player, "Error on checking permissions!", LoggerUtility.Level.ERROR);
    }
}