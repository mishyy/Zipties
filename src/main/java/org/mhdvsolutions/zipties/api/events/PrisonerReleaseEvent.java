package org.mhdvsolutions.zipties.api.events;

import org.bukkit.entity.Player;
import org.mhdvsolutions.zipties.api.ReleaseType;

public final class PrisonerReleaseEvent extends PrisonerEvent {

    private final ReleaseType type;

    /**
     * Called when a prisoner is released from their cuffs (or if they escape from cuffs)
     *
     * @param prisoner   escaping/being released
     * @param releasedBy released/broken out by
     * @param type       whether the released was caused due to release, etc.
     */
    public PrisonerReleaseEvent(Player prisoner, Player releasedBy, ReleaseType type) {
        super(prisoner, releasedBy);
        this.type = type;
    }

    public ReleaseType getType() {
        return type;
    }

}
