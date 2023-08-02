package net.exotia.developer.kit.core.commands;

import dev.rollczi.litecommands.LiteCommandsBuilder;
import dev.rollczi.litecommands.bukkit.LiteBukkitFactory;
import dev.rollczi.litecommands.bukkit.tools.BukkitOnlyPlayerContextual;
import dev.rollczi.litecommands.bukkit.tools.BukkitPlayerArgument;
import net.exotia.developer.kit.core.commands.handlers.InvalidCommandUsageHandler;
import net.exotia.developer.kit.core.commands.handlers.UnauthorizedCommandHandler;
import net.exotia.developer.kit.core.configuration.sections.LiteCommandsSection;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class CommandsFactory {
    public static LiteCommandsBuilder<CommandSender> create(Plugin plugin, LiteCommandsSection messages) {
        return LiteBukkitFactory.builder(plugin.getServer(), plugin.getName())
                .argument(Player.class, new BukkitPlayerArgument<>(plugin.getServer(), messages.getPlayerIsOffline()))
                .contextualBind(Player.class, new BukkitOnlyPlayerContextual<>(messages.getCommandOnlyForPlayers()))

                .invalidUsageHandler(new InvalidCommandUsageHandler(messages))
                .permissionHandler(new UnauthorizedCommandHandler(messages));
    }
}
