package me.numilani.forgerpchat.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

public class ChatRangeCapability {

    public static void register(RegisterCapabilitiesEvent event){
        event.register(ISelectedChatRange.class);
    }

    private ChatRangeCapability(){}
}
