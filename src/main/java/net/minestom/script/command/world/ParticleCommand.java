package net.minestom.script.command.world;

import net.minestom.script.command.ScriptCommand;
import net.minestom.script.utils.ArgumentUtils;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import net.minestom.server.utils.Vector;
import net.minestom.server.utils.location.RelativeVec;

import static net.minestom.server.command.builder.arguments.ArgumentType.Float;
import static net.minestom.server.command.builder.arguments.ArgumentType.Integer;
import static net.minestom.server.command.builder.arguments.ArgumentType.*;

public class ParticleCommand extends ScriptCommand {
    public ParticleCommand() {
        super("particle");

        setDefaultExecutor((sender, args) -> sender.sendMessage("Usage: /particle <type> <position> <delta> <speed> <count>"));

        addSyntax((sender, args) -> {
                    final Particle particle = args.get("particle");
                    final RelativeVec relativePosition = args.get("position");
                    final RelativeVec relativeDelta = args.get("delta");
                    final float speed = args.get("speed");
                    final int count = args.get("count");

                    final Vector position = ArgumentUtils.from(sender, relativePosition);
                    final Vector delta = ArgumentUtils.from(sender, relativeDelta);

                    ParticlePacket particlePacket = ParticleCreator.createParticlePacket(
                            particle, false, position.getX(), position.getY(), position.getZ(),
                            (float) delta.getX(), (float) delta.getY(), (float) delta.getZ(), speed, count, null);

                    if (sender.isPlayer()) {
                        sender.asPlayer().sendPacketToViewersAndSelf(particlePacket);
                    }

                    sender.sendMessage("Particle(s) sent!");

                }, Particle("particle"), RelativeVec3("position"),
                RelativeVec3("delta"), Float("speed"),
                Integer("count"));
    }
}
