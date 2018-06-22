package org.mhdvsolutions.zipties.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

abstract class PrisonerEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlerList = new HandlerList();
    private final Player other;
    private boolean cancelled = false;

    PrisonerEvent(final Player prisoner, final Player other) {
        super(prisoner);
        this.other = other;
    }

    @Override
    public String getEventName() {
        return super.getEventName();
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public Player getOther() {
        return other;
    }

}
