package net.exotia.developer.kit.core.commands.handlers;

import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.command.permission.RequiredPermissions;
import dev.rollczi.litecommands.handle.PermissionHandler;
import net.exotia.developer.kit.core.configuration.sections.LiteCommandsSection;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

public class UnauthorizedCommandHandler implements PermissionHandler<CommandSender> {
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final LiteCommandsSection messages;

    public UnauthorizedCommandHandler(LiteCommandsSection messages) {
        this.messages = messages;
    }

    @Override
    public void handle(CommandSender sender, LiteInvocation invocation, RequiredPermissions requiredPermissions) {
        sender.sendMessage(this.miniMessage.deserialize(
                this.messages.getUnauthorizedCommand().replace("{permissions}", String.join(", ", requiredPermissions.getPermissions()))
        ));
    }
}