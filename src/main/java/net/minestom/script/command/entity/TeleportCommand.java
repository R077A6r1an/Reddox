package net.minestom.script.command.entity;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.command.ConsoleSender;
import net.minestom.script.command.RichCommand;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.command.builder.arguments.relative.ArgumentRelativeVec2;
import net.minestom.server.command.builder.arguments.relative.ArgumentRelativeVec3;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.server.utils.entity.EntityFinder;
import net.minestom.server.utils.location.RelativeVec;

import java.util.List;

public class TeleportCommand extends RichCommand {

    private final ArgumentRelativeVec3 location = ArgumentType.RelativeVec3("location");
    private final ArgumentEntity destination = ArgumentType.Entity("destination").singleEntity(true);
    private final ArgumentEntity targets = ArgumentType.Entity("targets");
    private final ArgumentRelativeVec2 direction = ArgumentType.RelativeVec2("direction");

    public TeleportCommand() {
        super("tp");

        setDefaultExecutor(this::usage);

        addSyntax(this::targetToDestinationWithDir, targets, destination, direction);
        addSyntax(this::targetToLocationWithDir, targets, location, direction);

        addSyntax(this::targetToDestination, targets, destination);
        addSyntax(this::targetToLocation, targets, location);

        addSyntax(this::selfToEntity, destination);
        addSyntax(this::selfToPosition, location);
    }

    private void usage(CommandSender sender, CommandContext context) {
        if (sender instanceof Player) {
            sender.sendMessage(Component.text("Usage: /tp <location>"));
            sender.sendMessage(Component.text("Usage: /tp <destination>"));
        }
        sender.sendMessage(Component.text("Usage: /tp <targets> (<destination>|<location>)"));
    }

    private void selfToEntity(CommandSender sender, CommandContext context) {
        if (sender instanceof ConsoleSender) {
            usage(sender, context);
            return;
        }

        EntityFinder entityFinder = context.get(destination);

        List<Entity> entities = entityFinder.find(sender);

        if (entities.size() > 0) {
            teleport(((Player)sender), entities.get(0).getPosition());
            sender.sendMessage(Component.text("Teleport to entity"));
        } else {
            sender.sendMessage(Component.text("No destination found", NamedTextColor.RED));
        }
    }

    private void selfToPosition(CommandSender sender, CommandContext context) {
        if (sender instanceof ConsoleSender) {
            usage(sender, context);
            return;
        }

        Player player = ((Player)sender);

        RelativeVec relativeVec = context.get(location);
        Pos position = relativeVec.from(player).asPosition();

        teleport(player, position);
        player.sendMessage(Component.text("Teleported " + player.getUsername() + " to " +
                position.x() + ", " + position.y() + ", " + position.z()));
    }

    public void targetToLocation(CommandSender sender, CommandContext context) {
        RelativeVec relativeVec = context.get(location);
        EntityFinder entityTarget = context.get(targets);

        List<Entity> targetsEntity = entityTarget.find(sender);

        if (targetsEntity.size() > 0) {
            targetsEntity.stream()
                    .filter(entity -> !(entity instanceof Player) ||
                            !(sender instanceof Player) ||
                            !((Player)sender).getUuid().equals(entity.getUuid()))
                    .forEach(entity -> {
                        Pos position = relativeVec.from(entity).asPosition();
                        teleport(entity, position);
                        sendTeleportedMessage(sender, entity, position);
                    });
            sender.sendMessage(Component.text("Teleported target(s)"));
        } else {
            sender.sendMessage(Component.text("No target found", NamedTextColor.RED));
        }
    }

    public void targetToLocationWithDir(CommandSender sender, CommandContext context) {
        RelativeVec relativeVec = context.get(location);
        RelativeVec relativeDirection = context.get(direction);

        EntityFinder entityTarget = context.get(targets);
        List<Entity> targetsEntity = entityTarget.find(sender);

        if (targetsEntity.size() > 0) {
            targetsEntity.stream()
                    .filter(entity -> !(entity instanceof Player) ||
                            !(sender instanceof Player) ||
                            !((Player)sender).getUuid().equals(entity.getUuid()))
                    .forEach(entity -> {
                        Pos position = relativeVec.from(entity).asPosition().withDirection(relativeDirection.from(entity));
                        teleport(entity, position);
                        sendTeleportedMessage(sender, entity, position);
                    });
            sender.sendMessage(Component.text("Teleported target(s)"));
        } else {
            sender.sendMessage(Component.text("No target found", NamedTextColor.RED));
        }

    }

    public void targetToDestination(CommandSender sender, CommandContext context) {
        EntityFinder destinationFinder = context.get(destination);
        EntityFinder entityTarget = context.get(targets);

        List<Entity> targetsEntity = entityTarget.find(sender);
        Entity destination = destinationFinder.find(sender).get(0);

        if (targetsEntity.size() > 0) {
            targetsEntity.stream()
                    .filter(entity -> !(entity instanceof Player) ||
                            !(sender instanceof Player) ||
                            !((Player)sender).getUuid().equals(entity.getUuid()))
                    .forEach(entity -> {
                        Pos position = destination.getPosition();
                        teleport(entity, position);
                        sendTeleportedMessage(sender, entity, position);
                    });
            sender.sendMessage(Component.text("Teleported target(s)"));
        } else {
            sender.sendMessage(Component.text("No target found", NamedTextColor.RED));
        }
    }

    public void targetToDestinationWithDir(CommandSender sender, CommandContext context) {
        RelativeVec relativeVec = context.get(location);
        RelativeVec relativeDirection = context.get(direction);

        EntityFinder entityTarget = context.get(targets);
        List<Entity> targetsEntity = entityTarget.find(sender);

        if (targetsEntity.size() > 0) {
            targetsEntity.stream()
                    .filter(entity -> !(entity instanceof Player) ||
                            !(sender instanceof Player) ||
                            !((Player)sender).getUuid().equals(entity.getUuid()))
                    .forEach(entity -> {
                        Pos position = relativeVec.from(entity).asPosition().withDirection(relativeDirection.from(entity));
                        teleport(entity, position);
                        sendTeleportedMessage(sender, entity, position);
                    });
            sender.sendMessage(Component.text("Teleported target(s)"));
        } else {
            sender.sendMessage(Component.text("No target found", NamedTextColor.RED));
        }
    }

    public void teleport(Entity target, Pos result) {
        target.teleport(result);
    }

    private void sendTeleportedMessage(CommandSender sender, Entity entity, Pos position) {
        String entityName = entity.getCustomName() == null ?
                entity.getEntityType().name() : LegacyComponentSerializer.legacyAmpersand().serialize(entity.getCustomName());
        sender.sendMessage(Component.text("Teleported " + entityName + " to " +
                position.x() + ", " + position.y() + ", " + position.z()));
    }
}
