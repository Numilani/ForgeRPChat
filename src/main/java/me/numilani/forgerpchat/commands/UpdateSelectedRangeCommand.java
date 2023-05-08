package me.numilani.forgerpchat.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import me.numilani.forgerpchat.capability.ChatRangeProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import org.slf4j.Logger;

public class UpdateSelectedRangeCommand implements Command<CommandSourceStack> {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final UpdateSelectedRangeCommand CMD = new UpdateSelectedRangeCommand();

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return Commands.argument("range", StringArgumentType.word())
                        .executes(CMD);
    }



    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        switch (context.getArgument("range", String.class)){
            case "global", "region", "local", "quiet", "whisper":
                context.getSource()
                        .getPlayerOrException()
                        .getCapability(ChatRangeProvider.PLAYER_CHATRANGE)
                        .ifPresent(rg -> {
                            rg.setRange(context.getArgument("range", String.class));
                            try {
                                context.getSource().getPlayerOrException().sendMessage(new TextComponent("Now chatting in " + context.getArgument("range", String.class) + " range."), context.getSource().getPlayerOrException().getUUID());
                            } catch (CommandSyntaxException e) {
                                throw new RuntimeException(e);
                            }
                        });
                break;
            default:
                context.getSource().getPlayerOrException().sendMessage(new TextComponent(context.getArgument("range", String.class) + " is not a valid chat range."), context.getSource().getPlayerOrException().getUUID());
                break;
        }

        return 0;
    }
}
