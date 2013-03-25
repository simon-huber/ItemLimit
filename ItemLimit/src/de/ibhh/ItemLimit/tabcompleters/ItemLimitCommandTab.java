package de.ibhh.ItemLimit.tabcompleters;

import de.ibhh.ItemLimit.ItemLimit;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

/**
 *
 * @author ibhh
 */
public class ItemLimitCommandTab implements TabCompleter {
    
    private ItemLimit plugin;

    public ItemLimitCommandTab(ItemLimit plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmd, String label, String[] args) {
        if(cmd.isRegistered()) {
            if(cmd.getName().equalsIgnoreCase("ItemLimit")) {
                return plugin.getCommand_args();
            }
        }
        return null;
    }
    
}
