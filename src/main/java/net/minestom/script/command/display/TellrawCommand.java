package net.minestom.script.command.display;

import net.kyori.adventure.text.Component;
import net.minestom.script.command.RichCommand;
import net.minestom.script.command.arguments.ArgumentFlexibleComponent;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;

import java.util.List;

import static net.minestom.server.command.builder.arguments.ArgumentType.Entity;

public class TellrawCommand extends RichCommand {
    public TellrawCommand() {
        super("tellraw");

        setDefaultExecutor((sender, context) ->
                sender.sendMessage(Component.text("Usage: /entity tellraw <targets> <message>")));

        addSyntax((sender, context) -> {
            EntityFinder entityFinder = context.get("targets");
            final Component component = context.get("component");
            final List<Entity> entities = entityFinder.find(sender);
            for (Entity entity : entities) {
                if (entity instanceof Player) {
                    final Player player = (Player) entity;
                    player.sendMessage(component);
                }
            }

            sender.sendMessage(Component.text("Message sent!"));
        }, Entity("targets").onlyPlayers(true), new ArgumentFlexibleComponent("component", true));

    }
}
