package net.minestom.script.command;

import net.minestom.script.command.entity.TeleportCommand;

public class EntityCommand extends ScriptCommand {
    public EntityCommand() {
        super("entity", ScriptCategory.ENTITY_MANIPULATION);

        addSubcommand(new TeleportCommand());
    }
}
