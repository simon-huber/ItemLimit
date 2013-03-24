package de.ibhh.ItemLimit.listeners;

import de.ibhh.ItemLimit.Exceptions.NotEnabledException;
import de.ibhh.ItemLimit.ItemLimit;
import org.bukkit.event.Listener;

/**
 *
 * @author ibhh
 */
public class ItemLimitListener implements Listener {

    public ItemLimitListener() throws NotEnabledException {
        if (ItemLimit.getPlugin() == null) {
            throw new NotEnabledException("Could not start logger because the plugin is NOT enabled!");
        }
        ItemLimit.getPlugin().getServer().getPluginManager().registerEvents(this, ItemLimit.getPlugin());
    }
}
