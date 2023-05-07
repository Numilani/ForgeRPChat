package me.numilani.forgerpchat.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChatRangeProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<ChatRange> PLAYER_CHATRANGE = CapabilityManager.get(new CapabilityToken<>() {});

    private ChatRange playerChatRange = null;
    private final LazyOptional<ChatRange> opt = LazyOptional.of(this::initRange);

    @NotNull
    private ChatRange initRange(){
        if (playerChatRange == null){
            playerChatRange = new ChatRange();
        }
        return playerChatRange;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PLAYER_CHATRANGE){
            return opt.cast();
        }
        return LazyOptional.empty();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        return getCapability(cap);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        initRange().saveNPT(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        initRange().loadNPT(nbt);
    }
}
