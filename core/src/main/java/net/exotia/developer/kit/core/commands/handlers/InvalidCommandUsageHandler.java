package net.exotia.developer.kit.core.commands.handlers;


import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.InvalidUsageHandler;
import dev.rollczi.litecommands.schematic.Schematic;
import net.exotia.developer.kit.core.configuration.sections.LiteCommandsSection;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

import java.util.List;

public class InvalidCommandUsageHandler implements InvalidUsageHandler<CommandSender> {
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final LiteCommandsSection messages;

    public InvalidCommandUsageHandler(LiteCommandsSection messages) {
        this.messages = messages;
    }

    @Override
    public void handle(CommandSender sender, LiteInvocation invocation, Schematic schematic) {
        List<String> schematics = schematic.getSchematics();

        if (schematics.size() == 1) {
            sender.sendMessage(this.miniMessage.deserialize(this.messages.getInvalidCommandUsage().replace("{command}", schematics.get(0))));
            return;
        }
        sender.sendMessage(this.miniMessage.deserialize(this.messages.getInvalidCommandUsageHeader()));
        for (String sch : schematics) {
            sender.sendMessage(this.miniMessage.deserialize(this.messages.getInvalidCommandUsageBody().replace("{schematic}", sch)));
        }
    }
}