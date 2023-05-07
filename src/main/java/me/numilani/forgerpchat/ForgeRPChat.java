package me.numilani.forgerpchat;

import com.mojang.logging.LogUtils;
import me.numilani.forgerpchat.capability.ChatRange;
import me.numilani.forgerpchat.capability.ChatRangeProvider;
import me.numilani.forgerpchat.commands.ChatToRangeCommand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jline.utils.Log;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("forgerpchat")
public class ForgeRPChat {

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public ForgeRPChat() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
//        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Entity.class, ForgeRPChat::onAttachCapabilitiesPlayer);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addGenericListener(Entity.class, ForgeRPChat::onAttachCapabilitiesPlayer);
        MinecraftForge.EVENT_BUS.addListener(ForgeRPChat::onPlayerCloned);
        MinecraftForge.EVENT_BUS.addListener(ForgeRPChat::registerCaps);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Some preinit code
        LOGGER.info("HELLO FROM PREINIT");
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
//    @SubscribeEvent
//    public void onServerStarting(ServerStartingEvent event) {
//        // Do something when the server starts
//        LOGGER.info("HELLO from server xyz");
//    }

    @SubscribeEvent
    public void onChat(ServerChatEvent event){
        event.setCanceled(true);

        event.getPlayer()
                .getCapability(ChatRangeProvider.PLAYER_CHATRANGE)
                .ifPresent(rg -> ChatToRangeCommand.sendRangedChat(event.getPlayer(), event.getFilteredMessage(), rg.getRange()));

    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event){
        ChatRangeCommandRegister.register(event.getDispatcher());
    }

    public static void registerCaps(RegisterCapabilitiesEvent event) {
        LOGGER.info("registerCaps() called");
        event.register(ChatRange.class);
    }

    public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
        LOGGER.info("onAttachCapabilitiesPlayer() called");
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(ChatRangeProvider.PLAYER_CHATRANGE).isPresent()) {
                event.addCapability(new ResourceLocation("forgerpchat", "defaultchatrange"), new ChatRangeProvider());
            }
        }
    }

    public static void onPlayerCloned(PlayerEvent.Clone event){
        LOGGER.info("onPlayerCloned() called");
        if (event.isWasDeath()){
            event.getOriginal().getCapability(ChatRangeProvider.PLAYER_CHATRANGE).ifPresent(oldStore ->{
                event.getPlayer().getCapability(ChatRangeProvider.PLAYER_CHATRANGE).ifPresent(newStore ->{
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }


}
