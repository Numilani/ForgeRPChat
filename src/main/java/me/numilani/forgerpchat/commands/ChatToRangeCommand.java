package me.numilani.forgerpchat.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;

public class ChatToRangeCommand implements Command<CommandSourceStack> {

    private static final ChatToRangeCommand CMD = new ChatToRangeCommand();

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return Commands.literal("say")
                .then(Commands.argument("range", StringArgumentType.word())
                        .then(Commands.argument("msg", StringArgumentType.greedyString())
                                .executes(CMD)));
    }

    public static void sendRangedChat(Player src, String msg, String range){
        var world = src.getCommandSenderWorld();

        double rangeDistance;
        String rangeColor;
        String rangeTag;
        boolean acrossWorlds = false;
        switch (range){
            case "global":
                rangeDistance = 100000;
                rangeColor = "§b";
                rangeTag = "[Global]";
                acrossWorlds = true;
                break;
            case "region":
                rangeDistance = 250;
                rangeColor = "§3";
                rangeTag = "[Region]";
                break;
            case "local":
                rangeDistance = 16;
                rangeColor = "§f";
                rangeTag = "[Local]";
                break;
            case "quiet":
                rangeDistance = 8;
                rangeColor = "§7";
                rangeTag = "[Quiet]";
                break;
            case "whisper":
                rangeDistance = 2;
                rangeColor = "§8";
                rangeTag = "[Whisper]";
                break;
            default:
                rangeDistance = 100000; // just default to global
                rangeColor = "§b";
                rangeTag = "[UnkRng]";
                break;
        }

        var nearbyPlayers = world.getEntitiesOfClass(src.getClass(), src.getBoundingBox().inflate(rangeDistance));

        for (Player recip :
                nearbyPlayers) {

            recip.sendMessage(new TextComponent(rangeColor + rangeTag + "§r " + src.getDisplayName().getString() + ": " + rangeColor + msg), recip.getUUID());
        }
    }
    public static void sendRangedChat(Player src, String msg){
        sendRangedChat(src, msg, "global");
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        sendRangedChat(context.getSource().getPlayerOrException(), context.getArgument("msg", String.class), context.getArgument("range", String.class));
        return 0;
    }
}
