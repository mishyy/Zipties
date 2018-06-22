/*
 * Zipties - Player restraint system.
 * Copyright (c) 2018, Mitchell Cook <https://github.com/Mishyy>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
