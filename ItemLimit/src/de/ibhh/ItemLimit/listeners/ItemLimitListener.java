package de.ibhh.ItemLimit.listeners;

import de.ibhh.ItemLimit.Exceptions.NotEnabledException;
import de.ibhh.ItemLimit.ItemLimit;
import de.ibhh.ItemLimit.logger.LoggerUtility;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.EnderChest;

/**
 *
 * @author ibhh
 */
public class ItemLimitListener implements Listener {

    private HashMap<String, Block> openchests = new HashMap<String, Block>();
    private HashMap<String, Block> openenderchests = new HashMap<String, Block>();
    private HashMap<Material, Integer> limitschest = new HashMap<Material, Integer>();
    private HashMap<Material, Integer> limitsEnderchest = new HashMap<Material, Integer>();

    public ItemLimitListener() throws NotEnabledException {
        if (ItemLimit.getPlugin() == null) {
            throw new NotEnabledException("Could not start logger because the plugin is NOT enabled!");
        }
        List<String> list = (List<String>) ItemLimit.getPlugin().getConfigHandler().getConfig().getList("ChestIDs");
        Iterator<String> iter = list.iterator();
        while (iter.hasNext()) {
            String[] id = iter.next().split(":");
            try {
                limitschest.put(Material.getMaterial(Integer.parseInt(id[0])), Integer.parseInt(id[1]));
            } catch (NumberFormatException e) {
                ItemLimit.getPlugin().getLoggerUtility().log("Please check your config.yml: " + e.getMessage(), LoggerUtility.Level.ERROR);
            }
        }
        list = (List<String>) ItemLimit.getPlugin().getConfigHandler().getConfig().getList("EnderChestIDs");
        iter = list.iterator();
        while (iter.hasNext()) {
            String[] id = iter.next().split(":");
            try {
                limitsEnderchest.put(Material.getMaterial(Integer.parseInt(id[0])), Integer.parseInt(id[1]));
            } catch (NumberFormatException e) {
                ItemLimit.getPlugin().getLoggerUtility().log("Please check your config.yml: " + e.getMessage(), LoggerUtility.Level.ERROR);
            }
        }
        ItemLimit.getPlugin().getServer().getPluginManager().registerEvents(this, ItemLimit.getPlugin());
    }

    @EventHandler
    public void onChestRightClick(PlayerInteractEvent event) {
        if (event.hasBlock()) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                final Block eventBlock = event.getClickedBlock();
                final Player player = event.getPlayer();
                if (ItemLimit.getPlugin().getConfigHandler().getConfig().getBoolean("limitChest")) {
                    if (eventBlock.getState() instanceof Chest) {
                        openchests.put(player.getName(), eventBlock);
                    }
                } else if (ItemLimit.getPlugin().getConfigHandler().getConfig().getBoolean("limitEnderChest")) {
                    if (eventBlock.getState() instanceof EnderChest) {
                        openenderchests.put(player.getName(), eventBlock);
                    }
                }
            }
        }
    }

    @EventHandler
    public void invClick(InventoryClickEvent event) {
        try {
            Player player = (Player) event.getWhoClicked();
            if (openchests.containsKey(player.getName())) {
                ItemLimit.getPlugin().getLoggerUtility().log("Player " + player.getName() + " clicked on " + event.getInventory().getType().name() + "!", LoggerUtility.Level.DEBUG);
                if (event.getInventory().getType().equals(InventoryType.CHEST)) {
                    if (event.getCurrentItem() == null) {
                        return;
                    }
                    if ((limitschest.containsKey(event.getCurrentItem().getType())) && (!event.getCurrentItem().getType().equals(Material.AIR))) {
                        Iterator<Material> itermater = limitschest.keySet().iterator();
                        while (itermater.hasNext()) {
                            Material material = itermater.next();
                            int a = 0;
                            for (ItemStack i : ((Chest) openchests.get(player.getName())).getBlockInventory().getContents()) {
                                if ((i == null) || (!i.getType().equals(material))) {
                                    continue;
                                }
                                a++;
                            }
                            if(limitschest.get(material) < a) {
                                event.setCancelled(true);
                                ItemLimit.getPlugin().getLoggerUtility().log(player, "Zu viele Items des Materials in der Kiste", LoggerUtility.Level.ERROR);
                            }
                        }
                        
                    }

//                    if (((countWrittenBooks(((Chest) this.ChestViewers.get(player)).getInventory()) <= 0) || (!((Chest) this.ChestViewers.get(player)).getInventory().contains(event.getCurrentItem()))) && (countWrittenBooks(((Chest) this.ChestViewers.get(player)).getInventory()) > 0)) {
//                        if (this.plugin.getConfig().getBoolean("useBookandQuill")) {
//                            this.plugin.Logger("UseBooksandQuill = true", "Debug");
//                            if ((!event.getCursor().getType().equals(Material.BOOK_AND_QUILL)) && (!event.getCurrentItem().getType().equals(Material.BOOK_AND_QUILL))) {
//                                this.plugin.Logger("!event.getCursor().getType().equals(Material.BOOK) || !event.getCurrentItem().getType().equals(Material.BOOK)", "Debug");
//                                this.plugin.Logger("Item is " + event.getCurrentItem().getType().name(), "Debug");
//                                this.plugin.PlayerLogger(player, this.plugin.getConfig().getString("Shop.error.wrongItem." + this.plugin.config.language), "Error");
//                                event.setCancelled(true);
//                            }
//                        } else {
//                            this.plugin.PlayerLogger(player, this.plugin.getConfig().getString("Shop.error.onebook." + this.plugin.config.language), "Error");
//                            event.setCancelled(true);
//                        }
//                    } else if ((event.isShiftClick()) && (countWrittenBooks(((Chest) this.ChestViewers.get(player)).getInventory()) > 0)) {
//                        this.plugin.PlayerLogger(player, this.plugin.getConfig().getString("Shop.error.onebook." + this.plugin.config.language), "Error");
//                        event.setCancelled(true);
//                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[" + ItemLimit.getPlugin().getName() + "] Error: Uncatched Exeption!");
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (openchests.containsKey(event.getPlayer().getName())) {
            openchests.remove(event.getPlayer().getName());
        } else if (openenderchests.containsKey(event.getPlayer().getName())) {
            openenderchests.remove(event.getPlayer().getName());
        }
    }
}
