package org.mhdvsolutions.zipties.utils;

import org.mhdvsolutions.zipties.Zipties;

public enum Message {

    PREFIX {
        @Override
        public String toString() {
            return Zipties.getPlugin().getConfig().getString("messages.prefix");
        }
    },
    COMMANDS_HELP,
    COMMANDS_ZIPTIES,
    COMMANDS_CUTTERS,
    COMMANDS_PLAYER,
    COMMANDS_PERMISSION,

    RESTRAINED_ALREADY,
    RESTRAINED_SELF,
    RESTRAINED_OTHER,

    RELEASED_RESTRAINER,
    RELEASED_PRISONER,

    ESCAPED_RESTRAINER,
    ESCAPED_PRISONER,
    ESCAPED_FREE,
    ESCAPED_ALMOST,
    ;

    private final String path;

    Message() {
        this.path = name().toLowerCase().replace("_", ".");
    }

    @Override
    public String toString() {
        return Zipties.getPlugin().getConfig().getString("messages." + path).replace("%prefix%", PREFIX.toString());
    }

}
