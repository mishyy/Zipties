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

import com.google.common.collect.HashBasedTable;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.mhdvsolutions.zipties.Zipties;
import org.mhdvsolutions.zipties.api.ReleaseType;
import org.mhdvsolutions.zipties.api.ZiptiesApi;
import org.mhdvsolutions.zipties.utils.Message;
import org.mhdvsolutions.zipties.utils.Msg;

import java.util.UUID;

public final class PlayerInteract implements Listener {

    private static final HashBasedTable<UUID, UUID, Integer> breakAttempts = HashBasedTable.create();
    private final Zipties plugin;
    private final ZiptiesApi api;

    public PlayerInteract(Zipties plugin) {
        this.plugin = plugin;
        this.api = Zipties.getApi();
    }

    @EventHandler
    public void onRestrain(PlayerInteractAtEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND || !(event.getRightClicked() instanceof Player)) {
            return;
        }

        Player player = event.getPlayer(), prisoner = (Player) event.getRightClicked();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }

        if (itemStack.isSimilar(api.getZiptieItem())
                && player.hasPermission("zipties.use")
                && !prisoner.hasPermission("zipties.bypass")) {
            event.setCancelled(true);
            int amount = itemStack.getAmount();
            if (amount > 1) {
                itemStack.setAmount(amount - 1);
            } else {
                player.getInventory().remove(itemStack);
            }

            api.restrain(player, prisoner);
            return;
        }

        if (itemStack.isSimilar(api.getCuttersItem()) && api.isRestrained(prisoner)) {
            event.setCancelled(true);
            UUID playerId = player.getUniqueId(), prisonerId = prisoner.getUniqueId();
//            if (api.getRestrainedBy(prisonerId).equals(playerId) || player.hasPermission("zipties.release")) {
//                api.release(prisoner, ReleaseType.RELEASE);
//                return;
//            }

            if (!breakAttempts.containsRow(prisonerId)) {
                breakAttempts.put(prisonerId, playerId, 0);
            }

            long count = plugin.getConfig().getInt("cutters.count");
            int attempt = breakAttempts.get(prisonerId, playerId);
            if (attempt < count) {
                if (++attempt == count) {
                    Msg.config(player, Message.ESCAPED_FREE, "%prisoner%", prisoner.getName());
                    api.release(player, ReleaseType.ESCAPE);
                    breakAttempts.rowMap().remove(prisonerId);
                    player.getInventory().remove(itemStack);
                } else {
                    breakAttempts.put(prisonerId, playerId, attempt);
                    Msg.config(player, Message.ESCAPED_ALMOST, "%count%", count, "%completed%", attempt, "%percentage%", (attempt / count) * 100.0);
                }
            }
        }
    }

}
