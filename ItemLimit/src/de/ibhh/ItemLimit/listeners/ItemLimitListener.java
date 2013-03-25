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
    
    private ItemLimit plugin;

    private HashMap<String, Block> openchests = new HashMap<String, Block>();
    private HashMap<String, Block> openenderchests = new HashMap<String, Block>();
    private HashMap<Material, Integer> limitschest = new HashMap<Material, Integer>();
    private HashMap<Material, Integer> limitsEnderchest = new HashMap<Material, Integer>();

    public ItemLimitListener(ItemLimit plugin) {
        this.plugin = plugin;
        List<String> list = (List<String>) plugin.getConfigHandler().getConfig().getList("ChestIDs");
        Iterator<String> iter = list.iterator();
        while (iter.hasNext()) {
            String[] id = iter.next().split(":");
            try {
                limitschest.put(Material.getMaterial(Integer.parseInt(id[0])), Integer.parseInt(id[1]));
            } catch (NumberFormatException e) {
                plugin.getLoggerUtility().log("Please check your config.yml: " + e.getMessage(), LoggerUtility.Level.ERROR);
            }
        }
        list = (List<String>) plugin.getConfigHandler().getConfig().getList("EnderChestIDs");
        iter = list.iterator();
        while (iter.hasNext()) {
            String[] id = iter.next().split(":");
            try {
                limitsEnderchest.put(Material.getMaterial(Integer.parseInt(id[0])), Integer.parseInt(id[1]));
            } catch (NumberFormatException e) {
                plugin.getLoggerUtility().log("Please check your config.yml: " + e.getMessage(), LoggerUtility.Level.ERROR);
            }
        }
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChestRightClick(PlayerInteractEvent event) {
        if (event.hasBlock()) {
            if(plugin.getConfigHandler().getConfig().getBoolean("debug")) {
                plugin.getLoggerUtility().log("Rightclick", LoggerUtility.Level.DEBUG);
            }
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                final Block eventBlock = event.getClickedBlock();
                final Player player = event.getPlayer();
                if (plugin.getConfigHandler().getConfig().getBoolean("limitChest")) {
                    if (eventBlock.getState() instanceof Chest) {
                        plugin.getLoggerUtility().log("limit chest: true", LoggerUtility.Level.DEBUG);
                        openchests.put(player.getName(), eventBlock);
                    } 
                } else if (plugin.getConfigHandler().getConfig().getBoolean("limitEnderChest")) {
                    if (eventBlock.getState() instanceof EnderChest) {
                        plugin.getLoggerUtility().log("Limit enderchest: true", LoggerUtility.Level.DEBUG);
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
            plugin.getLoggerUtility().log("Inv click", LoggerUtility.Level.DEBUG);
            if (openchests.containsKey(player.getName())) {
                plugin.getLoggerUtility().log("Player " + player.getName() + " clicked on " + event.getInventory().getType().name() + "!", LoggerUtility.Level.DEBUG);
                if (event.getInventory().getType().equals(InventoryType.CHEST)) {
                    if (event.getCurrentItem() == null) {
                        return;
                    }
                    if ((limitschest.containsKey(event.getCurrentItem().getType()))) {
                        if (limitschest.get(event.getCurrentItem().getType()) < this.countChest(player, event.getCurrentItem().getType())) {
                            event.setCancelled(true);
                            plugin.getLoggerUtility().log(player, "Zu viele Items des Materials in der Kiste", LoggerUtility.Level.ERROR);
                            return;
                        }
                    }

                    if (event.isShiftClick() && event.getCurrentItem().getAmount() > limitschest.get(event.getCurrentItem().getType())) {
                        event.setCancelled(true);
                        plugin.getLoggerUtility().log(player, "Zu viele Items des Materials in der Kiste", LoggerUtility.Level.ERROR);
                        return;
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
            System.out.println("[" + plugin.getName() + "] Error: Uncatched Exeption!");
        }
    }

    private int countChest(Player player, Material material) {
        int a = 0;
        for (ItemStack i : ((Chest) openchests.get(player.getName())).getBlockInventory().getContents()) {
            if ((i == null) || (!i.getType().equals(material))) {
                continue;
            }
            a++;
        }
        return a;
    }

    private int countEnderChest(Player player, Material material) {
        int a = 0;
        for (ItemStack i : ((Chest) openenderchests.get(player.getName())).getBlockInventory().getContents()) {
            if ((i == null) || (!i.getType().equals(material))) {
                continue;
            }
            a++;
        }
        return a;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (openchests.containsKey(event.getPlayer().getName())) {
            plugin.getLoggerUtility().log("Removing", LoggerUtility.Level.DEBUG);
            openchests.remove(event.getPlayer().getName());
        } else if (openenderchests.containsKey(event.getPlayer().getName())) {
            plugin.getLoggerUtility().log("Removing", LoggerUtility.Level.DEBUG);
            openenderchests.remove(event.getPlayer().getName());
        }
    }
}
