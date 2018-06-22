package org.mhdvsolutions.zipties.api.events;

import org.bukkit.entity.Player;

public final class PrisonerRestrainEvent extends PrisonerEvent {

    /**
     * Called when a player is restrained
     *
     * @param player being restrained
     * @param other  player responsible for the restraining
     */
    public PrisonerRestrainEvent(Player player, Player other) {
        super(player, other);
    }

}