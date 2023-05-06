package me.numilani.forgerpchat.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import me.numilani.forgerpchat.capability.ChatRangeCapability;
import me.numilani.forgerpchat.capability.SelectedChatRangeProvider;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.slf4j.Logger;

public class UpdateSelectedRangeCommand implements Command<CommandSourceStack> {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final UpdateSelectedRangeCommand CMD = new UpdateSelectedRangeCommand();

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher){
        return Commands.literal("switch")
                .then(Commands.argument("range", StringArgumentType.word())
                        .executes(CMD));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        LOGGER.info("UpdateSelectedRangeCommand.run() called!");
        context.getSource()
                .getPlayerOrException()
                .getCapability(SelectedChatRangeProvider.INSTANCE)
                .ifPresent(rg -> rg.setDefaultChatRange(context.getArgument("range", String.class)));
//        var x = context.getSource().getPlayerOrException().getCapability(ChatRangeCapability.INSTANCE);
//        if (x.resolve().isPresent()){
//            x.resolve().get().setDefaultChatRange(context.getArgument("range", String.class));
//        }
        return 0;
    }
}