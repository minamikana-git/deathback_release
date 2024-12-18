package net.hotamachisubaru;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Deathback extends JavaPlugin implements Listener, CommandExecutor {
    private Map<UUID, Location> deathLocations;

    public Deathback() {
        this.deathLocations = new HashMap<>();
    }

    @Override
    public void onEnable() {
        this.deathLocations = new HashMap<>();
        registerListeners();
        registerCommands();
    }

    private void registerCommands() {
        Objects.requireNonNull(this.getCommand("back")).setExecutor(this);
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location deathLocation = player.getLocation();
        this.deathLocations.put(player.getUniqueId(), deathLocation);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player player) {
            if (cmd.getName().equalsIgnoreCase("back")) {
                if (player.hasPermission("back.teleport")) {
                    Location deathLocation = this.deathLocations.get(player.getUniqueId());
                    if (deathLocation != null) {
                        player.teleport(deathLocation);
                        player.sendMessage("§aあなたは最後に死んだ場所にテレポートしました。");
                    } else {
                        player.sendMessage("§cあなたが最後に死んだ場所は見つかりませんでした。");
                    }
                } else {
                    player.sendMessage("§7このコマンドを使用する権限がありません");
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDisable() {
        this.deathLocations.clear();
    }
}
