package me.numilani.forgerpchat.capability;

import com.mojang.logging.LogUtils;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class SelectedChatRangeProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final ResourceLocation IDENTIFIER = new ResourceLocation("forgerpchat", "defaultchatrange");
    public static final Capability<ISelectedChatRange> INSTANCE = CapabilityManager.get(new CapabilityToken<>(){});

    private final ISelectedChatRange backend = new SelectedChatRangeImpl();
    private final LazyOptional<ISelectedChatRange> optData = LazyOptional.of(() -> backend);

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return INSTANCE.orEmpty(cap, optData);
    }

    @Override
    public CompoundTag serializeNBT() {
        return this.backend.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.backend.deserializeNBT(nbt);
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event){
        if (event.isWasDeath()){
            event.getOriginal().getCapability(SelectedChatRangeProvider.INSTANCE).ifPresent(old -> {
                event.getPlayer().getCapability(SelectedChatRangeProvider.INSTANCE).ifPresent(newstore -> {newstore.setDefaultChatRange(old.getDefaultChatRange());});
            });
        }
    }

    @SubscribeEvent
    public static void attach(AttachCapabilitiesEvent<Entity> event){
        LOGGER.info("ChatRangeProvider.attach() called!");
        if (event.getObject() instanceof Player){
            if (!event.getObject().getCapability(SelectedChatRangeProvider.INSTANCE).isPresent()){
                LOGGER.info(event.getObject().getDisplayName().getString() + " attached chat ranges");
                event.addCapability(IDENTIFIER, new SelectedChatRangeProvider());
            }
        }
    }

    private SelectedChatRangeProvider(){}
}
