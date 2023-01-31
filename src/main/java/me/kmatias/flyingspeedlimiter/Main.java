package me.kmatias.flyingspeedlimiter;

import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.NumberConversions;

public final class Main extends JavaPlugin implements Listener {

    double hspeed;
    double vspeed;
    boolean limith;
    boolean limitv;

    @Override
    public void onEnable() {
        // Plugin startup logic
        FileConfiguration config = this.getConfig();
        hspeed = config.getDouble("max-h-speed");
        vspeed = config.getDouble("max-v-speed");

        limith = hspeed > 0;
        limitv = vspeed > 0;

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!event.getPlayer().isInsideVehicle() && event.getPlayer().getGameMode() == GameMode.SURVIVAL || event.getPlayer().getGameMode() == GameMode.SPECTATOR) {
            double hDist = Math.sqrt(NumberConversions.square(event.getFrom().getX() - event.getTo().getX()) +
                    NumberConversions.square(event.getFrom().getZ() - event.getTo().getZ()));
            double vDist = Math.abs(event.getFrom().getY() - event.getTo().getY());

            if (limith && hDist > hspeed) {
                event.setCancelled(true);
            }

            if (limitv && vDist > vspeed) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.saveDefaultConfig();
    }
}
