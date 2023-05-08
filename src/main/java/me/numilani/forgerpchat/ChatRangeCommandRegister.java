package me.numilani.forgerpchat;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.numilani.forgerpchat.commands.ChatToRangeCommand;
import me.numilani.forgerpchat.commands.UpdateSelectedRangeCommand;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ChatRangeCommandRegister {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher)
    {
        LiteralCommandNode<CommandSourceStack> cmdTut = dispatcher.register(
                Commands.literal("range")
                        .then(ChatToRangeCommand.register(dispatcher))
                        .then(UpdateSelectedRangeCommand.register(dispatcher))
        );



    }
}
