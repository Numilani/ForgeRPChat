package me.numilani.forgerpchat;

import com.mojang.logging.LogUtils;
import me.numilani.forgerpchat.capability.ChatRangeCapability;
import me.numilani.forgerpchat.capability.ISelectedChatRange;
import me.numilani.forgerpchat.capability.SelectedChatRangeProvider;
import me.numilani.forgerpchat.commands.ChatToRangeCommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("forgerpchat")
public class ForgeRPChat {

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public ForgeRPChat() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
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
        ICapabilityProvider provider = event.getPlayer();
        event.getPlayer()
                .getCapability(SelectedChatRangeProvider.INSTANCE)
                .ifPresent(rg -> ChatToRangeCommand.sendRangedChat(event.getPlayer(), event.getFilteredMessage(), rg.getDefaultChatRange()));

//        var x = event.getPlayer().getCapability(ChatRangeCapability.INSTANCE);
//        if (x.resolve().isPresent()){
//            ChatToRangeCommand.sendRangedChat(event.getPlayer(), event.getFilteredMessage(), x.resolve().get().getDefaultChatRange());
//        }
//        ChatToRangeCommand.sendRangedChat(event.getPlayer(), event.getFilteredMessage());
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event){
        ChatRangeCommandRegister.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void registerCaps(RegisterCapabilitiesEvent event){
        ChatRangeCapability.register(event);
    }

}
