package org.mhdvsolutions.zipties.listeners;

import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.mhdvsolutions.zipties.Zipties;
import org.mhdvsolutions.zipties.api.ReleaseType;
import org.mhdvsolutions.zipties.api.ZiptiesApi;

public final class Escape implements Listener {

    private final Zipties plugin;
    private final ZiptiesApi api;

    public Escape(Zipties plugin) {
        this.plugin = plugin;
        this.api = Zipties.getApi();
    }

    @EventHandler
    public void onPigLeave(VehicleExitEvent event) {
        if (!(event.getVehicle() instanceof Pig) || !(event.getExited() instanceof Player)) {
            return;
        }

        Pig pig = (Pig) event.getVehicle();
        if (!pig.getName().startsWith("Restraint Pig") || pig.isDead()) {
            return;
        }

        Player player = (Player) event.getExited();
        if (api.isRestrained(player)) {
            event.setCancelled(true);
            System.out.println("Cancelled.");
        }
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        if (!(event.getEntered() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntered();
        if (api.isRestrained(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLeashBreak(PlayerUnleashEntityEvent event) {
        if (!(event.getEntity() instanceof Pig)) {
            return;
        }

        Pig pig = (Pig) event.getEntity();
        if (!pig.getName().startsWith("Restraint Pig") || pig.isDead()) {
            return;
        }

        Player player = event.getPlayer();
        if (api.isRestrained(player)) {
            event.setCancelled(true);
            if (event.getReason() == EntityUnleashEvent.UnleashReason.HOLDER_GONE) {
                api.release(player, ReleaseType.OTHER);
            }
        }
    }

}
