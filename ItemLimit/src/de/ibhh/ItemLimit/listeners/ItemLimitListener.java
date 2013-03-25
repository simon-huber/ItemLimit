package de.ibhh.ItemLimit.listeners;

import de.ibhh.ItemLimit.ItemLimit;
import de.ibhh.ItemLimit.logger.LoggerUtility;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.EnderChest;

/**
 *
 * @author ibhh
 */
public class ItemLimitListener implements Listener {

    private ItemLimit plugin;
    private HashMap<Player, Chest> openchests = new HashMap<Player, Chest>();
    private HashMap<Player, Chest> openenderchests = new HashMap<Player, Chest>();
    private HashMap<Material, Integer> limitschest = new HashMap<Material, Integer>();
    private HashMap<Material, Integer> limitsEnderchest = new HashMap<Material, Integer>();

    public ItemLimitListener(ItemLimit plugin) {
        this.plugin = plugin;
        plugin.getLoggerUtility().log("Scanning Chest", LoggerUtility.Level.DEBUG);
        List<String> list = (List<String>) plugin.getConfigHandler().getConfig().getList("ChestIDs");
        Iterator<String> iter = list.iterator();
        while (iter.hasNext()) {
            String next = iter.next();
            plugin.getLoggerUtility().log("Next: " + next, LoggerUtility.Level.DEBUG);
            String[] id = next.split(":");
            try {
                Material material = Material.matchMaterial(id[0]);
                int count = Integer.parseInt(id[1]);
                if (material == null) {
                    plugin.getLoggerUtility().log("Adding: null ", LoggerUtility.Level.DEBUG);
                    continue;
                }
                limitschest.put(material, count);
                plugin.getLoggerUtility().log("Adding: " + material.name() + " limit: " + count, LoggerUtility.Level.DEBUG);
            } catch (NumberFormatException e) {
                plugin.getLoggerUtility().log("Please check your config.yml: " + e.getMessage(), LoggerUtility.Level.ERROR);
            }
        }
        plugin.getLoggerUtility().log("Scanning EnderChest", LoggerUtility.Level.DEBUG);
        list = (List<String>) plugin.getConfigHandler().getConfig().getList("EnderChestIDs");
        iter = list.iterator();
        while (iter.hasNext()) {
            String next = iter.next();
            plugin.getLoggerUtility().log("Next: " + next, LoggerUtility.Level.DEBUG);
            String[] id = next.split(":");
            try {
                Material material = Material.matchMaterial(id[0]);
                int count = Integer.parseInt(id[1]);
                if (material == null) {
                    plugin.getLoggerUtility().log("Adding: null ", LoggerUtility.Level.DEBUG);
                    continue;
                }
                limitsEnderchest.put(material, count);
                plugin.getLoggerUtility().log("Adding: " + material.name() + " limit: " + count, LoggerUtility.Level.DEBUG);
            } catch (NumberFormatException e) {
                plugin.getLoggerUtility().log("Please check your config.yml: " + e.getMessage(), LoggerUtility.Level.ERROR);
            }
        }
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChestRightClick(PlayerInteractEvent event) {
        if (event.hasBlock()) {
            if (plugin.getConfigHandler().getConfig().getBoolean("debug")) {
                plugin.getLoggerUtility().log("Rightclick", LoggerUtility.Level.DEBUG);
            }
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                final Block eventBlock = event.getClickedBlock();
                final Player player = event.getPlayer();
                if (plugin.getConfigHandler().getConfig().getBoolean("limitChest")) {
                    if (eventBlock.getState() instanceof Chest) {
                        plugin.getLoggerUtility().log("limit chest: true", LoggerUtility.Level.DEBUG);
                        openchests.put(player, (Chest) eventBlock.getState());
                    }
                } else if (plugin.getConfigHandler().getConfig().getBoolean("limitEnderChest")) {
                    if (eventBlock.getState() instanceof EnderChest) {
                        plugin.getLoggerUtility().log("Limit enderchest: true", LoggerUtility.Level.DEBUG);
                        openenderchests.put(player, (Chest) eventBlock.getState());
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
            if (openchests.containsKey(player)) {
                plugin.getLoggerUtility().log("Player " + player.getName() + " clicked on " + event.getInventory().getType().name() + "!", LoggerUtility.Level.DEBUG);
                if (event.getInventory().getType().equals(InventoryType.CHEST)) {
                    if (event.getCurrentItem() == null) {
                        return;
                    } else if (event.getCurrentItem().getType() == null) {
                        return;
                    }
                    if (limitschest.containsKey(event.getCurrentItem().getType())) {
                        if (event.isShiftClick()) {
                            openchests.get(player).update();
                            int items_total = (event.getCurrentItem().getAmount()
                                    + countChest(event.getInventory(), event.getCurrentItem().getType()));
                            plugin.getLoggerUtility().log("Player " + player.getName() + " items_total: " + items_total, LoggerUtility.Level.DEBUG);
                            if (items_total > limitschest.get(event.getCurrentItem().getType())) {
                                event.setCancelled(true);
                                plugin.getLoggerUtility().log(player, "Zu viele Items des Materials in der Kiste, es sind maximal " + limitschest.get(event.getCursor().getType()) + " erlaubt!", LoggerUtility.Level.ERROR);
                                return;
                            }
                        }
                    }
                    if (event.getCursor() != null) {
                        if (limitschest.containsKey(event.getCursor().getType())) {
                            int total = 0;
                            if (event.isLeftClick()) {
                                total = event.getCursor().getAmount() + countChest(event.getInventory(), event.getCursor().getType());
                            } else if (event.isRightClick()) {
                                player.getInventory().addItem(event.getCursor().clone());
                                player.updateInventory();
                                event.setCursor(null);
                                event.setCancelled(true);
                                plugin.getLoggerUtility().log(player, "Funktion ist bei limitierten Items deaktiviert!", LoggerUtility.Level.ERROR);
                            }
                            plugin.getLoggerUtility().log("Player " + player.getName() + " items_total: " + total, LoggerUtility.Level.DEBUG);
                            if (total > limitschest.get(event.getCursor().getType())) {
                                player.getInventory().addItem(event.getCursor().clone());
                                player.updateInventory();
                                plugin.getLoggerUtility().log(player, "Zu viele Items des Materials in der Kiste, es sind maximal " + limitschest.get(event.getCursor().getType()) + " erlaubt!", LoggerUtility.Level.ERROR);
                                event.setCursor(null);
                                event.setCancelled(true);
                                return;
                            }
                        }
                    }
//                    if ((limitschest.containsKey(event.getCurrentItem().getType()))) {
//                        if (limitschest.get(event.getCurrentItem().getType()) < this.countChest(openchests.get(player.getName()).getInventory(), event.getCurrentItem().getType())) {
//                            event.setCancelled(true);
//                            plugin.getLoggerUtility().log(player, "Zu viele Items des Materials in der Kiste", LoggerUtility.Level.ERROR);
//                            return;
//                        }
//                    }

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

    private int countChest(Inventory inv, Material material) {
        int a = 0;
        for (ItemStack i : inv.getContents()) {
            if ((i == null)) {
                continue;
            } else if (!i.getType().equals(material)) {
                continue;
            }
            a++;
        }
        return a;
    }

    private int countEnderChest(Inventory inv, Material material) {
        int a = 0;
        for (ItemStack i : inv.getContents()) {
            if ((i == null)) {
                continue;
            } else if (!i.getType().equals(material)) {
                continue;
            }
            a++;
        }
        return a;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (openchests.containsKey((Player) event.getPlayer())) {
            plugin.getLoggerUtility().log("Removing", LoggerUtility.Level.DEBUG);
            openchests.remove((Player) event.getPlayer());
        } else if (openenderchests.containsKey((Player) event.getPlayer())) {
            plugin.getLoggerUtility().log("Removing", LoggerUtility.Level.DEBUG);
            openenderchests.remove((Player) event.getPlayer());
        }
    }
}
