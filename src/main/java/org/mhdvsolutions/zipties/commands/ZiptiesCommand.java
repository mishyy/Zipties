package org.mhdvsolutions.zipties.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mhdvsolutions.zipties.Zipties;
import org.mhdvsolutions.zipties.api.ReleaseType;
import org.mhdvsolutions.zipties.api.ZiptiesApi;
import org.mhdvsolutions.zipties.utils.Message;
import org.mhdvsolutions.zipties.utils.Msg;

public final class ZiptiesCommand implements CommandExecutor {

    private final Zipties plugin;
    private final ZiptiesApi api;

    public ZiptiesCommand(final Zipties plugin) {
        this.plugin = plugin;
        this.api = Zipties.getApi();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            Msg.msg(sender, "%prefix% &cOnly players may use this command.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length == 0) {
            Msg.config(player, Message.COMMANDS_HELP, "%cmd%", label);
            return true;
        }

        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].toLowerCase();
        }

        switch (args[0]) {
            case "zipties":
                player.getInventory().addItem(api.getZiptieItem());
                Msg.config(player, Message.COMMANDS_ZIPTIES);
                return true;

            case "cutters":
                player.getInventory().addItem(api.getCuttersItem());
                Msg.config(player, Message.COMMANDS_CUTTERS);
                return true;

            case "release":
                if (!player.hasPermission("zipties.use")) {
                    Msg.config(player, Message.COMMANDS_PERMISSION);
                    return true;
                }

                if (args.length == 2) {
                    Player prisoner = Bukkit.getPlayer(args[1]);
                    if (prisoner == null || !prisoner.isOnline()) {
                        Msg.config(player, Message.COMMANDS_PLAYER, "%name%", args[0]);
                        return true;
                    }

                    if (api.isRestrained(prisoner) && api.getRestrainedBy(prisoner).equals(player.getUniqueId())) {
                        api.release(prisoner, ReleaseType.RELEASE);
                        return true;
                    }

                    if (!player.hasPermission("zipties.admin")) {
                        Msg.config(player, Message.COMMANDS_PERMISSION);
                        return true;
                    }
                    api.release(prisoner, ReleaseType.OTHER);
                    return true;
                } else {
                    Msg.config(player, Message.COMMANDS_HELP, "%cmd%", label);
                    return true;
                }

            default:
                Msg.config(player, Message.COMMANDS_HELP, "%cmd%", label);
                return true;
        }
    }

}
