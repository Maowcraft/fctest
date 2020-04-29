package maowcraft.fctest.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import maowcraft.fctest.FCTest;
import nerdhub.cardinal.components.api.component.ComponentProvider;
import net.minecraft.server.command.ServerCommandSource;

import static net.minecraft.server.command.CommandManager.literal;

public class SettingsCommand {
    private static boolean testToggle = false;

    public static LiteralCommandNode register(CommandDispatcher<ServerCommandSource> dispatcher) {
        return dispatcher.register(literal("setting")
                .executes(ctx -> set(ctx)));
    }

    public static int set(CommandContext<ServerCommandSource> ctx) {
        FCTest.setPropertyBoolean(ComponentProvider.fromWorld(ctx.getSource().getWorld()), 0, true);
        return 0;
    }
}
