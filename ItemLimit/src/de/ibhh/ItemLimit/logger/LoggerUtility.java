package de.ibhh.ItemLimit.logger;

import de.ibhh.ItemLimit.Exceptions.NotEnabledException;
import de.ibhh.ItemLimit.ItemLimit;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author ibhh
 */
public class LoggerUtility {

    private boolean debugfile;
    private boolean debug;
    private String Prefix;
    private boolean usePrefix;
    public ChatColor PrefixColor, TextColor;

    public enum Level {
        DEBUG, INFO, SEVERE, WARNING, ERROR;
    }

    public LoggerUtility() throws NotEnabledException {
        if(ItemLimit.getPlugin() == null) {
            throw new NotEnabledException("Could not start logger because the plugin is NOT enabled!");
        }
        debugfile = ItemLimit.getPlugin().getConfig().getBoolean("debugfile");
        debug = ItemLimit.getPlugin().getConfig().getBoolean("debug");
        Prefix = ItemLimit.getPlugin().getConfig().getString("Prefix");
        usePrefix = ItemLimit.getPlugin().getConfig().getBoolean("UsePrefix");
        loadcolors();
    }

    private void loadcolors() {
        PrefixColor = ChatColor.getByChar(ItemLimit.getPlugin().getConfig().getString("PrefixColor"));
        TextColor = ChatColor.getByChar(ItemLimit.getPlugin().getConfig().getString("TextColor"));
    }

    public void log(String msg, Level TYPE) {
        try {
            if (TYPE.equals(Level.WARNING) || TYPE.equals(Level.ERROR)) {
                System.err.println("[" + ItemLimit.getPlugin().getName() + "] " + TYPE.name() + ": " + msg);
                Bukkit.broadcast(PrefixColor + "[" + Prefix + "]" + ChatColor.RED  + " " +  TYPE.name() + ": " + TextColor + msg, "Paypassage.log");
                if (debugfile) {
                    this.log("Error: " + msg);
                }
            } else if (TYPE.equals(Level.DEBUG)) {
                if (debug) {
                    System.out.println("[" + Prefix + "]" + " Debug: " + msg);
                    Bukkit.broadcast(PrefixColor + "[" + Prefix + "]" + " Debug: " + TextColor + msg, "Paypassage.log");
                }
                if (debugfile) {
                    this.log("Debug: " + msg);
                }
            } else {
                System.out.println(Prefix + msg);
                Bukkit.broadcast(PrefixColor + "[" + Prefix + "]" + " " + TextColor + msg, "Paypassage.log");
                if (debugfile) {
                    this.log(msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[" + ItemLimit.getPlugin().getName() + "] Error: Uncatched Exeption!");
        }
    }

    public void log(Player p, String msg, Level TYPE) {
        try {
            String playername = p.getName();
            if (TYPE.equals(Level.WARNING) || TYPE.equals(Level.ERROR)) {
                if (usePrefix) {
                    p.sendMessage(PrefixColor + "[" + Prefix + "]" + ChatColor.RED  + " " +  TYPE.name() + ": " + TextColor + msg);
                    if (debugfile) {
                        this.log("Player: " + playername + " " +  TYPE.name() + ": " + msg);
                    }
                } else {
                    p.sendMessage(ChatColor.RED +  TYPE.name() + ": " + TextColor + msg);
                    if (debugfile) {
                        this.log("Player: " + playername + " " +  TYPE.name() + ": " + msg);
                    }
                }
            } else {
                if (usePrefix) {
                    p.sendMessage(PrefixColor + "[" + Prefix + "]" + " " + TextColor + msg);
                    if (debugfile) {
                        this.log("Player: " + playername + " Msg: " + msg);
                    }
                } else {
                    p.sendMessage(TextColor + msg);
                    if (debugfile) {
                        this.log("Player: " + playername + " Msg: " + msg);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("[" + ItemLimit.getPlugin().getName() + "] Error: Uncatched Exeption!");
        }
    }

    public void log(String in) {
        Date now = new Date();
        String Stream = now.toString();
        String path = ItemLimit.getPlugin().getDataFolder().toString() + File.separator + "debugfiles" + File.separator;
        File directory = new File(path);
        directory.mkdirs();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd 'at' HH");
        File file = new File(path + "debug-" + ft.format(now) + ".txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
        try {
            // Create file
            FileWriter fstream = new FileWriter(file, true);
            PrintWriter out = new PrintWriter(fstream);
            out.println("[" + Stream + "] " + in);
            //Close the output stream
            out.close();
        } catch (Exception e) {//Catch exception if any
            System.out.println("Error: " + e.getMessage());
        }
    }
}
