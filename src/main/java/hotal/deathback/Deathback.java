package org.hotal.deathback;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Deathback extends JavaPlugin implements Listener {
    private Map<UUID, Location> deathLocations;

    public Deathback() {
    }

    public void onEnable() {
        this.deathLocations = new HashMap();
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    public void onDisable() {
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location deathLocation = player.getLocation();
        this.deathLocations.put(player.getUniqueId(), deathLocation);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player player) {
            if (cmd.getName().equalsIgnoreCase("dback")) {
                if (player.hasPermission("dback.teleport")) {
                    Location deathLocation = (Location)this.deathLocations.get(player.getUniqueId());
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
}
